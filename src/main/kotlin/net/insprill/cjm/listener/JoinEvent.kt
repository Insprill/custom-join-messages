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
        val action = if (e.player.hasPlayedBefore()) MessageAction.JOIN else MessageAction.FIRST_JOIN
        plugin.messageSender.trySendMessages(e.player, action, true)
        plugin.worldChangeEvent.saveVisitedWorld(e.player, e.player.world)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onOperatorJoin(e: PlayerJoinEvent) {
        if (!e.player.isOp)
            return
        if (!plugin.updateChecker.isEnabled(UpdateChecker.NotificationType.IN_GAME))
            return
        plugin.updateChecker.getVersion {
            if (!it.isNewer(plugin))
                return@getVersion

            val text = plugin.commandManager.locales.getMessage(null, MessageKey.of("cjm.update-checker.in-game.text"))
                .format(it.name)
            val hover = plugin.commandManager.locales.getMessage(null, MessageKey.of("cjm.update-checker.in-game.hover"))
                .format(
                    it.name,
                    SimpleDateFormat(plugin.config.getString("Update-Checker.Date-Format")).format(Date(it.releaseDate * 1000L)),
                    it.downloads,
                    it.rating.average
                )

            e.player.spigot().sendMessage(
                *MineDown.parse(
                    "[$text](${plugin.updateChecker.getResourceUrl()} $hover)"
                )
            )
        }
    }

}
