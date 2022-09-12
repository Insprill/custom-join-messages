package net.insprill.cjm.message

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import de.leonhard.storage.SimplixBuilder
import net.insprill.cjm.CustomJoinMessages
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files

class MessageSenderTest {

    private lateinit var server: ServerMock
    private lateinit var plugin: CustomJoinMessages
    private lateinit var messageSender: MessageSender

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        messageSender = plugin.messageSender
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
