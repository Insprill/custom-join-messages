package net.insprill.cjm.update

import com.google.gson.JsonParser
import net.insprill.cjm.BuildParameters
import net.insprill.cjm.CustomJoinMessages

class SpigotUpdateChecker(plugin: CustomJoinMessages) : UpdateChecker(plugin) {

    override val platform = Platform.SPIGOT
    override val resourceUrl = "https://www.spigotmc.org/resources/%s/updates".format(BuildParameters.SPIGOT_RESOURCE_ID)
    override val requestUrl = "https://api.spiget.org/v2/resources/%s/versions/latest".format(BuildParameters.SPIGOT_RESOURCE_ID)

    @Suppress("DEPRECATION") // Legacy :/
    override fun parseVersion(json: String): VersionData {
        val obj = JsonParser().parse(json).asJsonObject
        val versionNumber = obj["name"].asString
        val downloads = obj["downloads"].asInt
        val ratingCount = obj["rating"].asJsonObject["count"].asInt
        val ratingAverage = obj["rating"].asJsonObject["average"].asFloat
        val datePublished = obj["releaseDate"].asLong
        return VersionData(versionNumber, downloads, VersionData.Rating(ratingCount, ratingAverage), datePublished)
    }

}
