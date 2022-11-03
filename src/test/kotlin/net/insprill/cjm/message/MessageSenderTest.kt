package net.insprill.cjm.message

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import de.leonhard.storage.SimplixBuilder
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.types.MessageType
import net.insprill.cjm.test.MessageSentException
import net.insprill.cjm.test.MessageTypeMock
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.nio.file.Files

class MessageSenderTest {

    private lateinit var server: ServerMock
    private lateinit var plugin: CustomJoinMessages
    private lateinit var messageSender: MessageSender
    private lateinit var messageTypeMock: MessageType
    private lateinit var player: PlayerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        messageSender = plugin.messageSender
        messageTypeMock = MessageTypeMock(plugin)
        messageSender.registerType(messageTypeMock)
        player = server.addPlayer()
        // Wacky MockBukkit permissions go brr
        server.pluginManager.addPermission(Permission("cjm.default", PermissionDefault.TRUE))
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun reloadPermissions_RegistersAllPermissions() {
        val file = Files.createTempFile("config", ".yml").toFile()
        val config = SimplixBuilder.fromFile(file).createYaml()
        for (action in MessageAction.values()) {
            for (visibility in MessageVisibility.values()) {
                val path = visibility.configSection + "." + action.configSection
                for (i in 1..10) {
                    config.set("$path.$i.Permission", "cjm.$action.$visibility.$i")
                }
            }
        }

        messageSender.reloadPermissions(config)

        for (action in MessageAction.values()) {
            for (visibility in MessageVisibility.values()) {
                for (i in 1..10) {
                    assertNotNull(server.pluginManager.getPermission("cjm.$action.$visibility.$i"))
                }
            }
        }
    }

    @Test
    fun reloadPermissions_UnregistersPermissions() {
        val file = Files.createTempFile("config", ".yml").toFile()
        val config = SimplixBuilder.fromFile(file).createYaml()
        for (action in MessageAction.values()) {
            for (visibility in MessageVisibility.values()) {
                val path = visibility.configSection + "." + action.configSection
                for (i in 1..10) {
                    config.set("$path.$i.Permission", "cjm.$action.$visibility.$i")
                }
            }
        }

        messageSender.reloadPermissions(config)
        config.clear()
        messageSender.reloadPermissions(config)

        for (action in MessageAction.values()) {
            for (visibility in MessageVisibility.values()) {
                for (i in 1..10) {
                    assertNull(server.pluginManager.getPermission("cjm.$action.$visibility.$i"))
                }
            }
        }
    }

    @Test
    fun trySendMessages_ToggledOn_MessageSent() {
        assertThrows<MessageSentException> {
            messageSender.trySendMessages(player, MessageAction.JOIN, false)
        }
    }

    @Test
    fun trySendMessages_ToggledOff_NoMessageSent() {
        plugin.toggleHandler.setToggle(player, MessageAction.JOIN, false)
        assertDoesNotThrow {
            messageSender.trySendMessages(player, MessageAction.JOIN, false)
        }
    }

    @Test
    fun trySendMessages_NoVanishCheck_Vanished_MessageSent() {
        player.setMetadata("vanished", FixedMetadataValue(plugin, true))
        assertThrows<MessageSentException> {
            messageSender.trySendMessages(player, MessageAction.JOIN, false)
        }
    }

    @Test
    fun trySendMessages_VanishCheck_Vanished_NoMessageSent() {
        player.setMetadata("vanished", FixedMetadataValue(plugin, true))
        assertDoesNotThrow {
            messageSender.trySendMessages(player, MessageAction.JOIN, true)
        }
    }

    @Test
    fun trySendMessages_MessageTypeDisabled_NoMessageSent() {
        messageTypeMock.config.set("Enabled", false)
        assertDoesNotThrow {
            messageSender.trySendMessages(player, MessageAction.JOIN, true)
        }
    }

