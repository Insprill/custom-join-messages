package net.insprill.cjm

import net.swiftzer.semver.SemVer
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Suppress("KotlinConstantConditions", "SimplifyBooleanWithConstants")
class BuildParametersTest {

    @Test
    fun bStatsId() {
        assertNotNull(BuildParameters.BSTATS_ID.toIntOrNull())
    }

    @Test
    fun spigotResourceId_ValidId() {
        assertNotNull(BuildParameters.SPIGOT_RESOURCE_ID.toIntOrNull())
    }

    @Test
    fun modrinthProjectId_ValidId() {
        assertTrue(BuildParameters.MODRINTH_PROJECT_ID.matches(Regex("^[a-zA-Z0-9]{8}$")))
    }

    @Test
    fun version_ValidSemVer() {
        // Cannot use assertDoesNotThrow here due to bytecode version mismatch
        SemVer.parse(BuildParameters.VERSION)
    }

    @Test
    fun targetPlatform_ValidPlatform() {
        assertTrue(
            BuildParameters.TARGET_PLATFORM == "hangar"
                    || BuildParameters.TARGET_PLATFORM == "modrinth"
                    || BuildParameters.TARGET_PLATFORM == "spigot"
        )
    }

}
