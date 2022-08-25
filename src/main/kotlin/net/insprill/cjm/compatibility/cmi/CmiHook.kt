package net.insprill.cjm.compatibility.cmi

import com.Zrips.CMI.CMI
import com.Zrips.CMI.Containers.CMIUser
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.PluginHook
import org.bukkit.entity.Player

class CmiHook(plugin: CustomJoinMessages) : PluginHook {

    override val authHook = null
    override val vanishHook = CmiVanishHook(plugin, this)
    override val jailHook = CmiJailHook(this)

    fun getUser(player: Player): CMIUser {
        return CMI.getInstance().playerManager.getUser(player)
    }

}
