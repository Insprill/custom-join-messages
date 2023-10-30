package net.insprill.cjm.listener

import de.leonhard.storage.SimplixBuilder
import de.leonhard.storage.internal.FlatFile
import de.leonhard.storage.internal.settings.ReloadSettings
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import net.insprill.cjm.util.CrossPlatformScheduler
import net.insprill.cjm.util.EnumUtils.tryGetEnum
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent
import java.nio.file.Paths
import kotlin.math.ceil

class WorldChangeEvent(private val plugin: CustomJoinMessages) : Listener {

    private val visitedWorldsConfig: FlatFile = SimplixBuilder.fromPath(Paths.get("${plugin.dataFolder}/data/worlds.json"))
        .setReloadSettings(ReloadSettings.MANUALLY)
        .createJson()
    private val groupPath = "World-Based-Messages.Groups"

    private val isEnabled: Boolean
        get() = plugin.config.getBoolean("World-Based-Messages.Enabled")
    private val minimumWorldTime: Int
        get() = plugin.config.getInt("World-Based-Messages.Minimum-World-Time")

    private val worldJoinTimes = HashMap<String, Long>()

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

        if (!checkTime(e.player))
            return

        worldJoinTimes[e.player.uniqueId.toString()] = System.currentTimeMillis()

        trySendLater(e.player, toWorld)
    }

    private fun trySendLater(player: Player, toWorld: World) {
        val hasJoinedWorldBefore = saveVisitedWorld(player, toWorld)

        plugin.messageSender.trySendMessages(player, MessageAction.QUIT, true)

        CrossPlatformScheduler.runDelayed(plugin, {
            if (!checkTime(player))
                return@runDelayed
            plugin.messageSender.trySendMessages(
                player,
                if (hasJoinedWorldBefore) MessageAction.JOIN else MessageAction.FIRST_JOIN,
                true
            )
        }, ceil(minimumWorldTime / 50.0f).toLong() + 10L)
    }

    private fun checkTime(player: Player): Boolean {
        val uuid = player.uniqueId.toString()
        val time = System.currentTimeMillis()
        return time - worldJoinTimes.getOrDefault(uuid, 0) >= minimumWorldTime
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(e: PlayerQuitEvent) {
        if (!isEnabled)
            return

        worldJoinTimes.remove(e.player.uniqueId.toString())
    }

    fun saveVisitedWorld(player: Player, world: World): Boolean {
        if (!isEnabled)
            return false
        worldJoinTimes[player.uniqueId.toString()] = System.currentTimeMillis()
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
