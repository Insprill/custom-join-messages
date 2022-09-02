package net.insprill.cjm.command

import co.aikar.commands.BukkitCommandManager
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.message.types.MessageType

class CommandCompletion(private val plugin: CustomJoinMessages) {

    fun register(manager: BukkitCommandManager): Unit = manager.commandCompletions.run {
        registerAsyncCompletion("messageType") {
            plugin.messageSender.typeMap.keys
        }
        registerAsyncCompletion("messageVisibility") {
            MessageVisibility.values().map { it.name.lowercase() }
        }
        registerAsyncCompletion("messageAction") {
            MessageAction.values().map { it.name.lowercase() }
        }
        registerAsyncCompletion("messageId") {
            val messageType = it.getContextValue(MessageType::class.java)
            val visibility = it.getContextValue(MessageVisibility::class.java)
            val action = it.getContextValue(MessageAction::class.java)
            messageType.config.getKeys("${visibility.configSection}.${action.configSection}")
        }
    }

}
