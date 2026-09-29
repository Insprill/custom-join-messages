package net.insprill.cjm.compatibility.essentials

import com.earth2me.essentials.Essentials
import net.insprill.cjm.compatibility.hook.JailHook
import org.bukkit.entity.Player

class EssentialsJailHook(private val essHook: EssentialsHook) : JailHook {

    override fun isJailed(player: Player): Boolean {
        return essHook.getUser(player).isJailed
    }

    override fun isInUse(): Boolean {
        return Essentials.getPlugin(Essentials::class.java).jails.count > 0
    }

}
