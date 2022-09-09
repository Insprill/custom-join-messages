package net.insprill.cjm.message

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class MessageVisibilityTest {

    @ParameterizedTest
    @EnumSource(MessageVisibility::class)
    fun configSection_NotBlank(visibility: MessageVisibility) {
        assertFalse(visibility.configSection.isBlank())
    }

}
