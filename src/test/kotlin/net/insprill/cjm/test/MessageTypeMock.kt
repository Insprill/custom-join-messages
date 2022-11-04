package net.insprill.cjm.test

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.message.types.MessageType
import org.bukkit.entity.Player
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class MessageTypeMock(plugin: CustomJoinMessages) : MessageType(plugin, "mock", "Messages") {

    val results: MutableList<Result> = ArrayList()

    val result
        get() = run {
            assertTrue(results.size == 1)
            results.first()
        }

    fun assertHasResult() {
        assertFalse(results.isEmpty(), "Expected results to contain at least one element!")
    }

    fun assertDoesntHaveResult() {
        assertTrue(results.isEmpty(), "Expected results to be empty!")
    }

    fun clearResults() {
        results.clear()
    }

    override fun handle(primaryPlayer: Player, recipients: List<Player>, chosenPath: String, visibility: MessageVisibility) {
        results.add(Result(recipients, chosenPath))
    }

    data class Result(val recipients: List<Player>, val chosenPath: String)

}

