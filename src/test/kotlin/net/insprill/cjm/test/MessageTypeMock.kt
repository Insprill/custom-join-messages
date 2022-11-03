package net.insprill.cjm.test

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.message.types.MessageType
import org.bukkit.entity.Player

class MessageTypeMock(plugin: CustomJoinMessages) : MessageType(plugin, "mock", "Messages") {
    override fun handle(primaryPlayer: Player, recipients: List<Player>, chosenPath: String, visibility: MessageVisibility) {
        throw MessageSentException(chosenPath, recipients)
    }
}

