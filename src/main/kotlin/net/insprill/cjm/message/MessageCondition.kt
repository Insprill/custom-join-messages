package net.insprill.cjm.message

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.types.MessageType

enum class MessageCondition(private val condition: (CustomJoinMessages, MessageType, String) -> Boolean) {
    MAX_PLAYERS({ plugin, msg, path ->
        val maxPlayers = msg.config.getInt("$path.Max-Players")
        maxPlayers < 1 || plugin.server.onlinePlayers.size > maxPlayers
    }),
    MIN_PLAYERS({ plugin, msg, path ->
        val minPlayers = msg.config.getInt("$path.Min-Players")
        minPlayers < 1 || plugin.server.onlinePlayers.size < minPlayers
    }),
    ;

    fun checkCondition(plugin: CustomJoinMessages, msg: MessageType, messagePath: String): Boolean {
        return condition.invoke(plugin, msg, messagePath)
    }

    companion object {
        fun checkAllConditions(plugin: CustomJoinMessages, msg: MessageType, messagePath: String): Boolean {
            return values().all { it.checkCondition(plugin, msg, messagePath) }
        }
    }

}
