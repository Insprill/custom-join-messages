package net.insprill.cjm.compatibility.advancedvanish

import me.quantiom.advancedvanish.event.PlayerUnVanishEvent
import me.quantiom.advancedvanish.event.PlayerVanishEvent
import me.quantiom.advancedvanish.util.AdvancedVanishAPI
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.VanishHook
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

class AdvancedVanishVanishHook(override val plugin: CustomJoinMessages) : VanishHook {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onVanishToggle(e: PlayerVanishEvent) {
        handleToggle(e.player, true)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onVanishToggle(e: PlayerUnVanishEvent) {
        handleToggle(e.player, false)
    }

    override fun isVanished(player: Player): Boolean {
        return AdvancedVanishAPI.INSTANCE.isPlayerVanished(player)
    }

}
