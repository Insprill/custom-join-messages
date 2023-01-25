package net.insprill.cjm

import net.swiftzer.semver.SemVer
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

@Suppress("KotlinConstantConditions")
class MetadataTest {

    @Test
    fun bStatsId() {
        assertNotNull(bStatsId.toIntOrNull())
    }

    @Test
    fun spigotResourceId_ValidId() {
        assertNotNull(spigotResourceId.toIntOrNull())
    }

    @Test
    fun modrinthProjectId_ValidId() {
        assertTrue(modrinthProjectId.matches(Regex("^[a-zA-Z0-9]{8}$")))
    }

    @Test
    fun version_ValidSemVer() {
        assertDoesNotThrow { SemVer.parse(version) }
    }

    @Test
    fun targetPlatform_SpigotOrModrinth() {
        assertTrue(targetPlatform == "spigot" || targetPlatform == "modrinth")
    }

}
