package net.insprill.cjm.message.type

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.message.types.SoundMessage
import org.bukkit.Sound
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SoundMessageTest {

    private lateinit var plugin: CustomJoinMessages
    private lateinit var server: ServerMock
    private lateinit var sound: SoundMessage
    private lateinit var player: PlayerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        sound = SoundMessage(plugin)
        player = server.addPlayer()
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun handle_SendsMessage() {
        sound.config.set("key.Sound", Sound.AMBIENT_CAVE.name)

        sound.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertEquals(1, player.heardSounds.size)
        player.assertSoundHeard(Sound.AMBIENT_CAVE)
    }

    @Test
    fun handle_Global_SendsMessage_CorrectLocation() {
        sound.config.set("key.Sound", Sound.AMBIENT_CAVE.name)
        sound.config.set("key.Global", true)
        val player2 = server.addPlayer()
        player2.teleport(player2.location.add(10.0, 0.0, 0.0))

        sound.handle(player, listOf(player, player2), "key", MessageVisibility.PUBLIC)

        assertEquals(1, player.heardSounds.size)
        assertEquals(1, player2.heardSounds.size)
        player.assertSoundHeard(Sound.AMBIENT_CAVE) { e -> e.location == player.location }
        player2.assertSoundHeard(Sound.AMBIENT_CAVE) { e -> e.location == player2.location }
    }

    @Test
    fun handle_NonGlobal_SendsMessage_CorrectLocation() {
        sound.config.set("key.Sound", Sound.AMBIENT_CAVE.name)
        sound.config.set("key.Global", false)
        val player2 = server.addPlayer()
        player2.teleport(player2.location.add(10.0, 0.0, 0.0))

        sound.handle(player, listOf(player, player2), "key", MessageVisibility.PUBLIC)

        assertEquals(1, player.heardSounds.size)
        assertEquals(1, player2.heardSounds.size)
        player.assertSoundHeard(Sound.AMBIENT_CAVE) { e -> e.location == player.location }
        player2.assertSoundHeard(Sound.AMBIENT_CAVE) { e -> e.location == player.location }
    }

    @Test
    fun handle_NullMessage_DoesntSend() {
        sound.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertTrue(player.heardSounds.isEmpty())
    }

    @Test
    fun handle_BlankMessage_DoesntSend() {
        sound.config.set("key.Sound", " ")

        sound.handle(player, listOf(player), "key", MessageVisibility.PUBLIC)

        assertTrue(player.heardSounds.isEmpty())
    }

}
