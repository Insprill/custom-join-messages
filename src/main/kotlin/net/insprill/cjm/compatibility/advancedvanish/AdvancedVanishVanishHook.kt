package net.insprill.cjm.compatibility.advancedvanish

import me.quantiom.advancedvanish.event.PlayerUnVanishEvent
import me.quantiom.advancedvanish.event.PlayerVanishEvent
import me.quantiom.advancedvanish.util.AdvancedVanishAPI
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.VanishHook
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerQuitEvent

class AdvancedVanishVanishHook(override val plugin: CustomJoinMessages) : VanishHook {

    private var isHandlingQuit = false
    private var wasLastPlayerVanished = false

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerQuitLowest(e: PlayerQuitEvent) {
        wasLastPlayerVanished = isVanished(e.player) // Before setting isHandlingQuit
        isHandlingQuit = true
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerQuitMonitor(e: PlayerQuitEvent) {
        isHandlingQuit = false
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerVanish(e: PlayerVanishEvent) {
        if (e.onJoin) return
        handleToggle(e.player, true)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerUnvanish(e: PlayerUnVanishEvent) {
        if (isHandlingQuit)
            return
        handleToggle(e.player, false)
    }

    override fun isVanished(player: Player): Boolean {
        return if (isHandlingQuit) {
            wasLastPlayerVanished
        } else {
            AdvancedVanishAPI.INSTANCE.isPlayerVanished(player)
        }
    }

}