    @Test
    fun trySendMessages_MultiPermMessages_HasOne_PicksOne() {
        messageTypeMock.config.set("Public.Join.2.Messages.1.Message", "Yup")
        messageTypeMock.config.set("Public.Join.2.Permission", "cjm.2")

        val exc = assertThrows<MessageSentException> {
            messageSender.trySendMessages(player, MessageAction.JOIN, true)
        }

        assertEquals("Public.Join.1.Messages.1", exc.chosenPath)
    }

    @Test
    fun trySendMessages_MultiPermMessages_HasBoth_PicksHighest() {
        messageTypeMock.config.set("Public.Join.2.Messages.1.Message", "Yup")
        messageTypeMock.config.set("Public.Join.2.Permission", "cjm.2")
        player.addAttachment(plugin, "cjm.2", true)

        val exc = assertThrows<MessageSentException> {
            messageSender.trySendMessages(player, MessageAction.JOIN, true)
        }

        assertEquals("Public.Join.2.Messages.1", exc.chosenPath)
    }

    @Test
    fun trySendMessages_HasNoPerms_NoMessageSent() {
        player.addAttachment(plugin, "cjm.default", false)

        assertDoesNotThrow {
            messageSender.trySendMessages(player, MessageAction.JOIN, true)
        }
    }

    @Test
    fun trySendMessages_ChecksMessageConditions() {
        messageTypeMock.config.set("Public.Join.2.Min-Players", 2)

        assertDoesNotThrow {
            messageSender.trySendMessages(player, MessageAction.JOIN, true)
        }
    }

    @Test
    fun trySendMessages_NoRadius_SendsToEveryone() {
        val player2 = server.addPlayer()
        player2.location = player.location.add(29_999_984.0, 0.0, 29_999_984.0)

        val exc = assertThrows<MessageSentException> {
            messageSender.trySendMessages(player, MessageAction.JOIN, true)
        }

        assertTrue(exc.recipients.contains(player2))
    }

    @Test
    fun trySendMessages_Radius_PlayerOutsideRadius_DoesntReceiveMessage() {
        messageTypeMock.config.set("Public.Join.1.Radius", 10.0)
        val player2 = server.addPlayer()
        player2.location = player.location.add(10.5, 0.0, 0.0)

        val exc = assertThrows<MessageSentException> {
            messageSender.trySendMessages(player, MessageAction.JOIN, true)
        }

        assertFalse(exc.recipients.contains(player2))
    }

    @Test
    fun trySendMessages_Radius_PlayerInsideRadius_ReceivesMessage() {
        messageTypeMock.config.set("Public.Join.1.Radius", 10.0)
        val player2 = server.addPlayer()
        player2.location = player.location.add(9.5, 0.0, 0.0)

        val exc = assertThrows<MessageSentException> {
            messageSender.trySendMessages(player, MessageAction.JOIN, true)
        }

        assertTrue(exc.recipients.contains(player2))
    }

    @Test
    fun trySendMessages_Delay_LessThanOne_SendsImmediately() {
        messageTypeMock.config.set("Public.Join.1.Delay", 0)

        assertThrows<MessageSentException> {
            messageSender.trySendMessages(player, MessageAction.JOIN, true)
        }
    }

    @Test
    fun trySendMessages_Delay_SendsAfterDelay() {
        messageTypeMock.config.set("Public.Join.1.Delay", 5)

        assertDoesNotThrow {
            messageSender.trySendMessages(player, MessageAction.JOIN, true)
        }

        server.scheduler.performTicks(4)

        assertThrows<MessageSentException> {
            server.scheduler.performTicks(1)
        }
    }

    @Test
    fun getRandomKey_NullSection_ReturnsNull() {
        val file = Files.createTempFile("config", ".yml").toFile()
        val config = SimplixBuilder.fromFile(file).createYaml()

        assertNull(messageSender.getRandomKey(config, "key"))
    }

    @Test
    fun getRandomKey() {
        val file = Files.createTempFile("config", ".yml").toFile()
        val config = SimplixBuilder.fromFile(file).createYaml()
        config.set("key.1", true)
        config.set("key.2", true)
        config.set("key.3", true)

        assertNotNull(messageSender.getRandomKey(config, "key"))
    }

}
