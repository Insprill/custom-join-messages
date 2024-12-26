package net.insprill.cjm.formatting

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.util.TestUtils.setFinalField
import net.insprill.spigotutils.ServerEnvironment
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock

class FormatterTypeTest {

    private lateinit var server: ServerMock
    private lateinit var plugin: CustomJoinMessages
    private lateinit var lastServerEnvironment: ServerEnvironment

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CustomJoinMessages::class.java)
        lastServerEnvironment = ServerEnvironment.getCurrentEnvironment()
    }

    @AfterEach
    fun teardown() {
        MockBukkit.unmock()
        setFinalField(ServerEnvironment::class, "currentEnvironment", lastServerEnvironment)
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
    fun formatter_MineDown_IsCompatible() {
        assertTrue(FormatterType.MINEDOWN.isCompatible.invoke().status)
    }

    @Test
    fun formatter_MiniMessage_CorrectType() {
        assertInstanceOf(MiniMessageFormatter::class.java, FormatterType.MINIMESSAGE.formatter)
    }

    @Test
    fun formatter_MiniMessage_IsCompatible() {
        assertTrue(FormatterType.MINIMESSAGE.isCompatible.invoke().status)
    }

    @Test
    fun formatter_MiniMessage_NotPaper_NotCompatible() {
        setFinalField(ServerEnvironment::class, "currentEnvironment", ServerEnvironment.SPIGOT)

        assertFalse(FormatterType.MINIMESSAGE.isCompatible.invoke().status)
        assertFalse(FormatterType.MINIMESSAGE.isCompatible.invoke().message.isBlank())
    }

    @Test
    fun formatter_Legacy_CorrectType() {
        assertInstanceOf(LegacyFormatter::class.java, FormatterType.LEGACY.formatter)
    }

    @Test
    fun formatter_Legacy_IsCompatible() {
        assertTrue(FormatterType.LEGACY.isCompatible.invoke().status)
    }

    @Test
    fun compatibilityResult_None() {
        assertTrue(FormatterType.CompatibilityResult.NONE.status)
        assertTrue(FormatterType.CompatibilityResult.NONE.message.isEmpty())
    }

}
