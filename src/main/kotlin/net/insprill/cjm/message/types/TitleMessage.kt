package net.insprill.cjm.message.types

import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.placeholder.Placeholders
import net.insprill.xenlib.MinecraftVersion
import net.insprill.xenlib.files.YamlFile
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.entity.Player
import java.io.File
import java.time.Duration

class TitleMessage : MessageType {

    override val config = YamlFile("messages" + File.separator + "title.yml").setModifiable(false)
    override val key = "Messages"
    override val name = "title"

    override fun handle(primaryPlayer: Player, players: List<Player>, rootPath: String?, chosenPath: String, visibility: MessageVisibility) {
        val title = Placeholders.fillPlaceholders(primaryPlayer, config.getString("$chosenPath.Title", "")!!)
        val subTitle = Placeholders.fillPlaceholders(primaryPlayer, config.getString("$chosenPath.SubTitle", "")!!)
        val fadeIn = config.getInt("$chosenPath.Fade-In")
        val stay = config.getInt("$chosenPath.Stay")
        val fadeOut = config.getInt("$chosenPath.Fade-Out")

        for (p in players) {
            sendTitle(title, subTitle, fadeIn, stay, fadeOut, p)
        }
    }

    private fun sendTitle(title: String, subTitle: String, fadeIn: Int, stay: Int, fadeOut: Int, player: Player) {
        if (config.getBoolean("MiniMessage") && MinecraftVersion.isAtLeast(MinecraftVersion.v1_16_0)) {
            if (title.isNotEmpty()) {
                val titleComp = MiniMessage.miniMessage().deserialize(title)
                player.sendTitlePart(TitlePart.TITLE, titleComp)
            }
            if (subTitle.isNotEmpty()) {
                val subTitleComp = MiniMessage.miniMessage().deserialize(subTitle)
                player.sendTitlePart(TitlePart.SUBTITLE, subTitleComp)
            }
            val times = Title.Times.times(Duration.ofMillis(fadeIn * 50L), Duration.ofMillis(stay * 50L), Duration.ofMillis(fadeOut * 50L))
            player.sendTitlePart(TitlePart.TIMES, times)
        } else {
            @Suppress("DEPRECATION")
            player.sendTitle(title, subTitle, fadeIn, stay, fadeOut)
        }
    }

}
