package net.insprill.cjm.message.types

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.extension.replacePlaceholders
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.util.CrossPlatformScheduler
import net.insprill.cjm.util.EnumUtils.tryGetEnum
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class BossbarMessage(private val plugin: CustomJoinMessages) : MessageType(plugin, "bossbar", "Messages") {

    override fun handle(primaryPlayer: Player, recipients: List<Player>, chosenPath: String, visibility: MessageVisibility) {
        val msg = config.getString("$chosenPath.Message")?.replacePlaceholders(primaryPlayer)
        if (msg.isNullOrBlank()) return
        val barColor = tryGetEnum(plugin, config.getString("$chosenPath.Bar-Color"), BarColor::class) ?: return
        val barStyle = tryGetEnum(plugin, config.getString("$chosenPath.Bar-Style"), BarStyle::class) ?: return
        val barFlags = config.getStringList("$chosenPath.Bar-Flags").mapNotNull { tryGetEnum(plugin, it, BarFlag::class) }.toTypedArray()
        val showTime = config.getLong("$chosenPath.Show-Time")
        val countDown = config.getOrDefault("$chosenPath.Count-Down", true)

        val finalRecipients = recipients.filter { visibility != MessageVisibility.PUBLIC || primaryPlayer != it } // Don't send public message to the person joining
        if (finalRecipients.isEmpty()) return

        val barInfo = BarInfo(
            BaseComponent.toLegacyText(*plugin.formatter.format(msg)),
            barColor,
            barStyle,
            barFlags,
            countDown,
            showTime,
            finalRecipients
        )

        handleBar(barInfo)
    }

    private fun handleBar(info: BarInfo) {
        val bossBar = Bukkit.createBossBar(info.msg, info.color, info.style, *info.flags)
        for (p in info.players) {
            bossBar.addPlayer(p)
        }

        if (info.countDown) {
            class CountDown : Runnable {
                var progress = 1.0
                private lateinit var cancelableTask: CrossPlatformScheduler.CancelableTask

                fun start(plugin: Plugin) {
                    cancelableTask = CrossPlatformScheduler.runAtFixedRate(plugin, this, 1, 1)
                }

                override fun run() {
                    progress -= 1 / info.showTime.toDouble()
                    if (progress < 0.0) {
                        bossBar.removeAll()
                        cancelableTask.cancel()
                    } else {
                        bossBar.progress = progress
                    }
                }
            }
            CountDown().start(plugin)
        } else {
            CrossPlatformScheduler.runDelayed(plugin, { bossBar.removeAll() }, info.showTime)
        }
    }

    class BarInfo(
        val msg: String,
        val color: BarColor,
        val style: BarStyle,
        val flags: Array<BarFlag>,
        val countDown: Boolean,
        val showTime: Long,
        val players: List<Player>
    )

}
