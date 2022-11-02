package net.insprill.cjm.placeholder

import net.insprill.cjm.compatibility.Dependency
import net.milkbowl.vault.chat.Chat
import org.bukkit.Bukkit
import org.bukkit.entity.Player

enum class Placeholder(internal val stringName: String, internal val result: (Player) -> String) {
    DISPLAY_NAME("displayname", { it.customName ?: it.displayName }),
    NAME("name", { it.name }),
    PREFIX("prefix", { if (Dependency.VAULT.isEnabled) (Dependency.VAULT.clazz as Chat).getPlayerPrefix(it) ?: "" else "" }),
    SUFFIX("suffix", { if (Dependency.VAULT.isEnabled) (Dependency.VAULT.clazz as Chat).getPlayerSuffix(it) ?: "" else "" }),
    UNIQUE_JOINS("uniquejoins", { Bukkit.getOfflinePlayers().size.toString() }),
    UUID("uuid", { it.uniqueId.toString() }),
}
