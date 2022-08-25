package net.insprill.cjm.compatibility.cmi

import net.insprill.cjm.compatibility.hook.JailHook
import org.bukkit.entity.Player

class CmiJailHook(private val cmiHook: CmiHook) : JailHook {

    override fun isJailed(player: Player): Boolean {
        return cmiHook.getUser(player).isJailed
    }

}
