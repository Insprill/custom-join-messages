package net.insprill.cjm.util

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.jetbrains.annotations.ApiStatus

object FastOfflinePlayers : Listener {

    var count: Int = Bukkit.getOfflinePlayers().size; private set

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(e: PlayerJoinEvent) {
        if (!e.player.hasPlayedBefore())
            count++
    }

    @ApiStatus.Internal
    fun reset() {
        count = 0
    }

}
