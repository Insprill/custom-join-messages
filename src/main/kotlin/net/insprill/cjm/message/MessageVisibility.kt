package net.insprill.cjm.message

enum class MessageVisibility(val configSection: String, private vararg val supportedActions: MessageAction) {
    PUBLIC("Public", MessageAction.FIRST_JOIN, MessageAction.JOIN, MessageAction.QUIT),
    PRIVATE("Private", MessageAction.FIRST_JOIN, MessageAction.JOIN),
    ;

    fun supports(action: MessageAction): Boolean {
        return supportedActions.contains(action)
    }

}
