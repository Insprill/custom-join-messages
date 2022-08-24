package net.insprill.cjm.compatibility.essentials

import net.insprill.cjm.compatibility.hook.JailHook
import org.bukkit.entity.Player

class EssentialsJailHook(private val essHook: EssentialsHook) : JailHook {

    override fun isJailed(player: Player): Boolean {
        return essHook.getUser(player).isJailed
    }

}
