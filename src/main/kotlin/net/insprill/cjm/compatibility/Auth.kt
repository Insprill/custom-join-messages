package net.insprill.cjm.compatibility

import org.bukkit.entity.Player

class Auth {

    private val loggedInPlayers = HashSet<Player>()

    fun markLoggedIn(player: Player) {
        loggedInPlayers.add(player)
    }

    fun markLoggedOut(player: Player) {
        loggedInPlayers.remove(player)
    }

    fun isLoggedIn(player: Player): Boolean {
        return loggedInPlayers.contains(player)
    }

}