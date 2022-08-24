package net.insprill.cjm.compatibility.supervanish

import de.myzelyam.api.vanish.PlayerVanishStateChangeEvent
import de.myzelyam.api.vanish.VanishAPI
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.VanishHook
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class SuperVanishVanishHook(override val plugin: CustomJoinMessages) : VanishHook, Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onVanishToggle(e: PlayerVanishStateChangeEvent) {
        val player = Bukkit.getPlayer(e.uuid) ?: return // Why the hell can it return a UUID for a player that doesn't exist??
        handleToggle(player, e.isVanishing)
    }

    override fun isVanished(player: Player): Boolean {
        return VanishAPI.isInvisible(player)
    }

}
