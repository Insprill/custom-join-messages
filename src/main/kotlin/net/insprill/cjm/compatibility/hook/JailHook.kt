package net.insprill.cjm.compatibility.hook

import org.bukkit.entity.Player

interface JailHook {

    fun isJailed(player: Player): Boolean

}
