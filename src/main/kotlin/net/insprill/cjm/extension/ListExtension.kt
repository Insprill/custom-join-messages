package net.insprill.cjm.extension

import net.insprill.cjm.CustomJoinMessages
import org.bukkit.entity.Player

fun List<String>.replacePlaceholders(plugin: CustomJoinMessages, player: Player): List<String> {
    return this.map { it.replacePlaceholders(plugin, player) }
}
