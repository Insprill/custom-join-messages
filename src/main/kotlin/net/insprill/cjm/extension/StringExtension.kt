package net.insprill.cjm.extension

import me.clip.placeholderapi.PlaceholderAPI
import net.insprill.cjm.compatibility.Dependency
import net.insprill.cjm.placeholder.Placeholder
import org.bukkit.entity.Player

fun String.replacePlaceholders(player: Player): String {
    var newMessage = this
    for (placeholder in Placeholder.values()) {
        newMessage = newMessage.replace("%${placeholder.stringName}%", placeholder.result.invoke(player))
    }
    if (Dependency.PAPI.isEnabled) {
        newMessage = PlaceholderAPI.setPlaceholders(player, newMessage)
    }
    return newMessage
}
