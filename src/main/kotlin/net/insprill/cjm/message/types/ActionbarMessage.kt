package net.insprill.cjm.message.types

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.extension.replacePlaceholders
import net.insprill.cjm.message.MessageVisibility
import net.md_5.bungee.api.ChatMessageType
import org.bukkit.entity.Player

class ActionbarMessage(private val plugin: CustomJoinMessages) : MessageType(plugin, "actionbar", "Messages") {

    override fun handle(primaryPlayer: Player, recipients: List<Player>, chosenPath: String, visibility: MessageVisibility) {
        val msg = config.getString("$chosenPath.Message")?.replacePlaceholders(plugin, primaryPlayer)
        if (msg.isNullOrBlank()) return
        val components = plugin.formatter.format(msg)
        for (player in recipients) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *components)
        }
    }

}
