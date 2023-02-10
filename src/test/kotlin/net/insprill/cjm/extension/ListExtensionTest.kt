package net.insprill.cjm.extension

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ListExtensionTest {

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
    fun replacePlaceholders_SingleList_Placeholders_Fills() {
        @Suppress("DEPRECATION")
        player.displayName = "SprillJ"
        val strings = listOf("%displayname% (%name%) has joined! [#%uniquejoins%]")

        val newStrings = strings.replacePlaceholders(player)

        Assertions.assertEquals(1, newStrings.size)
        Assertions.assertEquals("SprillJ (Insprill) has joined! [#1]", newStrings[0])
    }

    @Test
    fun replacePlaceholders_MultiList_Placeholders_Fills() {
        @Suppress("DEPRECATION")
        player.displayName = "SprillJ"
        val strings = listOf(
            "%displayname% (%name%) has joined! [#%uniquejoins%]",
            "%displayname% (%name%) has joined! [%uuid%]"
        )

        val newStrings = strings.replacePlaceholders(player)

        Assertions.assertEquals(2, newStrings.size)
        Assertions.assertEquals("SprillJ (Insprill) has joined! [#1]", newStrings[0])
        Assertions.assertEquals("SprillJ (Insprill) has joined! [${player.uniqueId}]", newStrings[1])
    }

}
