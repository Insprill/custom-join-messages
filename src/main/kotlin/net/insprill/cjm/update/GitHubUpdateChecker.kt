package net.insprill.cjm.update

import net.insprill.cjm.CustomJoinMessages

// I was just lazy with this one ngl
class GitHubUpdateChecker(plugin: CustomJoinMessages) : ModrinthUpdateChecker(plugin) {

    override val platform = Platform.GITHUB
    // Intentionally not overridden so people go to Modrinth instead of back to GitHub.
    // override val resourceUrl = "https://github.com/Insprill/custom-join-messages/releases/latest"

}
