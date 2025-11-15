package net.insprill.cjm.message.type

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.message.types.ChatMessage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock
import org.mockbukkit.mockbukkit.entity.PlayerMock

class ChatMessageTest {

    private lateinit var plugin: CustomJoinMessages
    private lateinit var server: ServerMock
    private lateinit var chat: ChatMessage
    private lateinit var player: PlayerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        player = server.addPlayer() // Add before we load the plugin
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        chat = ChatMessage(plugin)
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun handle_SingletonList_SendsMessage() {
        chat.config.set("key", listOf("Hello!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("Hello!", player.nextMessage())
        assertNull(player.nextMessage())
    }

    @Test
    fun handle_MultiList_SendsMessage() {
        chat.config.set("key", listOf("Hello!", "How're you?"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("Hello!", player.nextMessage())
        assertEquals("How're you?", player.nextMessage())
        assertNull(player.nextMessage())
    }

    @Test
    fun handle_NullMessageKey_DoesntSend() {
        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertNull(player.nextMessage())
    }

    @Test
    fun handle_EmptyMessage_Sends() {
        chat.config.set("key", listOf(""))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("", player.nextMessage())
    }

    @Test
    fun handle_BlankMessage_Sends() {
        chat.config.set("key", listOf(" "))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals(" ", player.nextMessage())
    }

    @Test
    fun handle_NullMessageAlone_Sends() {
        chat.config.set("key", listOf(null))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("", player.nextMessage())
    }

    @Test
    fun handle_NullMessageWithNonNull_Sends() {
        chat.config.set("key", listOf("howdy", null))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("howdy", player.nextMessage())
        assertEquals("", player.nextMessage())
    }

    @Test
    fun handle_Center_CentersMessage() {
        chat.config.set("key", listOf("center:Hello %name%!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        val message = player.nextMessage()
        assertNotNull(message)
        assertNotEquals(message, message?.trim())
        assertNull(player.nextMessage())
    }

    @Test
    fun handle_NoCenter_DoesntCenterMessage() {
        chat.config.set("key", listOf("Hello %name%!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        val message = player.nextMessage()
        assertNotNull(message)
        assertEquals(message, message?.trim())
        assertNull(player.nextMessage())
    }

    @Test
    fun handle_PublicMessage_SendsToConsole() {
        chat.config.set("key", listOf("Hello!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("§fHello!", server.consoleSender.nextMessage())
        assertNull(server.consoleSender.nextMessage())
    }

    @Test
    fun handle_PrivateMessage_DoesntSendToConsole() {
        chat.config.set("key", listOf("Hello!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PRIVATE)

        assertNull(server.consoleSender.nextMessage())
    }

    @Test
    fun handle_PublicMessage_ConsoleDisabled_DoesntSendToConsole() {
        chat.config.set("key", listOf("Hello!"))
        chat.config.set("Public.Send-To-Console", false)

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertNull(server.consoleSender.nextMessage())
    }

    @Test
    fun handle_FillsPlaceholders() {
        chat.config.set("key", listOf("Hello %name%!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("Hello ${player.name}!", player.nextMessage())
        assertNull(player.nextMessage())
    }

    @Test
    fun handle_InsertsColours() {
        chat.config.set("key", listOf("&7Hello!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("§7Hello!", player.nextMessage())
        assertNull(player.nextMessage())
    }

}
