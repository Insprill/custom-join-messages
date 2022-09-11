package net.insprill.cjm.command

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.insprill.cjm.CustomJoinMessages
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CjmCommandTest {

    private lateinit var plugin: CustomJoinMessages
    private lateinit var server: ServerMock
    private lateinit var player: PlayerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        plugin.commandManager.setDefaultExceptionHandler { _, _, _, _, t ->
            fail("Exception occurred while executing command", t)
        }
        player = server.addPlayer()
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun execute_NoArgs_NoPermission_DoesntExecute() {
        player.performCommand("cjm")

        player.assertSaid("§cI'm sorry, but you do not have permission to perform this command.")
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_NoArgs_GetsHelpPage() {
        player.addAttachment(plugin, "cjm.command.help", true)

        player.performCommand("cjm")

        assertNotNull(player.nextMessage())
    }

    @Test
    fun execute_Help_NoPermission_DoesntExecute() {
        player.performCommand("cjm help")

        player.assertSaid("§cI'm sorry, but you do not have permission to perform this command.")
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Help_GetsHelpPage() {
        player.addAttachment(plugin, "cjm.command.help", true)

        player.performCommand("cjm help 2")

        assertNotNull(player.nextMessage())
    }

    @Test
    fun execute_Preview_NoPermission_DoesntExecute() {
        player.performCommand("cjm preview")

        player.assertSaid("§cI'm sorry, but you do not have permission to perform this command.")
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Preview_NoArgs_Fails() {
        player.addAttachment(plugin, "cjm.command.preview", true)

        player.performCommand("cjm preview")

        val response = player.nextMessage()
        assertNotNull(response)
        assertTrue(response!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Preview_OneArg_Fails() {
        player.addAttachment(plugin, "cjm.command.preview", true)

        player.performCommand("cjm preview ${player.name}")

        val response = player.nextMessage()
        assertNotNull(response)
        assertTrue(response!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

}
