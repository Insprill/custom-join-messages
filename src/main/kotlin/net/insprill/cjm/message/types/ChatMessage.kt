package net.insprill.cjm.message.types

import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.placeholder.Placeholders.Companion.fillPlaceholders
import net.insprill.xenlib.CenteredMessages
import net.insprill.xenlib.files.YamlFile
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.File

class ChatMessage : MessageType {

    private val CENTER_PREFIX = "center:"

    override val config = YamlFile("messages" + File.separator + "chat.yml", false)
    override val key = "Messages"
    override val name = "chat"

    override fun handle(primaryPlayer: Player, players: List<Player>, rootPath: String?, chosenPath: String, visibility: MessageVisibility) {
        val messages = config.getStringList(chosenPath)
        fillPlaceholders(primaryPlayer, messages)
        messages.replaceAll {
            if (it.startsWith(CENTER_PREFIX)) {
                CenteredMessages.centerMessage(it.substring(CENTER_PREFIX.length))
            } else {
                it
            }
        }
        for (msg in messages) {
            val isJson = msg.contains("{\"text\":")
            for (player in players) {
                if (isJson) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:tellraw " + player.name + " " + msg)
                } else {
                    player.sendMessage(msg)
                }
            }
            if (visibility === MessageVisibility.PUBLIC && !isJson) {
                Bukkit.getConsoleSender().sendMessage(msg)
            }
        }
    }

}
