package net.insprill.cjm.formatting

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import net.insprill.cjm.CustomJoinMessages
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class FormatterTypeTest {


    private lateinit var server: ServerMock
    private lateinit var plugin: CustomJoinMessages

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
    }

    @ParameterizedTest
    @EnumSource(FormatterType::class)
    fun prettyName_NotEmpty(type: FormatterType) {
        assertFalse(type.prettyName.isEmpty())
    }

    @Test
    fun formatter_MineDown_CorrectType() {
        assertInstanceOf(MinedownFormatter::class.java, FormatterType.MINEDOWN.formatter)
    }

    @Test
    fun formatter_MiniMessage_CorrectType() {
        assertInstanceOf(MiniMessageFormatter::class.java, FormatterType.MINIMESSAGE.formatter)
    }

    @Test
    fun formatter_Legacy_CorrectType() {
        assertInstanceOf(LegacyFormatter::class.java, FormatterType.LEGACY.formatter)
    }

    @Test
    fun compatibilityResult_None() {
        assertTrue(FormatterType.CompatibilityResult.NONE.status)
        assertTrue(FormatterType.CompatibilityResult.NONE.message.isEmpty())
    }

}
