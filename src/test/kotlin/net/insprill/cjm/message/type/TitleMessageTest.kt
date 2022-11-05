package net.insprill.cjm.message.type

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.message.types.TitleMessage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TitleMessageTest {

    private lateinit var plugin: CustomJoinMessages
    private lateinit var server: ServerMock
    private lateinit var title: TitleMessage
    private lateinit var player: PlayerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        title = TitleMessage(plugin)
        player = server.addPlayer()
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun handle_SendsMessage() {
        title.config.set("key.Title", "Hello!")
        title.config.set("key.SubTitle", "Howdy!")

        title.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("§fHello!", player.nextTitle())
        assertEquals("§fHowdy!", player.nextSubTitle())
        assertNull(player.nextTitle())
        assertNull(player.nextSubTitle())
    }

    @Test
    fun handle_NullMessage_DoesntSend() {
        title.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertNull(player.nextTitle())
        assertNull(player.nextSubTitle())
    }

    @Test
    fun handle_BlankMessage_DoesntSend() {
        title.config.set("key.Title", " ")
        title.config.set("key.SubTitle", " ")

        title.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertNull(player.nextTitle())
        assertNull(player.nextSubTitle())
    }

    @Test
    fun handle_FillsPlaceholders() {
        title.config.set("key.Title", "Hello %name%!")
        title.config.set("key.SubTitle", "Howdy %name%!")

        title.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("§fHello ${player.name}!", player.nextTitle())
        assertEquals("§fHowdy ${player.name}!", player.nextSubTitle())
    }

    @Test
    fun handle_InsertsColours() {
        title.config.set("key.Title", "&7Hello!")
        title.config.set("key.SubTitle", "&7Howdy!")

        title.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals("§7Hello!", player.nextTitle())
        assertEquals("§7Howdy!", player.nextSubTitle())
    }

}
