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
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.message.types.MessageType
import net.insprill.xenlib.files.YamlFile
import net.insprill.xenlib.files.YamlFolder
import net.insprill.xenlib.localization.Lang
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.function.Consumer


@CommandAlias("cjm|customjoinmessages")
@Description("Base command for Custom Join Messages.")
@Suppress("UNUSED")
class CjmCommand(private val manager: BukkitCommandManager, private val plugin: CustomJoinMessages) : BaseCommand() {

    fun updateLocale() {
        val requestedLang = YamlFile.CONFIG.getString("language", "en")!!.lowercase()
        val lang: Locale = if (!manager.supportedLanguages.any { it.language.equals(requestedLang) }) {
            plugin.logger.severe("Unsupported language '$requestedLang'. Defaulting to 'en'. Please choose from one of the following: ${manager.supportedLanguages.map { it.language }}")
            Locale.ENGLISH
        } else {
            manager.supportedLanguages.first { it.language.equals(requestedLang) }
        }
        manager.locales.defaultLocale = lang
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
        YamlFolder.LOCALE.reload()
        YamlFile.CONFIG.reload()
        plugin.messageSender.typeMap.values.forEach(Consumer { m: MessageType -> m.config.reload() })
        plugin.messageSender.setupPermissions()
        Lang.send(sender, "commands.reload")
    }

    @Subcommand("preview|p")
    @Syntax("[target] [messageType] [visibility] [action] [messageId]")
    @CommandCompletion("@players @messageType @messageVisibility @messageAction @messageId")
    @CommandPermission("cjm.command.preview")
    @Description("Previews how a particular message will look/sound")
    fun onPreview(sender: CommandSender, target: Player, messageType: MessageType, visibility: MessageVisibility, action: MessageAction, id: Int) {
        val path = "${visibility.configSection}.${action.configSection}.$id"
        if (!messageType.config.contains(path)) {
            Lang.send(sender, "commands.preview.invalid-message", "%id%;$id")
            return
        }

        val randomKey = plugin.messageSender.getRandomKey(messageType.config, "$path.${messageType.key}") ?: return
        messageType.handle(target, listOf(sender as Player), path, randomKey, MessageVisibility.PRIVATE)
    }

}
