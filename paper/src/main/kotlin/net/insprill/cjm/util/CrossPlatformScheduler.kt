package net.insprill.cjm.util

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import net.insprill.spigotutils.ServerEnvironment
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

object CrossPlatformScheduler {
    fun runDelayed(plugin: Plugin, runnable: Runnable, delay: Long) {
        if (ServerEnvironment.isFolia())
            Bukkit.getGlobalRegionScheduler().runDelayed(plugin, { runnable.run() }, delay)
        else
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delay)
    }

    fun runAsync(plugin: Plugin, runnable: Runnable) {
        if (ServerEnvironment.isFolia())
            Bukkit.getAsyncScheduler().run { runnable }
        else
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable)
    }

    fun runAtFixedRate(plugin: Plugin, runnable: Runnable, initialDelayTicks: Long, periodTicks: Long): CancelableTask {
        return if (ServerEnvironment.isFolia()) {
            val task = Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, { runnable.run() }, initialDelayTicks, periodTicks)
            PaperCancelableTask(task)
        } else {
            val task = Bukkit.getScheduler().runTaskTimer(plugin, runnable, initialDelayTicks, periodTicks)
            BukkitCancelableTask(task)
        }
    }

    interface CancelableTask {
        fun cancel()
    }

    private class PaperCancelableTask(private val task: ScheduledTask) : CancelableTask {
        override fun cancel() {
            task.cancel()
        }
    }

    private class BukkitCancelableTask(private val task: BukkitTask) : CancelableTask {
        override fun cancel() {
            task.cancel()
        }
    }

}
