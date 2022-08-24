package net.insprill.cjm.placeholder

import me.clip.placeholderapi.PlaceholderAPI
import net.insprill.cjm.compatibility.Dependency
import net.insprill.xenlib.ColourUtils
import net.milkbowl.vault.chat.Chat
import org.bukkit.Bukkit
import org.bukkit.entity.Player

enum class Placeholders(private val placeholderName: String, private val result: (Player) -> String) {
    DISPLAY_NAME("displayname", { if (it.customName != null) it.customName!! else it.displayName }),
    NAME("name", { it.name }),
    PREFIX("prefix", { if (Dependency.VAULT.isEnabled) (Dependency.VAULT.clazz as Chat).getPlayerPrefix(it) ?: "" else "" }),
    SUFFIX("suffix", { if (Dependency.VAULT.isEnabled) (Dependency.VAULT.clazz as Chat).getPlayerSuffix(it) ?: "" else "" }),
    UNIQUE_JOINS("uniquejoins", { Bukkit.getOfflinePlayers().size.toString() }),
    ;

    companion object {

        fun fillPlaceholders(player: Player, msg: String): String {
            var newMessage = msg
            for (placeholder in values()) {
                newMessage = msg.replace("%$placeholder%", placeholder.result.invoke(player))
            }
            if (Dependency.PAPI.isEnabled) {
                newMessage = ColourUtils.format(PlaceholderAPI.setPlaceholders(player, msg))
            }
            return newMessage
        }

        fun fillPlaceholders(player: Player, strings: MutableList<String>) {
            strings.replaceAll { msg -> fillPlaceholders(player, msg) }
        }

    }

}
