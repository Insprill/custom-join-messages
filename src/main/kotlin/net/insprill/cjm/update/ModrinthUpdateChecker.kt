package net.insprill.cjm.update

import com.google.gson.JsonParser
import net.insprill.cjm.BuildParameters
import net.insprill.cjm.CustomJoinMessages
import java.time.Instant
import java.time.ZoneOffset

class ModrinthUpdateChecker(plugin: CustomJoinMessages) : UpdateChecker(plugin) {

    override val platform = Platform.MODRINTH
    override val resourceUrl = "https://modrinth.com/plugin/%s".format(BuildParameters.MODRINTH_PROJECT_ID)
    override val requestUrl = "https://api.modrinth.com/v2/project/%s/version".format(BuildParameters.MODRINTH_PROJECT_ID)

    @Suppress("DEPRECATION") // Legacy :/
    override fun parseVersion(body: String): VersionData {
        val obj = JsonParser().parse(body).asJsonArray[0].asJsonObject
        val versionNumber = obj["version_number"].asString
        val downloads = obj["downloads"].asInt
        val datePublished = Instant.parse(obj["date_published"].asString).atZone(ZoneOffset.UTC).toEpochSecond()
        return VersionData(versionNumber, downloads, null, datePublished)
    }

}
