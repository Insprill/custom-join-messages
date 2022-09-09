package net.insprill.cjm.toggle

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import net.insprill.cjm.message.MessageAction
import org.bukkit.plugin.Plugin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.io.File

class ToggleHandlerTest {

    private lateinit var toggleHandler: ToggleHandler
    private lateinit var plugin: Plugin
    private lateinit var server: ServerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.createMockPlugin()
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
    fun setToggled_SetsToggled(action: MessageAction) {
        val player = server.addPlayer()

        toggleHandler.setToggle(player, action, false)

        for (value in MessageAction.values()) {
            assertEquals(value != action, toggleHandler.isToggled(player, value))
        }
    }

}
