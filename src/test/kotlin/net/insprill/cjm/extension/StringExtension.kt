package net.insprill.cjm.extension

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StringExtension {

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
    fun replacePlaceholders_Placeholders_Fills() {
        @Suppress("DEPRECATION")
        player.displayName = "SprillJ"
        val string = "%displayname% (%name%) has joined! [#%uniquejoins%]"

        Assertions.assertEquals("SprillJ (Insprill) has joined! [#1]", string.replacePlaceholders(player))
    }

}
