package net.insprill.cjm.message.types

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.extension.replacePlaceholders
import net.insprill.cjm.message.MessageVisibility
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.entity.Player

class TitleMessage(private val plugin: CustomJoinMessages) : MessageType(plugin, "title", "Messages") {

    override fun handle(primaryPlayer: Player, recipients: List<Player>, chosenPath: String, visibility: MessageVisibility) {
        val titleStr = config.getString("$chosenPath.Title")?.replacePlaceholders(primaryPlayer) ?: ""
        val subTitleStr = config.getString("$chosenPath.SubTitle")?.replacePlaceholders(primaryPlayer) ?: ""
        if (titleStr.isBlank() && subTitleStr.isBlank()) return

        val title = BaseComponent.toLegacyText(*plugin.formatter.format(titleStr))
        val subTitle = BaseComponent.toLegacyText(*plugin.formatter.format(subTitleStr))
        val fadeIn = config.getInt("$chosenPath.Fade-In")
        val stay = config.getInt("$chosenPath.Stay")
        val fadeOut = config.getInt("$chosenPath.Fade-Out")

        for (player in recipients) {
            player.sendTitle(title, subTitle, fadeIn, stay, fadeOut)
        }
    }

}
