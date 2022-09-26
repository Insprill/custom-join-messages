package net.insprill.cjm.message

enum class MessageVisibility(val configSection: String, private vararg val unsupportedActions: MessageAction) {
    PUBLIC("Public"),
    PRIVATE("Private", MessageAction.QUIT),
    ;

    fun supports(action: MessageAction): Boolean {
        return !unsupportedActions.contains(action)
    }

}
