package net.insprill.cjm.compatibility.phoenix

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.VanishHook
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import xyz.refinedev.phoenix.utils.events.PlayerModModeDisableEvent
import xyz.refinedev.phoenix.utils.events.PlayerModModeEnableEvent

class PhoenixVanishHook(
    override val plugin: CustomJoinMessages,
) : VanishHook, Listener {

    private val vanishedPlayers = mutableSetOf<Player>()
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onVanish(e: PlayerModModeEnableEvent) {
        if (e.isModMode) {
            vanishedPlayers.add(e.player)
            handleToggle(e.player, true)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onUnVanish(e: PlayerModModeDisableEvent) {
        if (!e.isModMode) {
            vanishedPlayers.remove(e.player)
            handleToggle(e.player, false)
        }
    }

    override fun isVanished(player: Player): Boolean {
        return player in vanishedPlayers
    }
}
