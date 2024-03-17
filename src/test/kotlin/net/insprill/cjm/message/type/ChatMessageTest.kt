package net.insprill.cjm.message.type

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.message.types.ChatMessage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ChatMessageTest {

    private lateinit var plugin: CustomJoinMessages
    private lateinit var server: ServerMock
    private lateinit var chat: ChatMessage
    private lateinit var player: PlayerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        chat = ChatMessage(plugin)
        player = server.addPlayer()
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun handle_SingletonList_SendsMessage() {
        chat.config.set("key", listOf("Hello!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertSaid("Hello!")
        player.assertNoMoreSaid()
    }

    @Test
    fun handle_MultiList_SendsMessage() {
        chat.config.set("key", listOf("Hello!", "How're you?"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertSaid("Hello!")
        player.assertSaid("How're you?")
        player.assertNoMoreSaid()
    }

    @Test
    fun handle_NullMessageKey_DoesntSend() {
        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertNoMoreSaid()
    }

    @Test
    fun handle_EmptyMessage_Sends() {
        chat.config.set("key", listOf(""))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertSaid("")
    }

    @Test
    fun handle_BlankMessage_Sends() {
        chat.config.set("key", listOf(" "))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertSaid(" ")
    }

    @Test
    fun handle_NullMessageAlone_Sends() {
        chat.config.set("key", listOf(null))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertSaid("")
    }

    @Test
    fun handle_NullMessageWithNonNull_Sends() {
        chat.config.set("key", listOf("howdy", null))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertSaid("howdy")
        player.assertSaid("")
    }

    @Test
    fun handle_Center_CentersMessage() {
        chat.config.set("key", listOf("center:Hello %name%!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        val message = player.nextMessage()
        assertNotNull(message)
        assertNotEquals(message, message?.trim())
        player.assertNoMoreSaid()
    }

    @Test
    fun handle_NoCenter_DoesntCenterMessage() {
        chat.config.set("key", listOf("Hello %name%!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        val message = player.nextMessage()
        assertNotNull(message)
        assertEquals(message, message?.trim())
        player.assertNoMoreSaid()
    }

    @Test
    fun handle_PublicMessage_SendsToConsole() {
        chat.config.set("key", listOf("Hello!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        server.consoleSender.assertSaid("§fHello!")
        server.consoleSender.assertNoMoreSaid()
    }

    @Test
    fun handle_PrivateMessage_DoesntSendToConsole() {
        chat.config.set("key", listOf("Hello!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PRIVATE)

        server.consoleSender.assertNoMoreSaid()
    }

    @Test
    fun handle_PublicMessage_ConsoleDisabled_DoesntSendToConsole() {
        chat.config.set("key", listOf("Hello!"))
        chat.config.set("Public.Send-To-Console", false)

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        server.consoleSender.assertNoMoreSaid()
    }

    @Test
    fun handle_FillsPlaceholders() {
        chat.config.set("key", listOf("Hello %name%!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertSaid("Hello ${player.name}!")
        player.assertNoMoreSaid()
    }

    @Test
    fun handle_InsertsColours() {
        chat.config.set("key", listOf("&7Hello!"))

        chat.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertSaid("§7Hello!")
        player.assertNoMoreSaid()
    }

}
