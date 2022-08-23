package net.insprill.cjm.compatibility.hook

import net.insprill.cjm.CustomJoinMessages
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.kitteh.vanish.event.VanishStatusChangeEvent

class VanishNoPacketHook(private val plugin: CustomJoinMessages) : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onVanishToggle(e: VanishStatusChangeEvent) {
        plugin.vanish.handleToggle(e.player, e.isVanishing)
    }

}