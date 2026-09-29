package net.insprill.cjm.compatibility.cmi

import com.Zrips.CMI.CMI
import net.insprill.cjm.compatibility.hook.JailHook
import org.bukkit.entity.Player

class CmiJailHook(private val cmiHook: CmiHook) : JailHook {

    override fun isJailed(player: Player): Boolean {
        return cmiHook.getUser(player).isJailed
    }

    override fun isInUse(): Boolean {
        return !CMI.getInstance().jailManager.jails.isNullOrEmpty()
    }

}
