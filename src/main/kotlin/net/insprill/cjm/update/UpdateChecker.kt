package net.insprill.cjm.update

import net.insprill.cjm.CustomJoinMessages
import net.insprill.spigotutils.ServerEnvironment
import net.swiftzer.semver.SemVer
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.net.URL

abstract class UpdateChecker(private val plugin: CustomJoinMessages) {

    fun isEnabled(type: NotificationType): Boolean {
        return plugin.config.getBoolean("Update-Checker.Enabled") && plugin.config.getBoolean(type.configPath)
    }

    abstract val platform: Platform
    abstract val resourceUrl: String
    abstract val requestUrl: String

    private fun getLatestVersion(): VersionData {
        val conn = URL(requestUrl).openConnection()
        conn.addRequestProperty("User-Agent", plugin.name + "UpdateChecker")
        val body = conn.inputStream.use { String(it.readBytes(), Charsets.UTF_8) }
        return parseVersion(body)
    }

    protected abstract fun parseVersion(json: String): VersionData

    fun checkForUpdates(consumer: (VersionData, Platform) -> Unit) {
        if (ServerEnvironment.isMockBukkit() || requestUrl.isBlank())
            return
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            val latestVersion = getLatestVersion()
            if (!latestVersion.isNewer(plugin))
                return@Runnable
            consumer.invoke(latestVersion, platform)
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

    enum class Platform(val factory: (CustomJoinMessages) -> UpdateChecker) {
        HANGAR({ HangarUpdateChecker(it) }),
        MODRINTH({ ModrinthUpdateChecker(it) }),
        SPIGOT({ SpigotUpdateChecker(it) }),
    }

}
