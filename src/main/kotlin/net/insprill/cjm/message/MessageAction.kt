package net.insprill.cjm.message

enum class MessageAction(val configSection: String) {
    FIRST_JOIN("First-Join"),
    JOIN("Join"),
    QUIT("Quit"),
}
