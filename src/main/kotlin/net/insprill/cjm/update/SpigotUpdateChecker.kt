package net.insprill.cjm.update

import com.google.gson.JsonParser
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.spigotResourceId

class SpigotUpdateChecker(plugin: CustomJoinMessages) : UpdateChecker(plugin) {

    override val platform = Platform.SPIGOT
    override val resourceUrl = "https://modrinth.com/plugin/%s".format(spigotResourceId)
    override val requestUrl = "https://api.modrinth.com/v2/project/%s/version".format(spigotResourceId)

    @Suppress("DEPRECATION") // Legacy :/
    override fun parseVersion(json: String): VersionData {
        val obj = JsonParser().parse(json).asJsonObject
        val versionNumber = obj.get("name").asString
        val downloads = obj.get("downloads").asInt
        val ratingCount = obj.get("rating").asJsonObject.get("count").asInt
        val ratingAverage = obj.get("rating").asJsonObject.get("average").asFloat
        val datePublished = obj.get("releaseDate").asLong
        return VersionData(versionNumber, downloads, VersionData.Rating(ratingCount, ratingAverage), datePublished)
    }

}
