package net.insprill.cjm.message.types

import de.themoep.minedown.MineDown
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.placeholder.Placeholders.Companion.fillPlaceholders
import net.insprill.xenlib.CenteredMessages
import net.insprill.xenlib.files.YamlFile
import net.md_5.bungee.api.ChatMessageType
import org.bukkit.entity.Player
import java.io.File

class ChatMessage : MessageType {

    private val CENTER_PREFIX = "center:"

    override val config = YamlFile("messages" + File.separator + "chat.yml", false)
    override val key = "Messages"
    override val name = "chat"

    override fun handle(primaryPlayer: Player, players: List<Player>, rootPath: String?, chosenPath: String, visibility: MessageVisibility) {
        val messages = config.getStringList(chosenPath)
        formatMessages(primaryPlayer, messages)
        val components = messages.flatMap { MineDown.parse(it).toList() }.toTypedArray()
        for (player in players) {
            @Suppress("DEPRECATION")
            player.spigot().sendMessage(ChatMessageType.CHAT, *components)
        }
    }

    private fun formatMessages(primaryPlayer: Player, messages: MutableList<String>) {
        fillPlaceholders(primaryPlayer, messages)
        messages.replaceAll {
            if (it.startsWith(CENTER_PREFIX)) {
                CenteredMessages.centerMessage(it.substring(CENTER_PREFIX.length))
            } else {
                it
            }
        }
    }

}
