package net.insprill.cjm.toggle

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import net.insprill.cjm.message.types.ChatMessage
import org.bukkit.plugin.Plugin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.io.File
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock

class ToggleHandlerTest {

    private lateinit var toggleHandler: ToggleHandler
    private lateinit var plugin: CustomJoinMessages
    private lateinit var server: ServerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        toggleHandler = ToggleHandler(plugin)
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun config_IsCreated() {
        assertTrue(File("${plugin.dataFolder}/data/toggles.json").exists())
    }

    @ParameterizedTest
    @EnumSource(MessageAction::class)
    fun isToggled_NotSet_True(action: MessageAction) {
        val player = server.addPlayer()

        assertTrue(toggleHandler.isToggled(player, action))
    }

    @ParameterizedTest
    @EnumSource(MessageAction::class)
    fun setToggled_Action_SetsToggled(action: MessageAction) {
        val player = server.addPlayer()

        toggleHandler.setToggle(player, action, null, false)

        for (value in MessageAction.entries) {
            assertEquals(value != action, toggleHandler.isToggled(player, value))
        }
    }

    @ParameterizedTest
    @EnumSource(MessageAction::class)
    fun setToggled_Action_Type_SetsToggled(action: MessageAction) {
        val player = server.addPlayer()
        val type = ChatMessage(plugin)

        toggleHandler.setToggle(player, action, type, false)

        for (value in MessageAction.entries) {
            assertEquals(value != action, toggleHandler.isToggled(player, value, type))
        }
    }

}
