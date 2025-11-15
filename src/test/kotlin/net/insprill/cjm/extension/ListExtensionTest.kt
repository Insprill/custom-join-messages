package net.insprill.cjm.extension

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockbukkit.mockbukkit.ServerMock
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.entity.PlayerMock

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
        val strings = listOf("%displayname% (%name%) has joined!")

        val newStrings = strings.replacePlaceholders(player)

        Assertions.assertEquals(1, newStrings.size)
        Assertions.assertEquals("SprillJ (Insprill) has joined!", newStrings[0])
    }

    @Test
    fun replacePlaceholders_MultiList_Placeholders_Fills() {
        @Suppress("DEPRECATION")
        player.displayName = "SprillJ"
        val strings = listOf(
            "%displayname% (%name%) has joined!",
            "%displayname% (%name%) has joined! [%uuid%]"
        )

        val newStrings = strings.replacePlaceholders(player)

        Assertions.assertEquals(2, newStrings.size)
        Assertions.assertEquals("SprillJ (Insprill) has joined!", newStrings[0])
        Assertions.assertEquals("SprillJ (Insprill) has joined! [${player.uniqueId}]", newStrings[1])
    }

}
