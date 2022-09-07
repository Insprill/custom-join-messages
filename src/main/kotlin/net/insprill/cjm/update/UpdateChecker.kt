package net.insprill.cjm.update

import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import net.insprill.cjm.CustomJoinMessages
import net.swiftzer.semver.SemVer
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.util.logging.Level

class UpdateChecker(private val resourceId: Int, private val plugin: CustomJoinMessages) {

    companion object {
        private const val USER_AGENT = "CustomJoinMessagesUpdateChecker"
        private const val REQUEST_URL = "https://api.spiget.org/v2/resources/%s/versions/latest"
        private const val RESOURCE_URL = "https://www.spigotmc.org/resources/%s/updates"
    }

    fun isEnabled(type: NotificationType): Boolean {
        return plugin.config.getBoolean("Update-Checker.Enabled") && plugin.config.getBoolean(type.configPath)
    }

    fun getResourceUrl(): String {
        return RESOURCE_URL.format(resourceId)
    }

    fun getVersion(consumer: (VersionData) -> Unit) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            REQUEST_URL.format(resourceId).httpGet()
                .header("User-Agent", USER_AGENT)
                .responseObject { _, _, deser ->
                    try {
                        if (deser.component2() != null) {
                            throw deser.component2()!!
                        }
                        consumer.invoke(deser.component1()!!)
                    } catch (exception: Exception) {
                        plugin.logger.log(Level.WARNING, "Unable to check for updates", exception)
                    }
                }
        })
    }

    data class VersionData(val downloads: Int, val rating: Rating, val name: String, val releaseDate: Long) {

        fun isNewer(plugin: Plugin): Boolean {
            return SemVer.parse(name) > SemVer.parse(plugin.description.version)
        }

        data class Rating(val count: Int, val average: Float)

    }

    enum class NotificationType(val configPath: String) {
        IN_GAME("Update-Checker.Notifications.In-Game"),
        CONSOLE("Update-Checker.Notifications.Config")
    }

}
