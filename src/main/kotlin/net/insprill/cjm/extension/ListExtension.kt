package net.insprill.cjm.extension

import net.insprill.cjm.extension.StringExtension.replacePlaceholders
import org.bukkit.entity.Player

object ListExtension {

    fun List<String>.replacePlaceholders(player: Player): List<String> {
        return this.map { it.replacePlaceholders(player) }
    }

}
