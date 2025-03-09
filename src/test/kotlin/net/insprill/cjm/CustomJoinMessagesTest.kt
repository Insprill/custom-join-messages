package net.insprill.cjm

import de.leonhard.storage.SimplixBuilder
import net.insprill.cjm.util.TestUtils.setFinalField
import net.insprill.spigotutils.MinecraftVersion
import net.insprill.spigotutils.ServerEnvironment
import net.swiftzer.semver.SemVer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock
import java.io.File

class CustomJoinMessagesTest {

    private lateinit var server: ServerMock
    private lateinit var lastMinecraftVersion: MinecraftVersion

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        lastMinecraftVersion = MinecraftVersion.getCurrentVersion()
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
        setFinalField(MinecraftVersion::class, "currentVersion", lastMinecraftVersion)
    }

    @Test
    fun onEnable_ServerTooOld_Disables() {
        setFinalField(MinecraftVersion::class, "currentVersion", MinecraftVersion.v1_8_8)

        val plugin = MockBukkit.load(CustomJoinMessages::class.java)

        assertFalse(plugin.isEnabled)
    }

    @Test
    fun onEnable_NewEnough_Enables() {
        setFinalField(MinecraftVersion::class, "currentVersion", MinecraftVersion.v1_9_0)

        val plugin = MockBukkit.load(CustomJoinMessages::class.java)

        assertTrue(plugin.isEnabled)
    }

    @Test
    fun onEnable_PlainBukkit_Disables() {
        setFinalField(ServerEnvironment::class, "currentEnvironment", ServerEnvironment.BUKKIT)

        val plugin = MockBukkit.load(CustomJoinMessages::class.java)

        assertFalse(plugin.isEnabled)
    }

    @Test
    @Disabled("Fails on some systems")
    fun onEnable_LegacyConfig_GetsRenamed() {
        val plugin = MockBukkit.load(CustomJoinMessages::class.java)
        server.pluginManager.disablePlugin(plugin)
        val configFile = File(plugin.dataFolder, "config.yml")
        val config = SimplixBuilder.fromPath(configFile.toPath()).createYaml()
        config.set("MySQL.Host", "localhost")
        config.set("version", "2.0.1")

        server.pluginManager.enablePlugin(plugin)

        val movedFile = File(plugin.dataFolder.parentFile, "${plugin.name}-old")
        assertTrue(movedFile.exists())
        assertTrue(movedFile.isDirectory)

        assertFalse(plugin.isEnabled)
    }

    @Test
    @Disabled("Fails on some systems")
    fun onEnable_LegacyConfig_KeepsValues() {
        val plugin = MockBukkit.load(CustomJoinMessages::class.java)
        server.pluginManager.disablePlugin(plugin)
        val configFile = File(plugin.dataFolder, "config.yml")
        val config = SimplixBuilder.fromPath(configFile.toPath()).createYaml()
        config.set("MySQL.Host", "localhost")
        config.set("version", "2.0.1")

        server.pluginManager.enablePlugin(plugin)

        val movedFile = File(plugin.dataFolder.parentFile, "${plugin.name}-old")
        val movedConfig = SimplixBuilder.fromPath(File(movedFile, "config.yml").toPath()).createYaml()
        assertEquals("2.0.1", movedConfig.getString("version"))
        assertEquals("localhost", movedConfig.getString("MySQL.Host"))
    }

    @Test
    fun onEnable_LegacyConfig_DisablesPlugin() {
        val plugin = MockBukkit.load(CustomJoinMessages::class.java)
        server.pluginManager.disablePlugin(plugin)
        val configFile = File(plugin.dataFolder, "config.yml")
        val config = SimplixBuilder.fromPath(configFile.toPath()).createYaml()
        config.set("version", "2.0.1")

        server.pluginManager.enablePlugin(plugin)

        assertFalse(plugin.isEnabled)
    }

    @Test
    @Disabled("Fails on some systems")
    fun onEnable_LegacyConfig_WritesNewConfig() {
        val plugin = MockBukkit.load(CustomJoinMessages::class.java)
        server.pluginManager.disablePlugin(plugin)
        val configFile = File(plugin.dataFolder, "config.yml")
        val config = SimplixBuilder.fromPath(configFile.toPath()).createYaml()
        config.set("MySQL.Host", "localhost")
        config.set("version", "2.0.1")

        server.pluginManager.enablePlugin(plugin)

        config.forceReload()
        assertNotEquals("2.0.1", config.getString("version"))
        assertTrue(SemVer.parse(config.getString("version")).major >= 3)
        assertEquals("", config.getString("MySQL.Host"))
    }

    @Test
    fun onEnable_LegacyConfig_ModernConfig_DoesNothing() {
        val plugin = MockBukkit.load(CustomJoinMessages::class.java)
        server.pluginManager.disablePlugin(plugin)

        val configFile = File(plugin.dataFolder, "config.yml")
        val config = SimplixBuilder.fromPath(configFile.toPath()).createYaml()
        config.set("version", "3.0.0")

        server.pluginManager.enablePlugin(plugin)

        val movedFile = File(plugin.dataFolder.parentFile, "${plugin.name}-old")
        assertFalse(movedFile.exists())

        assertTrue(plugin.isEnabled)
    }

}
