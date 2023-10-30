package net.insprill.cjm.listener

import co.aikar.locales.MessageKey
import de.themoep.minedown.MineDown
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import net.insprill.cjm.update.UpdateChecker
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.text.SimpleDateFormat
import java.util.Date

class JoinEvent(private val plugin: CustomJoinMessages) : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.joinMessage = null
        if (!plugin.worldChangeEvent.isEnabled)
            return;
        val action = if (e.player.hasPlayedBefore()) MessageAction.JOIN else MessageAction.FIRST_JOIN
        plugin.messageSender.trySendMessages(e.player, action, true)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onOperatorJoin(e: PlayerJoinEvent) {
        if (!e.player.isOp)
            return
        if (!plugin.updateChecker.isEnabled(UpdateChecker.NotificationType.IN_GAME))
            return
        plugin.updateChecker.checkForUpdates { data, platform ->
            val text = plugin.commandManager.locales.getMessage(null, MessageKey.of("cjm.update-checker.in-game.text"))
                .format(data.version)

            val date = SimpleDateFormat(plugin.config.getString("Update-Checker.Date-Format")).format(Date(data.releaseDateSeconds * 1000L))
            val hover = when (platform) {
                net.insprill.cjm.update.UpdateChecker.Platform.HANGAR ->
                    plugin.commandManager.locales.getMessage(null, MessageKey.of("cjm.update-checker.in-game.hover.hangar"))
                        .format(data.version)

                net.insprill.cjm.update.UpdateChecker.Platform.MODRINTH ->
                    plugin.commandManager.locales.getMessage(null, MessageKey.of("cjm.update-checker.in-game.hover.modrinth"))
                        .format(data.version, date, data.downloads)

                net.insprill.cjm.update.UpdateChecker.Platform.SPIGOT ->
                    plugin.commandManager.locales.getMessage(null, MessageKey.of("cjm.update-checker.in-game.hover.spigot"))
                        .format(data.version, date, data.downloads, data.rating?.average)
            }

            e.player.spigot().sendMessage(
                *MineDown.parse(
                    "[$text](${plugin.updateChecker.resourceUrl} $hover)"
                )
            )
        }
    }

}
