package net.insprill.cjm.formatting

import net.md_5.bungee.api.chat.BaseComponent

interface Formatter {

    fun format(str: String): Array<BaseComponent>

}
