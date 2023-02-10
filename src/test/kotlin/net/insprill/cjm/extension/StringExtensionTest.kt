package net.insprill.cjm.extension

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import me.clip.placeholderapi.PlaceholderAPIPlugin
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

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
        val string = "%displayname% (%name%) has joined! [#%uniquejoins%]"

        Assertions.assertEquals("SprillJ (Insprill) has joined! [#1]", string.replacePlaceholders(player))
    }

    @Test
    @Disabled("MockBukkit requires a special constructor")
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
