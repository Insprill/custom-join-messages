package net.insprill.cjm.update

import com.google.gson.JsonParser
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.spigotResourceId
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class SpigotUpdateChecker(private val plugin: CustomJoinMessages) : UpdateChecker(plugin) {

    private val projectId = spigotResourceId

    override fun getPlatform(): Platform {
        return Platform.SPIGOT
    }

    override fun getResourceUrl(): String {
        return RESOURCE_URL.format(projectId)
    }

    override fun getLatestVersion(): VersionData {
        val conn = URL(REQUEST_URL.format(projectId)).openConnection() as HttpURLConnection
        conn.addRequestProperty("User-Agent", plugin.name + "UpdateChecker")
        val body = String(conn.inputStream.readAllBytes(), StandardCharsets.UTF_8)
        return parseVersion(body)
    }

    @Suppress("DEPRECATION") // Legacy :/
    private fun parseVersion(json: String): VersionData {
        val obj = JsonParser().parse(json).asJsonObject
        val versionNumber = obj.get("name").asString
        val downloads = obj.get("downloads").asInt
        val ratingCount = obj.get("rating").asJsonObject.get("count").asInt
        val ratingAverage = obj.get("rating").asJsonObject.get("average").asFloat
        val datePublished = obj.get("releaseDate").asLong
        return VersionData(versionNumber, downloads, VersionData.Rating(ratingCount, ratingAverage), datePublished)
    }

    companion object {
        private const val REQUEST_URL = "https://api.spiget.org/v2/resources/%s/versions/latest"
        private const val RESOURCE_URL = "https://www.spigotmc.org/resources/%s/updates"
    }

}
