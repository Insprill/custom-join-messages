package net.insprill.cjm.message.types

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.placeholder.Placeholders.Companion.fillPlaceholders
import net.insprill.xenlib.MinecraftVersion
import net.insprill.xenlib.XenUtils
import net.insprill.xenlib.files.YamlFile
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.io.File

class BossbarMessage(private val plugin: CustomJoinMessages) : MessageType {

    override val config = YamlFile("messages" + File.separator + "bossbar.yml").setModifiable(false)
    override val key = "Messages"
    override val name = "bossbar"

    override fun handle(primaryPlayer: Player, players: List<Player>, rootPath: String?, chosenPath: String, visibility: MessageVisibility) {
        val msg = fillPlaceholders(primaryPlayer, config.getString("$chosenPath.Message")!!)
        val barColor = checkEnum(config.getString("$chosenPath.Bar-Color")!!, BarColor::class.java) ?: return
        val barStyle = checkEnum(config.getString("$chosenPath.Bar-Style")!!, BarStyle::class.java) ?: return
        val barFlags = config.getStringList("$chosenPath.Bar-Flags").mapNotNull { checkEnum(it, BarFlag::class.java) }.toTypedArray()
        val showTime = config.getLong("$chosenPath.Show-Time")
        val countDown = config.getBoolean("$chosenPath.Count-Down", true)

        val barInfo = BarInfo(
            msg,
            barColor,
            barStyle,
            barFlags,
            countDown,
            showTime,
            players.filter { visibility != MessageVisibility.PUBLIC || primaryPlayer != it }
        )

        if (config.getBoolean("MiniMessage") && MinecraftVersion.isAtLeast(MinecraftVersion.v1_16_0)) {
            handleMiniMessage(barInfo)
        } else {
            handleLegacy(barInfo)
        }
    }

    private fun handleMiniMessage(info: BarInfo) {
        val component = MiniMessage.miniMessage().deserialize(info.msg)
        val bossBar = BossBar.bossBar(
            component,
            1.0F,
            BossBar.Color.valueOf(info.color.name),
            when (info.style) {
                BarStyle.SOLID -> BossBar.Overlay.PROGRESS
                BarStyle.SEGMENTED_6 -> BossBar.Overlay.NOTCHED_6
                BarStyle.SEGMENTED_10 -> BossBar.Overlay.NOTCHED_10
                BarStyle.SEGMENTED_12 -> BossBar.Overlay.NOTCHED_12
                BarStyle.SEGMENTED_20 -> BossBar.Overlay.NOTCHED_20
            },
            info.flags.map {
                when (it) {
                    BarFlag.DARKEN_SKY -> BossBar.Flag.DARKEN_SCREEN
                    BarFlag.PLAY_BOSS_MUSIC -> BossBar.Flag.PLAY_BOSS_MUSIC
                    BarFlag.CREATE_FOG -> BossBar.Flag.CREATE_WORLD_FOG
                }
            }.toMutableSet()
        )

        for (p in info.players) {
            p.showBossBar(bossBar)
        }

        if (info.countDown) {
            class CountDown : BukkitRunnable() {
                var progress = 1.0F
                override fun run() {
                    progress -= 1 / info.showTime.toFloat()
                    if (progress < 0.0) {
                        for (p in info.players) {
                            p.hideBossBar(bossBar)
                        }
                        cancel()
                        return
                    }
                    bossBar.progress(progress)
                }
            }
            CountDown().runTaskTimer(plugin, 1, 1)
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                for (p in info.players) {
                    p.hideBossBar(bossBar)
                }
            }, info.showTime)
        }
    }

    private fun handleLegacy(info: BarInfo) {
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
        if (!XenUtils.isValidEnum(type, str)) {
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