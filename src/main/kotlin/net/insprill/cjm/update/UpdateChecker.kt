package net.insprill.cjm.update

import net.insprill.cjm.CustomJoinMessages
import net.swiftzer.semver.SemVer
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

abstract class UpdateChecker(private val plugin: CustomJoinMessages) {

    fun isEnabled(type: NotificationType): Boolean {
        return plugin.config.getBoolean("Update-Checker.Enabled") && plugin.config.getBoolean(type.configPath)
    }

    abstract fun getPlatform(): Platform

    abstract fun getResourceUrl(): String

    abstract fun getLatestVersion(): VersionData

    fun checkForUpdates(consumer: (VersionData, Platform) -> Unit) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            val latestVersion = getLatestVersion()
            if (!latestVersion.isNewer(plugin))
                return@Runnable
            consumer.invoke(latestVersion, getPlatform())
        })
    }

    data class VersionData(val version: String, val downloads: Int, val rating: Rating?, val releaseDateSeconds: Long) {

        fun isNewer(plugin: Plugin): Boolean {
            return SemVer.parse(version) > SemVer.parse(plugin.description.version.substringBefore('-'))
        }

        data class Rating(val count: Int, val average: Float)

    }

    enum class NotificationType(val configPath: String) {
        IN_GAME("Update-Checker.Notifications.In-Game"),
        CONSOLE("Update-Checker.Notifications.Console")
    }

    enum class Platform {
        SPIGOT,
        MODRINTH
    }

}
