package net.insprill.cjm.update

import net.insprill.cjm.CustomJoinMessages

// Couldn't find an anonymous API to use for update checking,
// so we just use Modrinth instead, but link players to CurseForge to not get in trouble with them.
class CurseForgeUpdateChecker(plugin: CustomJoinMessages) : ModrinthUpdateChecker(plugin) {

    override val platform = Platform.CURSEFORGE
    override val resourceUrl = "https://www.curseforge.com/minecraft/bukkit-plugins/custom-join-messages/files"

}
