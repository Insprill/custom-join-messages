package net.insprill.cjm.compatibility.locklogin

import eu.locklogin.api.module.plugin.api.event.ModuleEventHandler
import eu.locklogin.api.module.plugin.api.event.user.UserPostJoinEvent
import eu.locklogin.api.module.plugin.api.event.util.EventListener
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.AuthHook
import net.insprill.cjm.message.MessageAction
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class LockLoginAuthHook(private val plugin: CustomJoinMessages, private val module: LockLoginModule) : AuthHook, EventListener {

    private val loggedInPlayers = HashSet<Player>()

    override fun isLoggedIn(player: Player): Boolean {
        return loggedInPlayers.contains(player)
    }

    @ModuleEventHandler(priority = ModuleEventHandler.Priority.LAST)
    fun onLogin(e: UserPostJoinEvent) {
        loggedInPlayers.add(e.player.getPlayer())
        handleJoin(e.player.getPlayer())
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onJoin(e: PlayerJoinEvent) {
        e.joinMessage = ""
        // TODO
//        if (AuthMeApi.getInstance().isUnrestricted(e.player)) {
//            loggedInPlayers.add(e.player)
//            handleJoin(e.player)
//        }
    }

    private fun handleJoin(player: Player) {
        loggedInPlayers.add(player)
        val action = if (player.hasPlayedBefore()) MessageAction.JOIN else MessageAction.FIRST_JOIN
        plugin.messageSender.trySendMessages(player, action, true)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onQuit(e: PlayerQuitEvent) {
        loggedInPlayers.remove(e.player)
    }

}
