package net.insprill.cjm.formatting

import net.insprill.spigotutils.MinecraftVersion
import net.insprill.spigotutils.ServerEnvironment
import net.md_5.bungee.api.chat.BaseComponent

class MineDownFormatter : Formatter {

    private val formatter = if (MiniMessageFormatter.isCompatible() && !ServerEnvironment.isMockBukkit()) MineDownFormatterModern() else MineDownFormatterLegacy()

    override fun format(str: String): Array<BaseComponent> {
        return formatter.format(str)
    }

    class MineDownFormatterLegacy : Formatter {
        override fun format(str: String): Array<BaseComponent> {
            return de.themoep.minedown.MineDown.parse(str)
        }
    }

    class MineDownFormatterModern : Formatter {
        override fun format(str: String): Array<BaseComponent> {
            return (FormatterType.MINIMESSAGE.formatter as MiniMessageFormatter).convertToLegacy(
                de.themoep.minedown.adventure.MineDown.parse(str)
            )
        }
    }

    companion object {
        fun isCompatible(): Boolean {
            return MinecraftVersion.isAtLeast(MinecraftVersion.v1_15_2)
        }
    }

}
