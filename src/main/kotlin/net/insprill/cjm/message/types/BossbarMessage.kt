package net.insprill.cjm.message.types

import de.themoep.minedown.MineDown
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.placeholder.Placeholders.Companion.fillPlaceholders
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class BossbarMessage(private val plugin: CustomJoinMessages) : MessageType(plugin, "bossbar", "Messages") {

    override fun handle(primaryPlayer: Player, players: List<Player>, chosenPath: String, visibility: MessageVisibility) {
        val msg = fillPlaceholders(primaryPlayer, config.getString("$chosenPath.Message")!!)
        val barColor = checkEnum(config.getString("$chosenPath.Bar-Color")!!, BarColor::class.java) ?: return
        val barStyle = checkEnum(config.getString("$chosenPath.Bar-Style")!!, BarStyle::class.java) ?: return
        val barFlags = config.getStringList("$chosenPath.Bar-Flags").mapNotNull { checkEnum(it, BarFlag::class.java) }.toTypedArray()
        val showTime = config.getLong("$chosenPath.Show-Time")
        val countDown = config.getOrDefault("$chosenPath.Count-Down", true)

        val barInfo = BarInfo(
            BaseComponent.toLegacyText(*MineDown.parse(msg)),
            barColor,
            barStyle,
            barFlags,
            countDown,
            showTime,
            players.filter { visibility != MessageVisibility.PUBLIC || primaryPlayer != it }
        )

        handleBar(barInfo)
    }

    private fun handleBar(info: BarInfo) {
        val bossBar = Bukkit.createBossBar(info.msg, info.color, info.style, *info.flags)
        for (p in info.players) {
            bossBar.addPlayer(p)
        }

        if (info.countDown) {
            class CountDown : BukkitRunnable() {
                var progress = 1.0
                override fun run() {
                    progress -= 1 / info.showTime.toDouble()
                    if (progress < 0.0) {
                        bossBar.removeAll()
                        cancel()
                        return
                    }
                    bossBar.progress = progress
                }
            }
            CountDown().runTaskTimer(plugin, 1, 1)
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                bossBar.removeAll()
            }, info.showTime)
        }
    }

    private inline fun <reified T : Enum<T>> checkEnum(str: String, type: Class<T>): T? {
        if (enumValues<T>().none { it.name == str }) {
            plugin.logger.severe("Unknown ${type.simpleName} '$str'! Please choose from one of the following: ${type.enumConstants.contentToString()}")
            return null
        }
        return enumValueOf<T>(str)
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
