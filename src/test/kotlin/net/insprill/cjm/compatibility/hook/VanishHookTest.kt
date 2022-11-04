package net.insprill.cjm.compatibility.hook

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.test.MessageTypeMock
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VanishHookTest {

    private lateinit var server: ServerMock
    private lateinit var plugin: CustomJoinMessages
    private lateinit var player: PlayerMock
    private lateinit var messageTypeMock: MessageTypeMock
    private lateinit var vanishHookMock: VanishHookMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        player = server.addPlayer()
        vanishHookMock = VanishHookMock(plugin)
        messageTypeMock = MessageTypeMock(plugin)
        plugin.messageSender.registerType(messageTypeMock)
        // Wacky MockBukkit permissions go brr
        server.pluginManager.addPermission(Permission("cjm.default", PermissionDefault.TRUE))
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun handleToggle_FakeMessagesDisabled_NoMessageSent() {
        plugin.config.set("Addons.Vanish.Fake-Messages.Enabled", false)

        vanishHookMock.handleToggle(player, true)

        messageTypeMock.assertDoesntHaveResult()
    }

    @Test
    fun handleToggle_Vanishing_Disabled_NoMessageSent() {
        plugin.config.set("Addons.Vanish.Fake-Messages.Vanish", false)

        vanishHookMock.handleToggle(player, true)

        messageTypeMock.assertDoesntHaveResult()
    }

    @Test
    fun handleToggle_Unvanishing_Disabled_NoMessageSent() {
        plugin.config.set("Addons.Vanish.Fake-Messages.Unvanish", false)

        vanishHookMock.handleToggle(player, false)

        messageTypeMock.assertDoesntHaveResult()
    }

    @Test
    fun handleToggle_Vanishing_Enabled_MessageSent() {
        vanishHookMock.handleToggle(player, true)

        messageTypeMock.assertHasResult()
    }

    @Test
    fun handleToggle_Unvanishing_Enabled_MessageSent() {
        vanishHookMock.handleToggle(player, false)

        messageTypeMock.assertHasResult()
    }

    class VanishHookMock(override val plugin: CustomJoinMessages) : VanishHook {

        override fun isVanished(player: Player): Boolean {
            throw UnsupportedOperationException()
        }

    }

}
