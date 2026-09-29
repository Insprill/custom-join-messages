package net.insprill.cjm.compatibility

import net.insprill.cjm.CustomJoinMessages
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock

class DependencyTest {

    private lateinit var server: ServerMock
    private lateinit var cjm: CustomJoinMessages

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        cjm = MockBukkit.load(CustomJoinMessages::class.java)
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun isIntegrationActive_NotOnServer_False() {
        assertFalse(Dependency.VAULT.isIntegrationActive)
    }

    @Test
    fun isIntegrationActive_OnServer_Disabled_False() {
        server.pluginManager.disablePlugin(MockBukkit.createMockPlugin("Vault"))
        Dependency.initAll(cjm)

        assertFalse(Dependency.VAULT.isIntegrationActive)
    }

    @Test
    fun isIntegrationActive_OnServer_Enabled_True() {
        MockBukkit.createMockPlugin("Vault")
        Dependency.initAll(cjm)

        assertTrue(Dependency.VAULT.isIntegrationActive)
    }

    @Test
    fun minVersion_InvalidVersion_IsCompatible() {
        MockBukkit.createMockPlugin("PlaceholderAPI", "1.2.3")
        Dependency.initAll(cjm)

        assertTrue(Dependency.PAPI.isIntegrationActive)
    }

    @Test
    fun cmiVersion_Minimum_IsCompatible() {
        MockBukkit.createMockPlugin("CMI", "9.7.14.3")
        Dependency.initAll(cjm)

        assertTrue(Dependency.CMI.isIntegrationActive)
    }

    @Test
    fun cmiVersion_LessThanMinimum_IsNotCompatible() {
        MockBukkit.createMockPlugin("CMI", "9.7.13")
        Dependency.initAll(cjm)

        assertFalse(Dependency.CMI.isIntegrationActive)
    }

}
