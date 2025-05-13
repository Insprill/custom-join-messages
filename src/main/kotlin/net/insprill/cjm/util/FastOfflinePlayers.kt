package net.insprill.cjm.util

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.UUID

object FastOfflinePlayers : Listener {

    val offlinePlayers: HashSet<UUID> = Bukkit.getOfflinePlayers().map { it.uniqueId }.toHashSet()

    val count: Int get() = offlinePlayers.size

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(e: PlayerJoinEvent) {
        offlinePlayers.add(e.player.uniqueId)
    }

}
