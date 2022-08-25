package net.insprill.cjm.listener

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import net.insprill.xenlib.files.YamlFile
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent
import java.io.File

class WorldChangeEvent(private val plugin: CustomJoinMessages) : Listener {

    val worldLogConfig = YamlFile("data" + File.separator + "worlds.yml")

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerChangeWorld(e: PlayerTeleportEvent) {
        if (!YamlFile.CONFIG.getBoolean("World-Based-Messages.Enabled"))
            return

        val from = e.from
        val to = e.to
        val fromWorld = from.world ?: return
        val toWorld = to.world ?: return

        if (toWorld == fromWorld)
            return

        val fromName = fromWorld.name
        val toName = toWorld.name
        if (!isDifferentGroup(toName, fromName))
            return

        val toPlayers = worldLogConfig.getStringList(toName)
        val uuid = e.player.uniqueId.toString()
        val hasJoinedWorldBefore = toPlayers.contains(uuid)
        if (!hasJoinedWorldBefore) {
            toPlayers.add(uuid)
            worldLogConfig[fromName] = toPlayers
            worldLogConfig[toName] = toPlayers
            worldLogConfig.save()
        }

        val blacklist = YamlFile.CONFIG.getStringList("World-Blacklist")
        val whitelist = YamlFile.CONFIG.getBoolean("World-Blacklist-As-Whitelist")

        if (whitelist xor !blacklist.contains(fromName)) {
            plugin.messageSender.trySendMessages(e.player, MessageAction.QUIT, true)
        }
        if (whitelist xor !blacklist.contains(toName)) {
            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                plugin.messageSender.trySendMessages(
                    e.player,
                    if (hasJoinedWorldBefore) MessageAction.JOIN else MessageAction.FIRST_JOIN,
                    true
                )
            }, 10L)
        }
    }

    private fun isDifferentGroup(toName: String, fromName: String): Boolean {
        if (isUngrouped(toName) != isUngrouped(fromName))
            return YamlFile.CONFIG.getBoolean("World-Based-Messages.Ungrouped-Group")
        for (key in YamlFile.CONFIG.getKeys("World-Based-Messages.Groups")) {
            val group = YamlFile.CONFIG.getStringList("World-Based-Messages.Groups.$key")
            if (group.contains(toName) && group.contains(fromName)) {
                return false
            }
        }
        return true
    }

    private fun isUngrouped(world: String): Boolean {
        val path = "World-Based-Messages.Groups"
        return YamlFile.CONFIG.getKeys(path).none {
            YamlFile.CONFIG.getStringList("$path.$it").contains(world)
        }
    }

}
