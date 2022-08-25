package net.insprill.cjm.listener

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class QuitEvent(private val plugin: CustomJoinMessages) : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(e: PlayerQuitEvent) {
        e.quitMessage = ""
        plugin.messageSender.trySendMessages(e.player, MessageAction.QUIT, true)
    }

}
