package net.insprill.cjm.listener

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinEvent(private val plugin: CustomJoinMessages) : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.joinMessage = ""
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            val action = if (e.player.hasPlayedBefore()) MessageAction.JOIN else MessageAction.FIRST_JOIN
            plugin.messageSender.trySendMessages(e.player, action, true)
        }, 10L)
    }

}
