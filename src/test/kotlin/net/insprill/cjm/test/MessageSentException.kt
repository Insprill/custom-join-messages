package net.insprill.cjm.test

import org.bukkit.entity.Player

class MessageSentException(val chosenPath: String, val recipients: List<Player>) : RuntimeException()
