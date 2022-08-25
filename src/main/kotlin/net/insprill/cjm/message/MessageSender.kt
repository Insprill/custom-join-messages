package net.insprill.cjm.message

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.types.MessageType
import net.insprill.xenlib.XenMath
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
     * Handles sending messages for a player.
     *
     * @param player      Player who joined/ left.
     * @param action      Action the player performed.
     * @param vanishCheck Whether to check if the player is vanished before sending messages.
     */
    fun sendMessages(player: Player, action: MessageAction, vanishCheck: Boolean) {
        if (action == MessageAction.QUIT && !plugin.hookManager.isLoggedIn(player))
            return
        if (vanishCheck && plugin.hookManager.isVanished(player))
            return
        if (!YamlFile.CONFIG.getBoolean("Addons.Jail.Ignore-Jailed-Players") && plugin.hookManager.isJailed(player))
            return
        for (visibility in MessageVisibility.values()) {
            if (visibility == MessageVisibility.PRIVATE && action == MessageAction.QUIT)
                continue
            for (msg in typeMap.values) {
                if (!msg.isEnabled)
                    continue

                val path = visibility.configSection + "." + action.configSection

                // Get the highest priority message the player has access to.
                val hp = msg.config.getKeys(path).stream()
                    .filter { key: String? -> XenMath.isInteger(key) }
                    .filter { num: String ->
                        val perm = msg.config.getString("$path.$num.Permission")
                        perm != null && player.hasPermission(perm)
                    }
                    .mapToInt { s: String -> s.toInt() }
                    .max()
                    .orElse(-1)
                if (hp == -1)
                    continue

                val messagePath = "$path.$hp"

                val maxPlayers = msg.config.getInt("$messagePath.Max-Players")
                if (maxPlayers > 0 && Bukkit.getOnlinePlayers().size > maxPlayers)
                    continue

                val minPlayers = msg.config.getInt("$messagePath.Min-Players")
                if (minPlayers > 0 && Bukkit.getOnlinePlayers().size < minPlayers)
                    continue

                val radius = msg.config.getDouble("$messagePath.Radius")
                val players: List<Player>
                if (visibility == MessageVisibility.PUBLIC) {
                    players = ArrayList(getNearbyPlayers(player, radius, YamlFile.CONFIG.getBoolean("World-Based-Messages.Enabled")))
                    if (action == MessageAction.QUIT && YamlFile.CONFIG.getBoolean("World-Based-Messages.Enabled")) {
                        players.remove(player)
                    }
                } else {
                    players = listOf(player)
                }
                val randomKey = getRandomKey(msg.config, "$messagePath.${msg.key}") ?: continue
                val delay = msg.config.getLong("$messagePath.Delay")
                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    msg.handle(player, players, path, randomKey, visibility)
                }, delay)
            }
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
