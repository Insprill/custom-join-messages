package net.insprill.cjm.update

import com.google.gson.JsonParser
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.modrinthProjectId
import java.time.Instant
import java.time.ZoneOffset

class ModrinthUpdateChecker(plugin: CustomJoinMessages) : UpdateChecker(plugin) {

    override val platform = Platform.MODRINTH
    override val resourceUrl = "https://modrinth.com/plugin/%s".format(modrinthProjectId)
    override val requestUrl = "https://api.modrinth.com/v2/project/%s/version".format(modrinthProjectId)

    @Suppress("DEPRECATION") // Legacy :/
    override fun parseVersion(json: String): VersionData {
        val obj = JsonParser().parse(json).asJsonArray[0].asJsonObject
        val versionNumber = obj.get("version_number").asString
        val downloads = obj.get("downloads").asInt
        val datePublished = Instant.parse(obj.get("date_published").asString).atZone(ZoneOffset.UTC).toEpochSecond()
        return VersionData(versionNumber, downloads, null, datePublished)
    }

}
