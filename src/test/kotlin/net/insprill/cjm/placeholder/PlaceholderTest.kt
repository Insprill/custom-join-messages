package net.insprill.cjm.placeholder

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.insprill.cjm.CustomJoinMessages
import net.kyori.adventure.text.Component
import org.bukkit.event.player.PlayerTeleportEvent
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class PlaceholderTest {

    private lateinit var server: ServerMock
    private lateinit var plugin: CustomJoinMessages

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun stringName_NoDuplicates() {
        val duplicates = Placeholder.values()
            .map { it.stringName.lowercase() }
            .groupingBy { it }
            .eachCount()
            .filter { it.value > 1 }

        if (duplicates.isNotEmpty()) {
            fail { "Found duplicate Placeholders: $duplicates" }
        }
    }

    @ParameterizedTest
    @EnumSource(Placeholder::class)
    fun result_NotNull(placeholder: Placeholder) {
        val player = server.addPlayer()

        assertNotNull(placeholder.result.invoke(plugin, player))
    }

    @Test
    fun displayName_NoCustomName_ReturnsDisplayName() {
        val player = server.addPlayer()
        player.displayName(Component.text("Insprill"))

        assertEquals("Insprill", Placeholder.DISPLAY_NAME.result.invoke(plugin, player))
    }

    @Test
    fun displayName_CustomName_ReturnsCustomName() {
        val player = server.addPlayer()
        player.customName(Component.text("Insprill"))

        assertEquals("Insprill", Placeholder.DISPLAY_NAME.result.invoke(plugin, player))
    }

    @Test
    fun name_HasDisplayName_ReturnsName() {
        val player = server.addPlayer("player")
        player.displayName(Component.text("Insprill"))

        assertEquals("player", Placeholder.NAME.result.invoke(plugin, player))
    }

    @Test
    fun name_HasCustomName_ReturnsName() {
        val player = server.addPlayer("player")
        player.customName(Component.text("Insprill"))

        assertEquals("player", Placeholder.NAME.result.invoke(plugin, player))
    }

    @Test
    fun prefix_NoVault_ReturnsEmptyString() {
        val player = server.addPlayer()

        assertEquals("", Placeholder.PREFIX.result.invoke(plugin, player))
    }

    @Test
    fun suffix_NoVault_ReturnsEmptyString() {
        val player = server.addPlayer()

        assertEquals("", Placeholder.SUFFIX.result.invoke(plugin, player))
    }

    @Test
    fun uniqueJoins_NewServer_ReturnsZero() {
        val player = PlayerMock(server, "player")

        assertEquals("0", Placeholder.UNIQUE_JOINS.result.invoke(plugin, player))
    }

    @Test
    fun uniqueJoins_OnePlayerOnline_ReturnsOne() {
        val player = server.addPlayer()

        assertEquals("1", Placeholder.UNIQUE_JOINS.result.invoke(plugin, player))
    }

    @Test
    fun uniqueJoins_MultiplePlayersOnline_ReturnsOne() {
        server.setPlayers(6)
        val player1 = server.playerList.onlinePlayers.first()

        assertEquals("6", Placeholder.UNIQUE_JOINS.result.invoke(plugin, player1))
    }

    @Test
    fun uniqueJoins_OnePlayer_NotOnline_ReturnsOne() {
        val player = server.addPlayer()
        player.disconnect()

        assertEquals("1", Placeholder.UNIQUE_JOINS.result.invoke(plugin, player))
    }

    @Test
    fun uuid_ReturnsCorrectUuid() {
        val player = server.addPlayer()

        assertEquals(player.uniqueId.toString(), Placeholder.UUID.result.invoke(plugin, player))
    }

    @Test
    fun worldFrom_NeverTeleported_Unknown() {
        val player = server.addPlayer()

        assertEquals("Unknown", Placeholder.WORLD_FROM.result.invoke(plugin, player))
    }

    @Test
    fun worldFrom_Teleported_OldWorldName() {
        val player = server.addPlayer()
        val world1 = server.addSimpleWorld("world1")
        val world2 = server.addSimpleWorld("world2")
        plugin.config.set("World-Based-Messages.Enabled", true)

        PlayerTeleportEvent(player, world1.spawnLocation, world2.spawnLocation).callEvent()

        assertEquals("world1", Placeholder.WORLD_FROM.result.invoke(plugin, player))
    }

    @Test
    fun worldTo_NeverTeleported_Unknown() {
        val player = server.addPlayer()

        assertEquals("Unknown", Placeholder.WORLD_TO.result.invoke(plugin, player))
    }

    @Test
    fun worldTo_Teleported_NewWorldName() {
        val player = server.addPlayer()
        val world1 = server.addSimpleWorld("world1")
        val world2 = server.addSimpleWorld("world2")
        plugin.config.set("World-Based-Messages.Enabled", true)

        PlayerTeleportEvent(player, world1.spawnLocation, world2.spawnLocation).callEvent()

        assertEquals("world2", Placeholder.WORLD_TO.result.invoke(plugin, player))
    }

}
