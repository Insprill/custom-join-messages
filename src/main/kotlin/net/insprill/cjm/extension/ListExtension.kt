package net.insprill.cjm.extension

import net.insprill.cjm.extension.StringExtension.replacePlaceholders
import org.bukkit.entity.Player

object ListExtension {

    fun MutableList<String>.replacePlaceholders(player: Player) = apply {
        this.replaceAll { it.replacePlaceholders(player) }
    }

}
