package net.insprill.cjm.toggle

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
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
    fun isToggled_NotSet_True(targetAction: MessageAction) {
        val player = server.addPlayer()

        assertTrue(toggleHandler.isToggled(player, targetAction))
        for (type in plugin.messageSender.typeMap.values) {
            assertTrue(toggleHandler.isToggled(player, null, type))
            assertTrue(toggleHandler.isToggled(player, targetAction, type))
        }
    }

    @ParameterizedTest
    @EnumSource(MessageAction::class)
    fun setToggled_Action_SetsToggled(targetAction: MessageAction) {
        val player = server.addPlayer()

        toggleHandler.setToggle(player, targetAction, null, false)

        for (action in MessageAction.entries) {
            assertEquals(action != targetAction, toggleHandler.isToggled(player, action))
            for (type in plugin.messageSender.typeMap.values) {
                assertEquals(action != targetAction, toggleHandler.isToggled(player, action, type))
            }
        }
    }

    @Test
    fun setToggled_Type_SetsToggled() {
        val player = server.addPlayer()
        val targetType = plugin.messageSender.typeMap["chat"]

        toggleHandler.setToggle(player, null, targetType, false)

        for (type in plugin.messageSender.typeMap.values) {
            assertEquals(type != targetType, toggleHandler.isToggled(player, null, type))
        }

        for (action in MessageAction.entries) {
            for (type in plugin.messageSender.typeMap.values) {
                assertEquals(type != targetType, toggleHandler.isToggled(player, action, type))
            }
        }
    }

    @ParameterizedTest
    @EnumSource(MessageAction::class)
    fun setToggled_Action_Type_SetsToggled(targetAction: MessageAction) {
        val player = server.addPlayer()
        val targetType = plugin.messageSender.typeMap["chat"]

        toggleHandler.setToggle(player, targetAction, targetType, false)

        for (action in MessageAction.entries) {
            assertEquals(action != targetAction, toggleHandler.isToggled(player, action, targetType))
            for (type in plugin.messageSender.typeMap.values) {
                assertEquals(action != targetAction || type != targetType, toggleHandler.isToggled(player, action, type))
            }
        }
    }

}
