package net.insprill.cjm.message

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.types.ChatMessage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MessageConditionTest {

    private lateinit var server: ServerMock
    private lateinit var plugin: CustomJoinMessages

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun maxPlayers_NotSet_NoPlayers_Passes() {
        val messageType = ChatMessage(plugin)

        val result = MessageCondition.MAX_PLAYERS.checkCondition(messageType, "Public.Join.69")

        assertTrue(result)
    }

    @Test
    fun maxPlayers_GreaterThanZero_NoPlayers_Passes() {
        val messageType = ChatMessage(plugin)
        messageType.config.set("Public.Join.69.Max-Players", 1)

        val result = MessageCondition.MAX_PLAYERS.checkCondition(messageType, "Public.Join.69")

        assertTrue(result)
    }

    @Test
    fun maxPlayers_GreaterThanZero_SamePlayers_Fails() {
        val messageType = ChatMessage(plugin)
        messageType.config.set("Public.Join.69.Max-Players", 1)
        server.addPlayer()

        val result = MessageCondition.MAX_PLAYERS.checkCondition(messageType, "Public.Join.69")

        assertFalse(result)
    }

    @Test
    fun maxPlayers_GreaterThanZero_MorePlayers_Fails() {
        val messageType = ChatMessage(plugin)
        messageType.config.set("Public.Join.69.Max-Players", 1)
        server.setPlayers(2)

        val result = MessageCondition.MAX_PLAYERS.checkCondition(messageType, "Public.Join.69")

        assertFalse(result)
    }

    @Test
    fun minPlayers_NotSet_NoPlayers_Passes() {
        val messageType = ChatMessage(plugin)

        val result = MessageCondition.MIN_PLAYERS.checkCondition(messageType, "Public.Join.69")

        assertTrue(result)
    }

    @Test
    fun minPlayers_GreaterThanZero_NoPlayers_Fails() {
        val messageType = ChatMessage(plugin)
        messageType.config.set("Public.Join.69.Min-Players", 1)

        val result = MessageCondition.MIN_PLAYERS.checkCondition(messageType, "Public.Join.69")

        assertFalse(result)
    }

    @Test
    fun minPlayers_GreaterThanZero_SamePlayers_Fails() {
        val messageType = ChatMessage(plugin)
        messageType.config.set("Public.Join.69.Min-Players", 1)
        server.addPlayer()

        val result = MessageCondition.MIN_PLAYERS.checkCondition(messageType, "Public.Join.69")

        assertFalse(result)
    }

    @Test
    fun minPlayers_GreaterThanZero_MorePlayers_Passes() {
        val messageType = ChatMessage(plugin)
        messageType.config.set("Public.Join.69.Min-Players", 1)
        server.setPlayers(2)

        val result = MessageCondition.MIN_PLAYERS.checkCondition(messageType, "Public.Join.69")

        assertTrue(result)
    }

    @Test
    fun checkAllConditions_AllMet_Passes() {
        val messageType = ChatMessage(plugin)
        server.setPlayers(2)
        messageType.config.set("Public.Join.69.Max-Players", 3)
        messageType.config.set("Public.Join.69.Min-Players", 1)

        val result = MessageCondition.checkAllConditions(messageType, "Public.Join.69")

        assertTrue(result)
    }

    @Test
    fun checkAllConditions_OneNotMet_Fails() {
        val messageType = ChatMessage(plugin)
        server.addPlayer()
        messageType.config.set("Public.Join.69.Max-Players", 3)
        messageType.config.set("Public.Join.69.Min-Players", 2)

        val result = MessageCondition.checkAllConditions(messageType, "Public.Join.69")

        assertFalse(result)
    }

}
