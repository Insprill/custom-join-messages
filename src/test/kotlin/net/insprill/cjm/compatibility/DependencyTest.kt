package net.insprill.cjm.compatibility

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DependencyTest {

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
    fun isEnabled_NotOnServer_False() {
        assertFalse(Dependency.VAULT.isEnabled)
    }

    @Test
    fun isEnabled_OnServer_Disabled_False() {
        server.pluginManager.disablePlugin(MockBukkit.createMockPlugin("Vault"))

        assertFalse(Dependency.VAULT.isEnabled)
    }

    @Test
    fun isEnabled_OnServer_Enabled_True() {
        MockBukkit.createMockPlugin("Vault")

        assertTrue(Dependency.VAULT.isEnabled)
    }

}
