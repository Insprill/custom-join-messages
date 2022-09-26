package net.insprill.cjm.message

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class MessageVisibilityTest {

    @ParameterizedTest
    @EnumSource(MessageVisibility::class)
    fun configSection_NotBlank(visibility: MessageVisibility) {
        assertFalse(visibility.configSection.isBlank())
    }

    @ParameterizedTest
    @EnumSource(MessageAction::class)
    fun supports_PublicSupportsAll(action: MessageAction) {
        assertTrue(MessageVisibility.PUBLIC.supports(action))
    }

    @ParameterizedTest
    @EnumSource(MessageAction::class)
    fun supports_PrivateDoesntSupportQuit(action: MessageAction) {
        assertTrue(MessageVisibility.PRIVATE.supports(action) == (action != MessageAction.QUIT))
    }

}
