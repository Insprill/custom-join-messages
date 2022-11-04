package net.insprill.cjm.extension

import co.aikar.commands.ACFUtil
import co.aikar.commands.BukkitCommandManager
import co.aikar.commands.MessageType
import co.aikar.locales.MessageKey
import org.bukkit.command.CommandSender

fun BukkitCommandManager.getMessage(key: String, vararg replacements: String): String {
    return ACFUtil.replaceStrings(this.locales.getMessage(null, MessageKey.of(key)), *replacements)
}

fun BukkitCommandManager.sendInfo(sender: CommandSender, key: String, vararg replacements: String) {
    this.sendMessage(sender, MessageType.INFO, MessageKey.of(key), *replacements)
}
