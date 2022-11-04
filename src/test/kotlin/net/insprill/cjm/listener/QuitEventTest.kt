package net.insprill.cjm.listener

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.test.MessageTypeMock
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class QuitEventTest {

    private lateinit var server: ServerMock
    private lateinit var plugin: CustomJoinMessages
    private lateinit var messageTypeMock: MessageTypeMock
    private lateinit var player: PlayerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        player = server.addPlayer()
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
    fun onPlayerQuit_SendsQuitMessage() {
        player.disconnect()

        messageTypeMock.assertHasResult()
        Assertions.assertTrue(messageTypeMock.result.chosenPath.contains(".Quit.", true))
    }

    @Test
    fun onPlayerQuit_Vanished_NoMessageSent() {
        player.setMetadata("vanished", FixedMetadataValue(plugin, true))
        player.disconnect()

        messageTypeMock.assertDoesntHaveResult()
    }

    @Test
    fun onPlayerQuit_ClearsDefaultMessage() {
        player.disconnect()

        server.pluginManager.assertEventFired(PlayerQuitEvent::class.java) { it.quitMessage() == null }
    }

}
