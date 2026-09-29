package net.insprill.cjm.toggle

import de.leonhard.storage.SimplixBuilder
import de.leonhard.storage.internal.FlatFile
import de.leonhard.storage.internal.settings.ReloadSettings
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import net.insprill.cjm.message.types.MessageType
import net.insprill.cjm.util.PermissionUtil
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.Plugin
import java.nio.file.Paths

class ToggleHandler(val plugin: CustomJoinMessages) {

    private val toggleConfig: FlatFile = SimplixBuilder.fromPath(Paths.get("${plugin.dataFolder}/data/toggles.json"))
        .setReloadSettings(ReloadSettings.MANUALLY)
        .createJson()

    init {
        val version = toggleConfig.getOrDefault("version", 1)
        if (version == 1) {
            for (uuid in toggleConfig.singleLayerKeySet()) {
                for (action in toggleConfig.singleLayerKeySet(uuid)) {
                    toggleConfig.set("$uuid.$action.enabled", toggleConfig.get("${uuid}.${action}"))
                }
            }
            toggleConfig.set("version", 2)
        }
    }

    fun registerPermissions() {
        for (typeName in plugin.messageSender.typeMap.keys) {
            PermissionUtil.registerIfMissing("cjm.view.$typeName", PermissionDefault.TRUE)
        }
        for (action in MessageAction.entries) {
            PermissionUtil.registerIfMissing("cjm.view.${action.displayName}", PermissionDefault.TRUE)
            for (type in plugin.messageSender.typeMap.values) {
                PermissionUtil.registerIfMissing("cjm.view.${action.displayName}.${type.name}", PermissionDefault.TRUE)
            }
        }
    }

    fun isToggled(player: OfflinePlayer, action: MessageAction?, type: MessageType? = null): Boolean {
        val actionToggled = action?.let { toggleConfig.getOrDefault(getKey(player, action, null), true) } ?: true
        val typeToggled = type?.let { toggleConfig.getOrDefault(getKey(player, null, type), true) } ?: true
        val actionTypeToggled = toggleConfig.getOrDefault(getKey(player, action, type), true)
        val isConfigToggled = actionToggled && typeToggled && actionTypeToggled

        var isPermissionToggled = true
        if (player is Player) {
            val actionToggled = action?.let { player.hasPermission("cjm.view.${action.displayName}") } ?: true
            val typeToggled = type?.let { player.hasPermission("cjm.view.${type.name}") } ?: true
            val actionTypeToggled = action?.let { type?.let { player.hasPermission("cjm.view.${action.displayName}.${type.name}") } } ?: true
            isPermissionToggled = actionToggled && typeToggled && actionTypeToggled
        }

        return isConfigToggled && isPermissionToggled
    }

    fun setToggle(player: OfflinePlayer, action: MessageAction?, type: MessageType? = null, toggle: Boolean) {
        return toggleConfig.set(getKey(player, action, type), toggle)
    }

    private fun getKey(player: OfflinePlayer, action: MessageAction?, type: MessageType?): String {
        return if (action != null && type != null)
            "${player.uniqueId}.${action.displayName}.${type.name}"
        else if (action != null)
            "${player.uniqueId}.${action.displayName}.enabled"
        else if (type != null)
            "${player.uniqueId}.${type.name}.enabled"
        else
            throw IllegalArgumentException("action and type cannot both be null!")
    }

}
