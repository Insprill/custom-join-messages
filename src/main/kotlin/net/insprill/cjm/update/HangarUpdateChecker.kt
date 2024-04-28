package net.insprill.cjm.update

import net.insprill.cjm.CustomJoinMessages

class HangarUpdateChecker(plugin: CustomJoinMessages) : UpdateChecker(plugin) {

    override val platform = Platform.HANGAR
    override val resourceUrl = ""
    override val requestUrl = ""

    override fun parseVersion(json: String): VersionData {
        return VersionData("", 0, null, 0)
    }

}
