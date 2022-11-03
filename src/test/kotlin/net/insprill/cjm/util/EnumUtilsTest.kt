package net.insprill.cjm.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class EnumUtilsTest {

    @Test
    fun tryGetEnum_ValidName_FindsEnum() {
        val result = EnumUtils.tryGetEnum(null, "ENUM_VALUE_ONE", TestEnum::class)

        assertEquals(TestEnum.ENUM_VALUE_ONE, result)
    }

    @Test
    fun tryGetEnum_Lowercase_FindsEnum() {
        val result = EnumUtils.tryGetEnum(null, "enum_value_one", TestEnum::class)

        assertEquals(TestEnum.ENUM_VALUE_ONE, result)
    }

    @Test
    fun tryGetEnum_Invalid_ReturnsNull() {
        val result = EnumUtils.tryGetEnum(null, "enum_value_two", TestEnum::class)

        assertNull(result)
    }

    private enum class TestEnum {
        ENUM_VALUE_ONE,
    }

}
