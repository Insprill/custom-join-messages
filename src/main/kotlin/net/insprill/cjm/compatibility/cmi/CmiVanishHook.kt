package net.insprill.cjm.compatibility.cmi

import com.Zrips.CMI.Modules.Vanish.VanishAction
import com.Zrips.CMI.events.CMIPlayerUnVanishEvent
import com.Zrips.CMI.events.CMIPlayerVanishEvent
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.VanishHook
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

class CmiVanishHook(override val plugin: CustomJoinMessages, private val cmiHook: CmiHook) : VanishHook {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onVanish(e: CMIPlayerVanishEvent) {
        handleToggle(e.player, true)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onUnVanish(e: CMIPlayerUnVanishEvent) {
        handleToggle(e.player, false)
    }

    override fun isVanished(player: Player): Boolean {
        return cmiHook.getUser(player).vanish.`is`(VanishAction.isVanished)
    }

}
