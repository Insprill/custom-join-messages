package net.insprill.cjm.util

import be.seeseemelk.mockbukkit.MockBukkit
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ServiceProviderUtilsTest {

    @BeforeEach
    fun setUp() {
        MockBukkit.mock()
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @Test
    fun getRegisteredServiceProvider_NoClass_ReturnsNull() {
        assertNull(ServiceProviderUtils.getRegisteredServiceProvider("non.existent.class"))
    }

    @Test
    fun getRegisteredServiceProvider_ValidClass_NoProvider() {
        assertNull(ServiceProviderUtils.getRegisteredServiceProvider(javaClass.name))
    }

    @Test
    fun getRegisteredServiceProvider_ValidClass_RegisteredProvider() {
        Bukkit.getServicesManager().register(javaClass, this, MockBukkit.createMockPlugin(), ServicePriority.Highest)

        val registeredProvider = ServiceProviderUtils.getRegisteredServiceProvider(javaClass.name)

        assertNotNull(registeredProvider)
        assertNotNull(registeredProvider?.service)
        assertNotNull(registeredProvider?.provider)
        assertEquals(javaClass, registeredProvider?.service)
        assertEquals(this, registeredProvider?.provider)
    }

}
