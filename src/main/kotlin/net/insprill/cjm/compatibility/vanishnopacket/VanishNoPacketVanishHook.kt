package net.insprill.cjm.compatibility.vanishnopacket

import de.myzelyam.api.vanish.VanishAPI
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.VanishHook
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.kitteh.vanish.event.VanishStatusChangeEvent

class VanishNoPacketVanishHook(override val plugin: CustomJoinMessages) : VanishHook {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onVanishToggle(e: VanishStatusChangeEvent) {
        handleToggle(e.player, e.isVanishing)
    }

    override fun isVanished(player: Player): Boolean {
        return VanishAPI.isInvisible(player)
    }

}
