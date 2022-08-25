package net.insprill.cjm.compatibility.hook

import org.bukkit.entity.Player
import org.bukkit.event.Listener

interface AuthHook : Listener {

    fun isLoggedIn(player: Player): Boolean

}
