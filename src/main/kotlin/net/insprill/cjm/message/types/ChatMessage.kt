package net.insprill.cjm.message.types

import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.placeholder.Placeholders.Companion.fillPlaceholders
import net.insprill.xenlib.CenteredMessages
import net.insprill.xenlib.MinecraftVersion
import net.insprill.xenlib.files.YamlFile
import net.kyori.adventure.text.minimessage.MiniMessage
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
        formatMessages(primaryPlayer, messages)
        for (msg in messages) {
            for (player in players) {
                sendMessage(msg, player, visibility)
            }
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

    private fun sendMessage(msg: String, player: Player, visibility: MessageVisibility) {
        if (config.getBoolean("MiniMessage") && MinecraftVersion.isAtLeast(MinecraftVersion.v1_16_0)) {
            val component = MiniMessage.miniMessage().deserialize(msg)
            player.sendMessage(component)
            if (visibility != MessageVisibility.PRIVATE)
                Bukkit.getConsoleSender().sendMessage(component)
        } else {
            player.sendMessage(msg)
            if (visibility != MessageVisibility.PRIVATE)
                Bukkit.getConsoleSender().sendMessage(msg)
        }
    }

}
