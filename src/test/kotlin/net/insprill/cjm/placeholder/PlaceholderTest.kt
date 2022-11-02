package net.insprill.cjm.placeholder

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import net.insprill.cjm.extension.ListExtension.replacePlaceholders
import net.insprill.cjm.extension.StringExtension.replacePlaceholders
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

        assertNotNull(placeholder.result.invoke(player))
    }

    @Test
    fun fillPlaceholders_NoPlaceholders_SameString() {
        val player = server.addPlayer()
        val string = "A message that has no placeholders!"

        assertEquals(string, string.replacePlaceholders(player))
    }

    @Test
    fun fillPlaceholders_Placeholders_Fills() {
        val player = server.addPlayer("Insprill")
        @Suppress("DEPRECATION")
        player.displayName = "SprillJ"
        val string = "%displayname% (%name%) has joined! [#%uniquejoins%]"

        assertEquals("SprillJ (Insprill) has joined! [#1]", string.replacePlaceholders(player))
    }

    @Test
    fun fillPlaceholders_SingleList_Placeholders_Fills() {
        val player = server.addPlayer("Insprill")
        @Suppress("DEPRECATION")
        player.displayName = "SprillJ"
        val strings = mutableListOf("%displayname% (%name%) has joined! [#%uniquejoins%]")

        val newStrings = strings.replacePlaceholders(player)

        assertEquals(1, newStrings.size)
        assertEquals("SprillJ (Insprill) has joined! [#1]", newStrings[0])
    }

    @Test
    fun fillPlaceholders_MultiList_Placeholders_Fills() {
        val player = server.addPlayer("Insprill")
        @Suppress("DEPRECATION")
        player.displayName = "SprillJ"
        val strings = mutableListOf(
            "%displayname% (%name%) has joined! [#%uniquejoins%]",
            "%displayname% (%name%) has joined! [%uuid%]"
        )

        val newStrings = strings.replacePlaceholders(player)

        assertEquals(2, newStrings.size)
        assertEquals("SprillJ (Insprill) has joined! [#1]", newStrings[0])
        assertEquals("SprillJ (Insprill) has joined! [${player.uniqueId}]", newStrings[1])
    }

}
