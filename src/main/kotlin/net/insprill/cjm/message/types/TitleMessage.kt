package net.insprill.cjm.message.types

import de.themoep.minedown.MineDown
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.placeholder.Placeholders
import net.insprill.xenlib.files.YamlFile
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.entity.Player
import java.io.File

class TitleMessage : MessageType {

    override val config = YamlFile("messages" + File.separator + "title.yml").setModifiable(false)
    override val key = "Messages"
    override val name = "title"

    override fun handle(primaryPlayer: Player, players: List<Player>, rootPath: String?, chosenPath: String, visibility: MessageVisibility) {
        val title = BaseComponent.toLegacyText(
            *MineDown.parse(
                Placeholders.fillPlaceholders(primaryPlayer, config.getString("$chosenPath.Title", "")!!)
            )
        )
        val subTitle = BaseComponent.toLegacyText(
            *MineDown.parse(
                Placeholders.fillPlaceholders(primaryPlayer, config.getString("$chosenPath.SubTitle", "")!!)
            )
        )
        val fadeIn = config.getInt("$chosenPath.Fade-In")
        val stay = config.getInt("$chosenPath.Stay")
        val fadeOut = config.getInt("$chosenPath.Fade-Out")

        for (player in players) {
            @Suppress("DEPRECATION")
            player.sendTitle(title, subTitle, fadeIn, stay, fadeOut)
        }
    }

}
