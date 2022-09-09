package net.insprill.cjm.message

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.AuthHook
import net.insprill.cjm.compatibility.hook.HookManager
import net.insprill.cjm.compatibility.hook.JailHook
import net.insprill.cjm.compatibility.hook.PluginHook
import net.insprill.cjm.compatibility.hook.VanishHook
import org.bukkit.entity.Player
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class MessageActionTest {

    private lateinit var plugin: CustomJoinMessages
    private lateinit var server: ServerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @ParameterizedTest
    @EnumSource(MessageAction::class)
    fun configSection_NotBlank(action: MessageAction) {
        assertFalse(action.configSection.isBlank())
    }

    @Test
    fun canRun_FirstJoin_True() {
        val player = server.addPlayer()
        assertTrue(MessageAction.FIRST_JOIN.canRun(plugin, player))
    }

    @Test
    fun canRun_Join_True() {
        val player = server.addPlayer()
        assertTrue(MessageAction.QUIT.canRun(plugin, player))
    }

    @Test
    fun canRun_Quit_True() {
        plugin.hookManager = HookManager(listOf(PluginHookMock()))
        val player = server.addPlayer()

        assertTrue(MessageAction.QUIT.canRun(plugin, player))
    }

    @Test
    fun canRun_Quit_PlayerDisconnected_False() {
        plugin.hookManager = HookManager(listOf(PluginHookMock()))
        val player = server.addPlayer()
        player.disconnect()

        assertFalse(MessageAction.QUIT.canRun(plugin, player))
    }

    private class PluginHookMock : PluginHook {

        override val authHook: AuthHook = AuthHookMock()
        override val vanishHook: VanishHook? = null
        override val jailHook: JailHook? = null

    }

    private class AuthHookMock : AuthHook {

        override fun isLoggedIn(player: Player): Boolean {
            return player.isOnline
        }

    }

}
