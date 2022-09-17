package net.insprill.cjm.message.types

import de.themoep.minedown.MineDown
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.placeholder.Placeholder
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.entity.Player

class TitleMessage(plugin: CustomJoinMessages) : MessageType(plugin, "title", "Messages") {

    override fun handle(primaryPlayer: Player, players: List<Player>, chosenPath: String, visibility: MessageVisibility) {
        val title = BaseComponent.toLegacyText(
            *MineDown.parse(
                Placeholder.fillPlaceholders(primaryPlayer, config.getString("$chosenPath.Title")!!)
            )
        )
        val subTitle = BaseComponent.toLegacyText(
            *MineDown.parse(
                Placeholder.fillPlaceholders(primaryPlayer, config.getString("$chosenPath.SubTitle")!!)
            )
        )
        val fadeIn = config.getInt("$chosenPath.Fade-In")
        val stay = config.getInt("$chosenPath.Stay")
        val fadeOut = config.getInt("$chosenPath.Fade-Out")

        for (player in players) {
            player.sendTitle(title, subTitle, fadeIn, stay, fadeOut)
        }
    }

}
