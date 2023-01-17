package net.insprill.cjm.formatting

import de.themoep.minedown.MineDown
import net.md_5.bungee.api.chat.BaseComponent

class MinedownFormatter : Formatter {

    override fun format(str: String): Array<BaseComponent> {
        return MineDown.parse(str)
    }

}
