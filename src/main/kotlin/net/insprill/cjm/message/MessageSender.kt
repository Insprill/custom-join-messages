package net.insprill.cjm.message

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.types.MessageType
import net.insprill.xenlib.XenUtils
import net.insprill.xenlib.files.YamlFile
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import kotlin.random.Random.Default.nextInt


class MessageSender(private val plugin: CustomJoinMessages, messageTypes: List<MessageType>) {

    val typeMap = messageTypes.associateBy { t -> t.name.lowercase() }

    private val registeredPermissions: MutableList<String> = ArrayList()

    fun setupPermissions() {
        for (permission in registeredPermissions) {
            XenUtils.unregisterPermission(permission)
        }
        registeredPermissions.clear()
        for (msg in typeMap.values) {
            if (!msg.isEnabled) continue
            for (action in MessageAction.values()) {
                for (visibility in MessageVisibility.values()) {
                    val path = visibility.configSection + "." + action.configSection
                    for (key in msg.config.getKeys(path)) {
                        val permission = msg.config.getString("$path.$key.Permission") ?: continue
                        XenUtils.registerPermission(permission, PermissionDefault.FALSE)
                        registeredPermissions.add(permission)
                    }
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
        if (vanishCheck && plugin.hookManager.isVanished(player))
            return
        if (!YamlFile.CONFIG.getBoolean("Addons.Jail.Ignore-Jailed-Players") && plugin.hookManager.isJailed(player))
            return
        for (visibility in MessageVisibility.values().filter { it.supports(action) }) {
            for (msg in typeMap.values.filter { it.isEnabled }) {
                val path = visibility.configSection + "." + action.configSection

                // Get the highest priority message the player has access to.
                val hp = getHighestPriorityMessage(msg, path, player)
                if (hp == -1)
                    continue

                val messagePath = "$path.$hp"
                if (!MessageCondition.checkAllConditions(plugin, msg, messagePath))
                    continue

                val radius = msg.config.getDouble("$messagePath.Radius")

                val players = getReceivingPlayers(player, visibility, action, radius)
                val randomKey = getRandomKey(msg.config, "$messagePath.${msg.key}") ?: continue
                val delay = msg.config.getLong("$messagePath.Delay")
                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    msg.handle(player, players, path, randomKey, visibility)
                }, delay)
            }
        }
    }

    private fun getHighestPriorityMessage(msgType: MessageType, path: String, player: Player): Int {
        return msgType.config.getKeys(path)
            .mapNotNull { it.toIntOrNull() }
            .filter { player.hasPermission(msgType.config.getString("$path.$it.Permission")!!) }
            .max()
    }

    private fun getReceivingPlayers(sourcePlayer: Player, visibility: MessageVisibility, action: MessageAction, radius: Double): List<Player> {
        return if (visibility == MessageVisibility.PUBLIC) {
            val players = ArrayList(getNearbyPlayers(sourcePlayer, radius, YamlFile.CONFIG.getBoolean("World-Based-Messages.Enabled")))
            if (action == MessageAction.QUIT && YamlFile.CONFIG.getBoolean("World-Based-Messages.Enabled")) {
                players.remove(sourcePlayer)
            }
            players
        } else {
            listOf(sourcePlayer)
        }
    }

    fun getRandomKey(config: YamlFile, path: String): String? {
        if (config.getConfigSection(path) == null) {
            plugin.logger.severe("\"" + path + "\" in messages/" + config.file.name + " has no keys. Perhaps the messages indentation is wrong?")
            return null
        }
        val amount = config.getConfigSection(path)!!.getKeys(false).size
        val selectedMessage = nextInt(amount) + 1
        return "$path.$selectedMessage"
    }

    private fun getNearbyPlayers(player: Player, radius: Double, sameWorldOnly: Boolean): List<Player> {
        return if (radius < 1) {
            if (!sameWorldOnly) {
                ArrayList(Bukkit.getOnlinePlayers())
            } else {
                player.world.players
            }
        } else {
            player.getNearbyEntities(radius, radius, radius).filterIsInstance<Player>()
        }
    }

}