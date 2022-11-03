package net.insprill.cjm.listener

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.WorldMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.test.MessageSentException
import net.insprill.cjm.test.MessageTypeMock
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class WorldChangeEventTest {

    private lateinit var server: ServerMock
    private lateinit var plugin: CustomJoinMessages
    private lateinit var player: PlayerMock
    private lateinit var world: WorldMock
    private lateinit var world1: WorldMock
    private lateinit var world2: WorldMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        plugin.messageSender.registerType(MessageTypeMock(plugin))
        plugin.config.set("World-Based-Messages.Enabled", true)
        player = server.addPlayer()
        world = server.addSimpleWorld("world")
        world1 = server.addSimpleWorld("world1")
        world2 = server.addSimpleWorld("world2")
        // Wacky MockBukkit permissions go brr
        server.pluginManager.addPermission(Permission("cjm.default", PermissionDefault.TRUE))
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun onTeleport_DifferentWorld_BothUngrouped_ModeNone() {
        plugin.config.set("World-Based-Messages.Ungrouped-Mode", "NONE")
        val event = PlayerTeleportEvent(player, loc(world1), loc(world2))

        assertDoesNotThrow {
            event.callEvent()
        }
    }

    @Test
    fun onTeleport_DifferentWorld_BothUngrouped_ModeSame() {
        plugin.config.set("World-Based-Messages.Ungrouped-Mode", "SAME")
        val event = PlayerTeleportEvent(player, loc(world1), loc(world2))

        assertDoesNotThrow {
            event.callEvent()
        }
    }

    @Test
    fun onTeleport_DifferentWorld_BothUngrouped_ModeIndividual() {
        plugin.config.set("World-Based-Messages.Ungrouped-Mode", "INDIVIDUAL")
        val event = PlayerTeleportEvent(player, loc(world1), loc(world2))

        assertThrows<MessageSentException> {
            event.callEvent()
        }
    }

    @Test
    fun onTeleport_DifferentWorld_OneUngrouped_ModeSame() {
        plugin.config.set("World-Based-Messages.Ungrouped-Mode", "SAME")
        val event = PlayerTeleportEvent(player, loc(world), loc(world2))

        assertThrows<MessageSentException> {
            event.callEvent()
        }
    }

    @Test
    fun onTeleport_DifferentWorld_OneUngrouped_ModeNone() {
        plugin.config.set("World-Based-Messages.Ungrouped-Mode", "NONE")
        val event = PlayerTeleportEvent(player, loc(world), loc(world2))

        assertDoesNotThrow {
            event.callEvent()
        }
    }

    private fun loc(world: World): Location {
        return Location(world, 0.0, 0.0, 0.0)
    }

}
