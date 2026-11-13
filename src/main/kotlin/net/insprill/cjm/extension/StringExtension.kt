package net.insprill.cjm.extension

import me.clip.placeholderapi.PlaceholderAPI
import net.insprill.cjm.compatibility.Dependency
import net.insprill.cjm.placeholder.Placeholder
import org.bukkit.Bukkit
import org.bukkit.entity.Player

fun String.replacePlaceholders(player: Player): String {
    var newMessage = this

    repeat(MAX_PLACEHOLDER_NESTING) {
        val previousMessage = newMessage
        for (placeholder in Placeholder.entries) {
            newMessage = newMessage.replace("%${placeholder.stringName}%", placeholder.result.invoke(player))
        }
        if (Dependency.PAPI.isIntegrationActive) {
            newMessage = PlaceholderAPI.setPlaceholders(player, newMessage)
        }

        if (newMessage == previousMessage) return newMessage
    }

    if (Dependency.PAPI.isIntegrationActive && PlaceholderAPI.containsPlaceholders(newMessage)) {
        val logger = Bukkit.getPluginManager().getPlugin("CustomJoinMessages")?.logger ?: Bukkit.getLogger()
        logger.severe("Placeholder expansion exceeded the maximum nesting depth of $MAX_PLACEHOLDER_NESTING for player '${player.name}'. The partially expanded message will be used: $newMessage")
    }

    return newMessage
}

// Limited to avoid circular placeholder crashes
private const val MAX_PLACEHOLDER_NESTING = 10
