package net.insprill.cjm.placeholder

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.kyori.adventure.text.Component
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

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun stringName_NoDuplicates() {
        val duplicates = Placeholder.entries
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

        assertNotNull(placeholder.result.invoke(player))
    }

    @Test
    fun displayName_NoCustomName_ReturnsDisplayName() {
        val player = server.addPlayer()
        player.displayName(Component.text("Insprill"))

        assertEquals("Insprill", Placeholder.DISPLAY_NAME.result.invoke(player))
    }

    @Test
    fun displayName_CustomName_ReturnsCustomName() {
        val player = server.addPlayer()
        player.customName(Component.text("Insprill"))

        assertEquals("Insprill", Placeholder.DISPLAY_NAME.result.invoke(player))
    }

    @Test
    fun name_HasDisplayName_ReturnsName() {
        val player = server.addPlayer("player")
        player.displayName(Component.text("Insprill"))

        assertEquals("player", Placeholder.NAME.result.invoke(player))
    }

    @Test
    fun name_HasCustomName_ReturnsName() {
        val player = server.addPlayer("player")
        player.customName(Component.text("Insprill"))

        assertEquals("player", Placeholder.NAME.result.invoke(player))
    }

    @Test
    fun prefix_NoVault_ReturnsEmptyString() {
        val player = server.addPlayer()

        assertEquals("", Placeholder.PREFIX.result.invoke(player))
    }

    @Test
    fun suffix_NoVault_ReturnsEmptyString() {
        val player = server.addPlayer()

        assertEquals("", Placeholder.SUFFIX.result.invoke(player))
    }

    @Test
    fun uniqueJoins_NewServer_ReturnsZero() {
        val player = PlayerMock(server, "player")

        assertEquals("0", Placeholder.UNIQUE_JOINS.result.invoke(player))
    }

    @Test
    fun uniqueJoins_OnePlayerOnline_ReturnsOne() {
        val player = server.addPlayer()

        assertEquals("1", Placeholder.UNIQUE_JOINS.result.invoke(player))
    }

    @Test
    fun uniqueJoins_MultiplePlayersOnline_ReturnsOne() {
        server.setPlayers(6)
        val player1 = server.playerList.onlinePlayers.first()

        assertEquals("6", Placeholder.UNIQUE_JOINS.result.invoke(player1))
    }

    @Test
    fun uniqueJoins_OnePlayer_NotOnline_ReturnsOne() {
        val player = server.addPlayer()
        player.disconnect()

        assertEquals("1", Placeholder.UNIQUE_JOINS.result.invoke(player))
    }

    @Test
    fun uuid_ReturnsCorrectUuid() {
        val player = server.addPlayer()

        assertEquals(player.uniqueId.toString(), Placeholder.UUID.result.invoke(player))
    }

}
