package net.insprill.cjm.compatibility.essentials

import net.ess3.api.events.VanishStatusChangeEvent
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.VanishHook
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

class EssentialsVanishHook(override val plugin: CustomJoinMessages, private val essHook: EssentialsHook) : VanishHook {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onVanish(e: VanishStatusChangeEvent) {
        handleToggle(e.controller.base, e.value)
    }

    override fun isVanished(player: Player): Boolean {
        return essHook.getUser(player).isVanished
    }

}
