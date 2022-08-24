package net.insprill.cjm.message.types

import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.placeholder.Placeholders
import net.insprill.xenlib.MinecraftVersion
import net.insprill.xenlib.files.YamlFile
import org.bukkit.entity.Player
import java.io.File

class TitleMessage : MessageType {

    override val config = YamlFile("messages" + File.separator + "title.yml", false)
    override val key = "Messages"
    override val name = "title"

    override fun handle(primaryPlayer: Player, players: List<Player>, rootPath: String?, chosenPath: String, visibility: MessageVisibility) {
        var title = config.getString("$chosenPath.Title")
        var subTitle = config.getString("$chosenPath.SubTitle")
        val fadeIn = config.getInt("$chosenPath.Fade-In")
        val stay = config.getInt("$chosenPath.Stay")
        val fadeOut = config.getInt("$chosenPath.Fade-Out")
        title = Placeholders.fillPlaceholders(primaryPlayer, title!!)
        subTitle = Placeholders.fillPlaceholders(primaryPlayer, subTitle!!)
        for (p in players) {
            sendTitle(p, title, subTitle, fadeIn, stay, fadeOut)
        }
    }

    private fun sendTitle(player: Player, title: String, subTitle: String, fadeIn: Int, stay: Int, fadeOut: Int) {
        if (MinecraftVersion.isAtLeast(MinecraftVersion.v1_9_0)) {
            player.sendTitle(title, subTitle, fadeIn, stay, fadeOut)
        } else {
            player.sendTitle(title, subTitle)
        }
    }

}
