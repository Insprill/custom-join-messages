package net.insprill.cjm.update

import com.google.gson.JsonParser
import net.insprill.cjm.CustomJoinMessages
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Properties

class ModrinthUpdateChecker(metadata: Properties, private val plugin: CustomJoinMessages) : UpdateChecker(plugin) {

    private val projectId = metadata["modrinth.project.id"] as String

    override fun getPlatform(): Platform {
        return Platform.MODRINTH
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

    private fun parseVersion(json: String): VersionData {
        val obj = JsonParser.parseString(json).asJsonObject
        val versionNumber = obj.get("version_number").asString
        val downloads = obj.get("downloads").asInt
        val datePublished = LocalDateTime.parse(obj.get("date_published").asString).toEpochSecond(ZoneOffset.UTC)
        return VersionData(versionNumber, downloads, null, datePublished)
    }

    companion object {
        private const val REQUEST_URL = "https://api.modrinth.com/v2/project/%s/version"
        private const val RESOURCE_URL = "https://modrinth.com/plugin/%s"
    }

}