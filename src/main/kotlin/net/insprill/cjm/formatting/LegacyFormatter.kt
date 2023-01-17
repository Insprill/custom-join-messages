package net.insprill.cjm.formatting

import net.insprill.spigotutils.MinecraftVersion
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import java.util.regex.Pattern

class LegacyFormatter : Formatter {

    val hexPattern = Pattern.compile("#[a-fA-F\\d]{6}")
    val formattedHexPattern = Pattern.compile("[?:{<&]?#[a-fA-F\\d]{6}[}>]?")

    override fun format(str: String): Array<BaseComponent> {
        var workingStr = str
        if (MinecraftVersion.isAtLeast(MinecraftVersion.v1_16_0)) {
            val formattedMatcher = formattedHexPattern.matcher(workingStr)
            while (formattedMatcher.find()) {
                val hex = formattedMatcher.group()
                val hexMatcher = hexPattern.matcher(hex)
                if (hexMatcher.find()) {
                    workingStr = workingStr.replace(formattedMatcher.group(), ChatColor.of(hexMatcher.group()).toString())
                    formattedMatcher.reset(workingStr)
                }
            }
        }
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', workingStr))
    }

}
