package net.insprill.cjm.update

import net.insprill.cjm.BuildParameters
import net.insprill.cjm.CustomJoinMessages

class HangarUpdateChecker(plugin: CustomJoinMessages) : UpdateChecker(plugin) {

    override val platform = Platform.HANGAR
    override val resourceUrl = "https://hangar.papermc.io/Insprill/%s/versions?channel=Release".format(BuildParameters.HANGAR_PROJECT_ID)
    override val requestUrl = "https://hangar.papermc.io/api/v1/projects/%s/latestrelease".format(BuildParameters.HANGAR_PROJECT_ID)

    override fun parseVersion(body: String): VersionData {
        return VersionData(body, 0, null, 0)
    }

}
