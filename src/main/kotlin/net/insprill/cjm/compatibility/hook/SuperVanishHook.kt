package net.insprill.cjm.compatibility.hook

import de.myzelyam.api.vanish.PlayerVanishStateChangeEvent
import net.insprill.cjm.CustomJoinMessages
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class SuperVanishHook(private val plugin: CustomJoinMessages) : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onVanishToggle(e: PlayerVanishStateChangeEvent) {
        val player = Bukkit.getPlayer(e.uuid) ?: return // Why the hell can it return a UUID for a player that doesn't exist??
        plugin.vanish.handleToggle(player, e.isVanishing)
    }

}