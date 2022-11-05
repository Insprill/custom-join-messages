package net.insprill.cjm.command

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
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
    fun updateLocale_ValidLocale() {
        plugin.config.set("language", "fr")
        val command = plugin.commandManager.getRootCommand("cjm").defCommand as CjmCommand

        command.updateLocale()

        assertEquals("fr", plugin.commandManager.locales.defaultLocale.language)
    }

    @Test
    fun updateLocale_InvalidLocale_Resets() {
        plugin.config.set("language", "french-toast")
        val command = plugin.commandManager.getRootCommand("cjm").defCommand as CjmCommand

        command.updateLocale()

        assertEquals("en", plugin.commandManager.locales.defaultLocale.language)
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

        assertTrue(player.nextMessage()!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Preview_OneArg_Fails() {
        player.addAttachment(plugin, "cjm.command.preview", true)

        player.performCommand("cjm preview ${player.name}")

        assertTrue(player.nextMessage()!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Preview_TwoArgs_Fails() {
        player.addAttachment(plugin, "cjm.command.preview", true)

        player.performCommand("cjm preview ${player.name} chat")

        assertTrue(player.nextMessage()!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Preview_ThreeArgs_Fails() {
        player.addAttachment(plugin, "cjm.command.preview", true)

        player.performCommand("cjm preview ${player.name} chat public")

        assertTrue(player.nextMessage()!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Preview_FourArgs_Fails() {
        player.addAttachment(plugin, "cjm.command.preview", true)

        player.performCommand("cjm preview ${player.name} chat public join")

        assertTrue(player.nextMessage()!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Preview() {
        player.addAttachment(plugin, "cjm.command.preview", true)

        player.performCommand("cjm preview ${player.name} chat public join 1")

        assertTrue(player.nextMessage()!!.contains("joined", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Preview_InvalidTarget_Fails() {
        player.addAttachment(plugin, "cjm.command.preview", true)

        player.performCommand("cjm preview thisNameIsTooLongToBeValid chat public join 1")

        assertTrue(player.nextMessage()!!.contains("is not a valid username", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Preview_OfflineTarget_Fails() {
        player.addAttachment(plugin, "cjm.command.preview", true)

        player.performCommand("cjm preview WRONG chat public join 1")

        assertTrue(player.nextMessage()!!.contains("No player matching", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Preview_WrongMessageType_Fails() {
        player.addAttachment(plugin, "cjm.command.preview", true)

        player.performCommand("cjm preview ${player.name} WRONG public join 1")

        assertTrue(player.nextMessage()!!.contains("Unknown message type", true))
        assertTrue(player.nextMessage()!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Preview_WrongVisibility_Fails() {
        player.addAttachment(plugin, "cjm.command.preview", true)

        player.performCommand("cjm preview ${player.name} chat WRONG join 1")

        assertTrue(player.nextMessage()!!.contains("Please specify one of", true))
        assertTrue(player.nextMessage()!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Preview_WrongAction_Fails() {
        player.addAttachment(plugin, "cjm.command.preview", true)

        player.performCommand("cjm preview ${player.name} chat public WRONG 1")

        assertTrue(player.nextMessage()!!.contains("Please specify one of", true))
        assertTrue(player.nextMessage()!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Preview_WrongId_Fails() {
        player.addAttachment(plugin, "cjm.command.preview", true)

        player.performCommand("cjm preview ${player.name} chat public join 69")

        assertTrue(player.nextMessage()!!.contains("A message with ID", true))
        assertTrue(player.nextMessage()!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Toggle_FromConsole_NoTarget_Fails() {
        server.dispatchCommand(server.consoleSender, "cjm toggle join off")

        assertTrue(server.consoleSender.nextMessage()!!.contains("You must specify a target when running the command from console", true))
        assertTrue(server.consoleSender.nextMessage()!!.contains("usage", true))
        server.consoleSender.assertNoMoreSaid()
    }

    @Test
    fun execute_Toggle_NoPermission_DoesntExecute() {
        player.performCommand("cjm toggle")

        player.assertSaid("§cI'm sorry, but you do not have permission to perform this command.")
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Toggle_NoArgs_Fails() {
        player.addAttachment(plugin, "cjm.command.toggle", true)

        player.performCommand("cjm toggle")

        assertTrue(player.nextMessage()!!.contains("usage", true))
    }

    @Test
    fun execute_Toggle_Toggles() {
        player.addAttachment(plugin, "cjm.command.toggle", true)

        player.performCommand("cjm toggle join")

        assertTrue(player.nextMessage()!!.contains("Toggled join messages off!", true))
        player.assertNoMoreSaid()
        assertFalse(plugin.toggleHandler.isToggled(player, MessageAction.JOIN))

        player.performCommand("cjm toggle join")

        assertTrue(player.nextMessage()!!.contains("Toggled join messages on!", true))
        player.assertNoMoreSaid()
        assertTrue(plugin.toggleHandler.isToggled(player, MessageAction.JOIN))
    }

    @Test
    fun execute_Toggle_Off_TogglesOff() {
        player.addAttachment(plugin, "cjm.command.toggle", true)

        player.performCommand("cjm toggle join off")

        assertTrue(player.nextMessage()!!.contains("Toggled join messages off!", true))
        player.assertNoMoreSaid()
        assertFalse(plugin.toggleHandler.isToggled(player, MessageAction.JOIN))
    }

    @Test
    fun execute_Toggle_On_TogglesOn() {
        player.addAttachment(plugin, "cjm.command.toggle", true)

        player.performCommand("cjm toggle join on")

        assertTrue(player.nextMessage()!!.contains("Toggled join messages on!", true))
        player.assertNoMoreSaid()
        assertTrue(plugin.toggleHandler.isToggled(player, MessageAction.JOIN))
    }

    @Test
    fun execute_Toggle_OtherPlayer() {
        val player2 = server.addPlayer()

        player.addAttachment(plugin, "cjm.command.toggle", true)

        player.performCommand("cjm toggle join off ${player2.name}")

        assertTrue(player.nextMessage()!!.contains("Toggled join messages off!", true))
        player.assertNoMoreSaid()
        assertTrue(plugin.toggleHandler.isToggled(player, MessageAction.JOIN))
        assertFalse(plugin.toggleHandler.isToggled(player2, MessageAction.JOIN))
    }

    @Test
    fun execute_Toggle_WrongType_Fails() {
        player.addAttachment(plugin, "cjm.command.toggle", true)

        player.performCommand("cjm toggle WRONG off")

        assertTrue(player.nextMessage()!!.contains("Please specify one of", true))
        assertTrue(player.nextMessage()!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Toggle_InvalidTarget_Fails() {
        player.addAttachment(plugin, "cjm.command.toggle", true)

        player.performCommand("cjm toggle join off thisNameIsTooLongToBeValid")

        assertTrue(player.nextMessage()!!.contains("is not a valid username", true))
        assertTrue(player.nextMessage()!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Toggle_OfflineTarget_Fails() {
        player.addAttachment(plugin, "cjm.command.toggle", true)

        player.performCommand("cjm toggle join off WRONG")

        assertTrue(player.nextMessage()!!.contains("No player matching", true))
        assertTrue(player.nextMessage()!!.contains("usage", true))
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Reload_NoPermission_DoesntSendNotice() {
        player.performCommand("cjm reload")

        player.assertSaid("§cI'm sorry, but you do not have permission to perform this command.")
        player.assertNoMoreSaid()
    }

    @Test
    fun execute_Reload_SendsNotice() {
        player.addAttachment(plugin, "cjm.command.reload", true)

        player.performCommand("cjm reload")

        assertNotNull(player.nextMessage())
        player.assertNoMoreSaid()
    }

}
