package net.insprill.cjm.message

import net.insprill.cjm.message.types.MessageType
import org.bukkit.Bukkit

enum class MessageCondition(private val condition: (MessageType, String) -> Boolean) {
    MAX_PLAYERS({ msg, path ->
        val maxPlayers = msg.config.getOrDefault("$path.Max-Players", -1)
        maxPlayers < 1 || Bukkit.getOnlinePlayers().size > maxPlayers
    }),
    MIN_PLAYERS({ msg, path ->
        val minPlayers = msg.config.getOrDefault("$path.Min-Players", -1)
        minPlayers < 1 || Bukkit.getOnlinePlayers().size < minPlayers
    }),
    ;

    fun checkCondition(msg: MessageType, messagePath: String): Boolean {
        return condition.invoke(msg, messagePath)
    }

    companion object {
        fun checkAllConditions(msg: MessageType, messagePath: String): Boolean {
            return values().all { it.checkCondition(msg, messagePath) }
        }
    }

}
