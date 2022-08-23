package net.insprill.cjm.compatibility.hook

import fr.xephi.authme.api.v3.AuthMeApi
import fr.xephi.authme.events.LoginEvent
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.messages.MessageAction
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class AuthMeHook(private val plugin: CustomJoinMessages) : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onLogin(e: LoginEvent) {
        sendMessage(e.player)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onJoin(e: PlayerJoinEvent) {
        e.joinMessage = ""
        if (AuthMeApi.getInstance().isUnrestricted(e.player)) {
            sendMessage(e.player)
        }
    }

    private fun sendMessage(player: Player) {
        plugin.auth.markLoggedIn(player)
        val action = if (player.hasPlayedBefore()) MessageAction.JOIN else MessageAction.FIRST_JOIN
        plugin.messageSender.sendMessages(player, action, true)
    }

}