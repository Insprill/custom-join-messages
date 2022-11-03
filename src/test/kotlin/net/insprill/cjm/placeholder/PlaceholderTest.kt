package net.insprill.cjm.placeholder

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class PlaceholderTest {

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
    fun stringName_NoDuplicates() {
        val duplicates = Placeholder.values()
            .map { it.stringName.lowercase() }
            .groupingBy { it }
            .eachCount()
            .filter { it.value > 1 }

        if (duplicates.isNotEmpty()) {
            fail { "Found duplicate Placeholders: $duplicates" }
        }
    }

    @ParameterizedTest
    @EnumSource(Placeholder::class)
    fun result_NotNull(placeholder: Placeholder) {
        val player = server.addPlayer()

        assertNotNull(placeholder.result.invoke(player))
    }

}
