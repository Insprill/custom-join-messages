package net.insprill.cjm.message.type

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.message.types.ActionbarMessage
import org.junit.jupiter.api.AfterEach
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
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        actionbar = ActionbarMessage(plugin)
        player = server.addPlayer()
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun handle_SendsMessage() {
        actionbar.config.set("key.Message", "Hello!")

        actionbar.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertSaid("Hello!")
        player.assertNoMoreSaid()
    }

    @Test
    fun handle_NullMessage_DoesntSend() {
        actionbar.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertNoMoreSaid()
    }

    @Test
    fun handle_BlankMessage_DoesntSend() {
        actionbar.config.set("key.Message", " ")

        actionbar.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertNoMoreSaid()
    }

    @Test
    fun handle_FillsPlaceholders() {
        actionbar.config.set("key.Message", "Hello %name%!")

        actionbar.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertSaid("Hello ${player.name}!")
        player.assertNoMoreSaid()
    }

    @Test
    fun handle_InsertsColours() {
        actionbar.config.set("key.Message", "&7Hello!")

        actionbar.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        player.assertSaid("§7Hello!")
        player.assertNoMoreSaid()
    }

}
