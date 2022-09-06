package net.insprill.cjm.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.BukkitCommandManager
import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.annotation.Syntax
import de.themoep.minedown.MineDown
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.message.types.MessageType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.Locale
import java.util.function.Consumer


@CommandAlias("cjm|customjoinmessages")
@Description("Base command for Custom Join Messages.")
@Suppress("UNUSED")
class CjmCommand(private val manager: BukkitCommandManager, private val plugin: CustomJoinMessages) : BaseCommand() {

    fun updateLocale() {
        val requestedLang = plugin.config.getOrDefault("language", "en")!!.lowercase()
        manager.locales.defaultLocale = if (!manager.supportedLanguages.any { it.language.equals(requestedLang) }) {
            plugin.logger.severe("Unsupported language '$requestedLang'. Defaulting to 'en'. Please choose from one of the following: ${manager.supportedLanguages.map { it.language }}")
            Locale.ENGLISH
        } else {
            manager.supportedLanguages.first { it.language.equals(requestedLang) }
        }
    }

    @HelpCommand
    @Syntax("(page)")
    @CommandPermission("cjm.command.help")
    @Suppress("UNUSED_PARAMETER")
    fun onHelp(sender: CommandSender, help: CommandHelp) {
        help.helpEntries.sortBy { it.command }
        help.showHelp()
    }

    @Subcommand("reload|r")
    @CommandPermission("cjm.command.reload")
    @Description("Reloads all configuration files")
    fun onReload(sender: CommandSender) {
        plugin.config.forceReload()
        plugin.messageSender.typeMap.values.forEach(Consumer { m: MessageType -> m.config.forceReload() })
        plugin.messageSender.setupPermissions()
        sender.spigot().sendMessage(*MineDown.parse("&aPlugin reloaded."))
    }

    @Subcommand("preview|p")
    @Syntax("[target] [messageType] [visibility] [action] [messageId]")
    @CommandCompletion("@players @messageType @messageVisibility @messageAction @messageId")
    @CommandPermission("cjm.command.preview")
    @Description("Previews how a particular message will look/sound")
    fun onPreview(sender: CommandSender, target: Player, messageType: MessageType, visibility: MessageVisibility, action: MessageAction, id: Int) {
        val path = "${visibility.configSection}.${action.configSection}.$id"
        if (!messageType.config.contains(path)) {
            sender.spigot().sendMessage(*MineDown.parse("&cA message with ID &4%id% &cdoesn't exist!", "id", id.toString()))
            return
        }

        val randomKey = plugin.messageSender.getRandomKey(messageType.config, "$path.${messageType.key}") ?: return
        messageType.handle(target, listOf(target), path, randomKey, MessageVisibility.PRIVATE)
    }

}
