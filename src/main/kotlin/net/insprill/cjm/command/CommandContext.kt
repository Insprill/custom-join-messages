package net.insprill.cjm.command

import co.aikar.commands.BukkitCommandManager
import co.aikar.commands.InvalidCommandArgument
import co.aikar.locales.MessageKey
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.types.MessageType

class CommandContext(private val plugin: CustomJoinMessages) {

    fun register(manager: BukkitCommandManager): Unit = manager.commandContexts.run {
        registerContext(MessageType::class.java) {
            val arg = it.popFirstArg().lowercase()
            plugin.messageSender.typeMap[arg]
                ?: throw InvalidCommandArgument(
                    MessageKey.of("cjm.command.error.invalid-message-type"), "%messageType%", arg
                )
        }
    }

}
