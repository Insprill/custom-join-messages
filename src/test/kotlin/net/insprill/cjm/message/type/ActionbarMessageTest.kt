package net.insprill.cjm.message.type

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.message.types.ActionbarMessage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock
import org.mockbukkit.mockbukkit.entity.PlayerMock

class ActionbarMessageTest {

    private lateinit var plugin: CustomJoinMessages
    private lateinit var server: ServerMock
    private lateinit var actionbar: ActionbarMessage
    private lateinit var player: PlayerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        player = server.addPlayer() // Add before we load the plugin
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        actionbar = ActionbarMessage(plugin)
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun handle_SendsMessage() {
        actionbar.config.set("key.Message", "Hello!")

        actionbar.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("Hello!", player.nextMessage())
        assertNull(player.nextMessage())
    }

    @Test
    fun handle_NullMessage_DoesntSend() {
        actionbar.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertNull(player.nextMessage())
    }

    @Test
    fun handle_BlankMessage_DoesntSend() {
        actionbar.config.set("key.Message", " ")

        actionbar.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertNull(player.nextMessage())
    }

    @Test
    fun handle_FillsPlaceholders() {
        actionbar.config.set("key.Message", "Hello %name%!")

        actionbar.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("Hello ${player.name}!", player.nextMessage())
        assertNull(player.nextMessage())
    }

    @Test
    fun handle_InsertsColours() {
        actionbar.config.set("key.Message", "&7Hello!")

        actionbar.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("§7Hello!", player.nextMessage())
        assertNull(player.nextMessage())
    }

}
