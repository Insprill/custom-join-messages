package net.insprill.cjm.placeholder

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.Dependency
import net.milkbowl.vault.chat.Chat
import org.bukkit.Bukkit
import org.bukkit.entity.Player

enum class Placeholder(internal val stringName: String, internal val result: (CustomJoinMessages, Player) -> String) {
    DISPLAY_NAME("displayname", { _, player -> player.customName ?: player.displayName }),
    NAME("name", { _, player -> player.name }),
    PREFIX("prefix", { _, player -> if (Dependency.VAULT.isEnabled) (Dependency.VAULT.clazz as Chat).getPlayerPrefix(player) ?: "" else "" }),
    SUFFIX("suffix", { _, player -> if (Dependency.VAULT.isEnabled) (Dependency.VAULT.clazz as Chat).getPlayerSuffix(player) ?: "" else "" }),
    UNIQUE_JOINS("uniquejoins", { _, _ -> Bukkit.getOfflinePlayers().size.toString() }),
    UUID("uuid", { _, player -> player.uniqueId.toString() }),
    WORLD_FROM("world_from", { plugin, player -> plugin.worldChangeEvent.getWorlds(player.uniqueId)?.first ?: "Unknown" }),
    WORLD_TO("world_to", { plugin, player -> plugin.worldChangeEvent.getWorlds(player.uniqueId)?.second ?: "Unknown" }),
}
