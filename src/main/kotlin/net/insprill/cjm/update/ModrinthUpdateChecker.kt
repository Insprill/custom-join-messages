package net.insprill.cjm.update

import com.google.gson.JsonParser
import net.insprill.cjm.BuildParameters
import net.insprill.cjm.CustomJoinMessages
import java.time.Instant
import java.time.ZoneOffset

open class ModrinthUpdateChecker(val plugin: CustomJoinMessages) : UpdateChecker(plugin) {

    override val platform = Platform.MODRINTH
    override val resourceUrl = "https://modrinth.com/plugin/%s".format(BuildParameters.MODRINTH_PROJECT_ID)
    override val requestUrl = "https://api.modrinth.com/v2/project/%s/version".format(BuildParameters.MODRINTH_PROJECT_ID)

    @Suppress("DEPRECATION") // Legacy :/
    override fun parseVersion(body: String): VersionData {
        val versions = JsonParser().parse(body).asJsonArray
        val obj = if (plugin.config.getBoolean("Update-Checker.Beta-Versions"))
            versions[0].asJsonObject
        else
            versions.first { it.asJsonObject["version_type"].asString == "release" }.asJsonObject
        val versionNumber = obj["version_number"].asString
        val downloads = obj["downloads"].asInt
        val datePublished = Instant.parse(obj["date_published"].asString).atZone(ZoneOffset.UTC).toEpochSecond()
        return VersionData(versionNumber, downloads, null, datePublished)
    }

}
