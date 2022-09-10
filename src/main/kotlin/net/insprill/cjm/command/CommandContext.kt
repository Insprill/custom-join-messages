package net.insprill.cjm.command

import co.aikar.commands.BukkitCommandManager
import co.aikar.commands.InvalidCommandArgument
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.types.MessageType

class CommandContext(private val plugin: CustomJoinMessages) {

    fun register(manager: BukkitCommandManager): Unit = manager.commandContexts.run {
        registerContext(MessageType::class.java) {
            plugin.messageSender.typeMap[it.popFirstArg().lowercase()] ?: throw InvalidCommandArgument()
        }
    }

}
