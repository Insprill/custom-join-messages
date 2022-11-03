package net.insprill.cjm.extension

import org.bukkit.entity.Player

fun List<String>.replacePlaceholders(player: Player): List<String> {
    return this.map { it.replacePlaceholders(player) }
}
