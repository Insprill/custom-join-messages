package net.insprill.cjm.placeholder

import de.themoep.minedown.MineDown
import me.clip.placeholderapi.PlaceholderAPI
import net.insprill.cjm.compatibility.Dependency
import net.md_5.bungee.api.chat.TextComponent
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
    ;

    companion object {

        fun fillPlaceholders(player: Player, msg: String): String {
            var newMessage = msg
            for (placeholder in values()) {
                newMessage = newMessage.replace("%${placeholder.stringName}%", placeholder.result.invoke(player))
            }
            if (Dependency.PAPI.isEnabled) {
                newMessage = TextComponent.toLegacyText(*MineDown.parse(PlaceholderAPI.setPlaceholders(player, msg)))
            }
            return newMessage
        }

        fun fillPlaceholders(player: Player, strings: MutableList<String>) {
            strings.replaceAll { fillPlaceholders(player, it) }
        }

    }

}
