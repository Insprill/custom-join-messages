package net.insprill.cjm.message

import de.leonhard.storage.internal.FlatFile
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.extension.getMessage
import net.insprill.cjm.message.types.MessageType
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault

class MessageSender(private val plugin: CustomJoinMessages) {

    val typeMap = HashMap<String, MessageType>()

    private val registeredPermissions = ArrayList<String>()

    fun registerType(messageType: MessageType) {
        typeMap[messageType.name.lowercase()] = messageType
    }

    fun reloadPermissions(config: FlatFile) {
        registeredPermissions.forEach { Bukkit.getPluginManager().removePermission(it) }
        registeredPermissions.clear()
        for (action in MessageAction.values()) {
            for (visibility in MessageVisibility.values()) {
                val path = visibility.configSection + "." + action.configSection
                for (key in config.singleLayerKeySet(path)) {
                    val permission = config.getString("$path.$key.Permission") ?: continue
                    if (Bukkit.getPluginManager().getPermission(permission) == null && permission != "cjm.default") {
                        Bukkit.getPluginManager().addPermission(Permission(permission, PermissionDefault.FALSE))
                    }
                    registeredPermissions.add(permission)
                }
            }
        }
    }

    /**
     * Handles sending messages for a player. May or may not send messages depending on if all conditions are met.
     *
     * @param player      Player who joined/ left.
     * @param action      Action the player performed.
     * @param vanishCheck Whether to check if the player is vanished before sending messages.
     */
    fun trySendMessages(player: Player, action: MessageAction, vanishCheck: Boolean) {
        if (!action.canRun(plugin, player))
            return
        if (!plugin.toggleHandler.isToggled(player, action))
            return
        if (vanishCheck && plugin.hookManager.isVanished(player))
            return
        if (!plugin.config.getBoolean("Addons.Jail.Ignore-Jailed-Players") && plugin.hookManager.isJailed(player))
            return
        for (visibility in MessageVisibility.values().filter { it.supports(action) }) {
            for (msg in typeMap.values.filter { it.isEnabled }) {
                val path = visibility.configSection + "." + action.configSection

                // Get the highest priority message the player has access to.
                val hp = getHighestPriorityMessage(msg, path, player)
                if (hp == -1)
                    continue

                val messagePath = "$path.$hp"
                if (!MessageCondition.checkAllConditions(msg, messagePath))
                    continue

                val radius = msg.config.getOrDefault("$messagePath.Radius", -1.0)

                val players = getReceivingPlayers(player, visibility, action, radius)
                val randomKey = getRandomKey(msg.config, "$messagePath.${msg.key}") ?: continue
                val delay = msg.config.getLong("$messagePath.Delay")
                val runnable = Runnable { msg.handle(player, players, randomKey, visibility) }
                if (delay < 1) {
                    runnable.run()
                } else {
                    Bukkit.getScheduler().runTaskLater(plugin, runnable, delay)
                }
            }
        }
    }

    private fun getHighestPriorityMessage(msgType: MessageType, path: String, player: Player): Int {
        return msgType.config.singleLayerKeySet(path)
            .mapNotNull { it.toIntOrNull() }
            .filter { player.hasPermission(msgType.config.getOrDefault("$path.$it.Permission", "cjm.default")) }
            .maxOrNull() ?: -1
    }

    private fun getReceivingPlayers(sourcePlayer: Player, visibility: MessageVisibility, action: MessageAction, radius: Double): List<Player> {
        if (visibility != MessageVisibility.PUBLIC) {
            return listOf(sourcePlayer)
        }
        val players = ArrayList(getNearbyPlayers(sourcePlayer, radius, plugin.config.getBoolean("World-Based-Messages.Enabled")))
        if (action == MessageAction.QUIT && plugin.config.getBoolean("World-Based-Messages.Enabled")) {
            players.remove(sourcePlayer)
        }
        return players
    }

    fun getRandomKey(config: FlatFile, path: String): String? {
        val section = config.getSection(path)
        if (section == null || section.singleLayerKeySet().isEmpty()) {
            plugin.logger.severe(plugin.commandManager.getMessage("cjm.message-sender.no-keys", "%path%", path, "%config%", config.file.name))
            return null
        }
        return "$path.${section.singleLayerKeySet().random()}"
    }

    private fun getNearbyPlayers(player: Player, radius: Double, sameWorldOnly: Boolean): List<Player> {
        if (radius > 0) {
            val curr = player.location
            return player.world.players.filter { it.location.distance(curr) <= radius }
        }
        return if (!sameWorldOnly) {
            ArrayList(Bukkit.getOnlinePlayers())
        } else {
            player.world.players
        }
    }

}
