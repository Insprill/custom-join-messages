package net.insprill.cjm.compatibility.authme

import fr.xephi.authme.api.v3.AuthMeApi
import fr.xephi.authme.events.LoginEvent
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.AuthHook
import net.insprill.cjm.messages.MessageAction
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class AuthMeAuthHook(private val plugin: CustomJoinMessages) : AuthHook {

    private val loggedInPlayers = HashSet<Player>()

    override fun isLoggedIn(player: Player): Boolean {
        return loggedInPlayers.contains(player)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onLogin(e: LoginEvent) {
        loggedInPlayers.add(e.player)
        handleJoin(e.player)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onJoin(e: PlayerJoinEvent) {
        e.joinMessage = ""
        if (AuthMeApi.getInstance().isUnrestricted(e.player)) {
            loggedInPlayers.add(e.player)
            handleJoin(e.player)
        }
    }

    private fun handleJoin(player: Player) {
        loggedInPlayers.add(player)
        val action = if (player.hasPlayedBefore()) MessageAction.JOIN else MessageAction.FIRST_JOIN
        plugin.messageSender.sendMessages(player, action, true)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onQuit(e: PlayerQuitEvent) {
        loggedInPlayers.remove(e.player)
    }

}
