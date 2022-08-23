package net.insprill.cjm.compatibility.hook

import com.Zrips.CMI.events.CMIPlayerUnVanishEvent
import com.Zrips.CMI.events.CMIPlayerVanishEvent
import net.insprill.cjm.CustomJoinMessages
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class CmiHook(private val plugin: CustomJoinMessages) : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onVanish(e: CMIPlayerVanishEvent) {
        plugin.vanish.handleToggle(e.player, true)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onVanish(e: CMIPlayerUnVanishEvent) {
        plugin.vanish.handleToggle(e.player, false)
    }

}
