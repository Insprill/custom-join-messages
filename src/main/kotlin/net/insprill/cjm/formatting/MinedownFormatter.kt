package net.insprill.cjm.formatting

import de.themoep.minedown.MineDown
import net.insprill.spigotutils.MinecraftVersion
import net.md_5.bungee.api.chat.BaseComponent

class MinedownFormatter : Formatter {

    override fun format(str: String): Array<BaseComponent> {
        return MineDown.parse(str)
    }

    companion object {
        fun isCompatible(): Boolean {
            return MinecraftVersion.isAtLeast(MinecraftVersion.v1_12_2)
        }
    }

}
