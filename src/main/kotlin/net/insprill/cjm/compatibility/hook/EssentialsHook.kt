package net.insprill.cjm.compatibility.hook

import com.earth2me.essentials.Essentials
import net.ess3.api.events.VanishStatusChangeEvent
import net.insprill.cjm.CustomJoinMessages
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class EssentialsHook(private val plugin: CustomJoinMessages) : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onVanish(e: VanishStatusChangeEvent) {
        plugin.vanish.handleToggle(e.affected.base, e.value)
    }

}
