package net.insprill.cjm.listener

import de.leonhard.storage.SimplixBuilder
import de.leonhard.storage.internal.FlatFile
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent
import java.nio.file.Path

class WorldChangeEvent(private val plugin: CustomJoinMessages) : Listener {

    private val worldLogConfig: FlatFile = SimplixBuilder.fromPath(Path.of("${plugin.dataFolder}/data/worlds.yml")).createYaml()

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerChangeWorld(e: PlayerTeleportEvent) {
        if (!plugin.config.getBoolean("World-Based-Messages.Enabled"))
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
        }

        val blacklist = plugin.config.getStringList("World-Blacklist")
        val whitelist = plugin.config.getBoolean("World-Blacklist-As-Whitelist")

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
            return plugin.config.getBoolean("World-Based-Messages.Ungrouped-Group")
        for (key in plugin.config.singleLayerKeySet("World-Based-Messages.Groups")) {
            val group = plugin.config.getStringList("World-Based-Messages.Groups.$key")
            if (group.contains(toName) && group.contains(fromName)) {
                return false
            }
        }
        return true
    }

    private fun isUngrouped(world: String): Boolean {
        val path = "World-Based-Messages.Groups"
        return plugin.config.singleLayerKeySet(path).none {
            plugin.config.getStringList("$path.$it").contains(world)
        }
    }

}
