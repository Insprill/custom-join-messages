package net.insprill.cjm.extension

import me.clip.placeholderapi.PlaceholderAPIPlugin
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock
import org.mockbukkit.mockbukkit.entity.PlayerMock

class StringExtensionTest {

    private lateinit var server: ServerMock
    private lateinit var player: PlayerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        player = server.addPlayer("Insprill")
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun replacePlaceholders_NoPlaceholders_SameString() {
        val player = server.addPlayer()
        val string = "A message that has no placeholders!"

        Assertions.assertEquals(string, string.replacePlaceholders(player))
    }

    @Test
    fun replacePlaceholders_WithPlaceholders_Fills() {
        @Suppress("DEPRECATION")
        player.displayName = "SprillJ"
        val string = "%displayname% (%name%) has joined!"

        Assertions.assertEquals("SprillJ (Insprill) has joined!", string.replacePlaceholders(player))
    }

    @Test
    @Disabled("MockBukkit requires a non-final main class")
    fun replacePlaceholders_WithPapiPlaceholders_Fills() {
        MockBukkit.load(PlaceholderAPIPlugin::class.java)
        PlaceholderExpansionMock().register()
        val string = "A wild %placeholder_mock% has joined!"

        Assertions.assertEquals("A wild mimus polyglottos has joined!", string.replacePlaceholders(player))
    }

    class PlaceholderExpansionMock : PlaceholderExpansion() {

        override fun getIdentifier(): String {
            return "mock"
        }

        override fun getAuthor(): String {
            return "Insprill"
        }

        override fun getVersion(): String {
            return "1.0.0"
        }

        override fun onRequest(player: OfflinePlayer?, params: String): String? {
            if (params == "placeholder_mock") {
                return "mimus polyglottos"
            }
            return null
        }

    }

}
