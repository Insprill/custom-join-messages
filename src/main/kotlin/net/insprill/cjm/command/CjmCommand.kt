package net.insprill.cjm.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.BukkitCommandManager
import co.aikar.commands.CommandHelp
import co.aikar.commands.CommandManager
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.MessageType
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Optional
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.annotation.Syntax
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import co.aikar.locales.MessageKey
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import net.insprill.cjm.message.MessageVisibility
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("cjm|customjoinmessages")
@Description("Base command for Custom Join Messages.")
@Suppress("UNUSED")
class CjmCommand(private val manager: BukkitCommandManager, private val plugin: CustomJoinMessages) : BaseCommand() {

    fun updateLocale() {
        val requestedLang = plugin.config.getOrDefault("language", "en")!!.lowercase()
        if (!manager.supportedLanguages.any { it.language.equals(requestedLang) }) {
            plugin.logger.severe("Unsupported language '$requestedLang'. Defaulting to 'en'. Please choose from one of the following: ${manager.supportedLanguages.map { it.language }}")
        } else {
            manager.locales.defaultLocale = manager.supportedLanguages.first { it.language.equals(requestedLang) }
        }
    }

    @HelpCommand
    @Syntax("(page)")
    @CommandPermission("cjm.command.help")
    @Description("{@@cjm.command.help.description}")
    @Suppress("UNUSED_PARAMETER")
    fun onHelp(sender: CommandSender, help: CommandHelp) {
        help.helpEntries.sortBy { it.command }
        help.showHelp()
    }

    @Subcommand("preview|p")
    @Syntax("[target] [messageType] [visibility] [action] [messageId]")
    @CommandCompletion("@players @messageType @messageVisibility @messageAction @messageId")
    @CommandPermission("cjm.command.preview")
    @Description("{@@cjm.command.preview.description}")
    @Suppress("UNUSED_PARAMETER")
    fun onPreview(
        sender: CommandSender,
        target: OnlinePlayer,
        messageType: net.insprill.cjm.message.types.MessageType,
        visibility: MessageVisibility,
        action: MessageAction,
        id: Int
    ) {
        val path = "${visibility.configSection}.${action.configSection}.$id"
        if (!messageType.config.contains(path)) {
            throw InvalidCommandArgument(MessageKey.of("cjm.command.preview.invalid-id"), "%id%", id.toString())
        }

        val randomKey = plugin.messageSender.getRandomKey(messageType.config, "$path.${messageType.key}") ?: return
        messageType.handle(target.player, listOf(target.player), randomKey, MessageVisibility.PRIVATE)
    }

    @Subcommand("toggle|t")
    @Syntax("[action] (on/off) (target)")
    @CommandCompletion("@messageAction @onOffToggle @players")
    @CommandPermission("cjm.command.toggle")
    @Description("{@@cjm.command.toggle.description}")
    fun onToggle(sender: CommandSender, action: MessageAction, @Optional toggle: String?, @Optional providedTarget: OfflinePlayer?) {
        if (sender !is Player && providedTarget == null) {
            throw InvalidCommandArgument("{@@cjm.command.toggle.no-target}")
        }
        val target = providedTarget ?: sender as Player
        val toggledTo: Boolean
        if (toggle != null) {
            toggledTo = toggle == "on"
            plugin.toggleHandler.setToggle(target, action, toggledTo)
        } else {
            toggledTo = !plugin.toggleHandler.isToggled(target, action)
            plugin.toggleHandler.setToggle(target, action, toggledTo)
        }
        sendMessage("cjm.command.toggle.${if (toggledTo) "on" else "off"}", MessageType.INFO, "%action%", action.name.lowercase())
    }

    private fun sendMessage(key: String, type: MessageType, vararg placeholders: String) {
        manager.sendMessage(CommandManager.getCurrentCommandIssuer(), type, MessageKey.of(key), *placeholders)
    }

}
