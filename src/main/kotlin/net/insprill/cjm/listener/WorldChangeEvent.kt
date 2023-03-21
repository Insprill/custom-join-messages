package net.insprill.cjm.listener

import de.leonhard.storage.SimplixBuilder
import de.leonhard.storage.internal.FlatFile
import de.leonhard.storage.internal.settings.ReloadSettings
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import net.insprill.cjm.util.EnumUtils.tryGetEnum
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent
import java.nio.file.Paths

class WorldChangeEvent(private val plugin: CustomJoinMessages) : Listener {

    private val visitedWorldsConfig: FlatFile = SimplixBuilder.fromPath(Paths.get("${plugin.dataFolder}/data/worlds.json"))
        .setReloadSettings(ReloadSettings.MANUALLY)
        .createJson()
    private val groupPath = "World-Based-Messages.Groups"

    private val isEnabled: Boolean
        get() = plugin.config.getBoolean("World-Based-Messages.Enabled")

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerChangeWorld(e: PlayerTeleportEvent) {
        if (!isEnabled)
            return

        val fromWorld = e.from.world ?: return
        val toWorld = e.to?.world ?: return
        if (toWorld == fromWorld)
            return

        if (isSameGroup(toWorld.name, fromWorld.name))
            return

        val hasJoinedWorldBefore = saveVisitedWorld(e.player, toWorld)

        plugin.messageSender.trySendMessages(e.player, MessageAction.QUIT, true)

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            plugin.messageSender.trySendMessages(
                e.player,
                if (hasJoinedWorldBefore) MessageAction.JOIN else MessageAction.FIRST_JOIN,
                true
            )
        }, 10L)
    }

    fun saveVisitedWorld(player: Player, world: World): Boolean {
        if (!isEnabled)
            return false
        val groupName = getGroupName(world.name)
        val uuid = player.uniqueId.toString()
        val groupPlayers = visitedWorldsConfig.getStringList(groupName)
        val hasJoinedWorldBefore = groupPlayers.contains(uuid)
        if (!hasJoinedWorldBefore) {
            groupPlayers.add(uuid)
            visitedWorldsConfig[groupName] = groupPlayers
        }
        return hasJoinedWorldBefore
    }

    private fun isSameGroup(toName: String, fromName: String): Boolean {
        return getGroupName(toName) == getGroupName(fromName)
                || (UngroupedMode.getMode(plugin) == UngroupedMode.NONE && (isUngrouped(toName) || isUngrouped(fromName)))
    }

    private fun getGroupName(worldName: String): String {
        if (!isUngrouped(worldName)) {
            return plugin.config.singleLayerKeySet(groupPath).first {
                plugin.config.getStringList("$groupPath.$it").contains(worldName)
            }
        }
        val mode = UngroupedMode.getMode(plugin) ?: return worldName
        return when (mode) {
            UngroupedMode.NONE -> ""
            UngroupedMode.SAME -> "ungrouped"
            UngroupedMode.INDIVIDUAL -> worldName
        }
    }

    private fun isUngrouped(world: String): Boolean {
        return plugin.config.singleLayerKeySet(groupPath).none {
            plugin.config.getStringList("$groupPath.$it").contains(world)
        }
    }

    private enum class UngroupedMode {
        NONE,
        SAME,
        INDIVIDUAL;

        companion object {
            fun getMode(plugin: CustomJoinMessages): UngroupedMode? {
                val modeStr = plugin.config.getString("World-Based-Messages.Ungrouped-Mode")
                return tryGetEnum(plugin, modeStr, UngroupedMode::class)
            }
        }
    }

}
