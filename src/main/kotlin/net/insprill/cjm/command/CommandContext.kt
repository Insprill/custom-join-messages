package net.insprill.cjm.command

import co.aikar.commands.BukkitCommandManager
import co.aikar.commands.InvalidCommandArgument
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.types.MessageType


class CommandContext(private val plugin: CustomJoinMessages) {

    fun register(manager: BukkitCommandManager) {
        manager.commandContexts.registerContext(MessageType::class.java) {
            it.popFirstArg()
            plugin.messageSender.typeMap[it.popFirstArg().lowercase()] ?: throw InvalidCommandArgument()
        }
    }

}
