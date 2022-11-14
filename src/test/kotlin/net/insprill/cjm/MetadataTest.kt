package net.insprill.cjm

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Suppress("KotlinConstantConditions")
class MetadataTest {

    @Test
    fun bStatsId() {
        if (bStatsId.isNotBlank()) {
            assertNotNull(bStatsId.toIntOrNull())
        }
    }

    @Test
    fun spigotResourceId() {
        if (spigotResourceId.isNotBlank()) {
            assertNotNull(spigotResourceId.toIntOrNull())
        }
    }

    @Test
    fun modrinthProjectId() {
        if (modrinthProjectId.isNotBlank()) {
            assertTrue(modrinthProjectId.matches(Regex("^[a-zA-Z0-9]*$")))
        }
    }

    @Test
    fun targetPlatform() {
        if (targetPlatform.isNotBlank()) {
            assertTrue(targetPlatform == "spigot" || targetPlatform == "modrinth")
        }
    }

}
