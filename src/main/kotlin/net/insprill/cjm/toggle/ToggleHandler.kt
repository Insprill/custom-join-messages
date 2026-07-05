package net.insprill.cjm.toggle

import de.leonhard.storage.SimplixBuilder
import de.leonhard.storage.internal.FlatFile
import de.leonhard.storage.internal.settings.ReloadSettings
import net.insprill.cjm.message.MessageAction
import net.insprill.cjm.message.types.MessageType
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.Plugin
import java.nio.file.Paths

class ToggleHandler(plugin: Plugin) {

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

    fun isToggled(player: OfflinePlayer, action: MessageAction, type: MessageType? = null): Boolean {
        return toggleConfig.getOrDefault(getKey(player, action, null), true) && toggleConfig.getOrDefault(getKey(player, action, type), true)
    }

    fun setToggle(player: OfflinePlayer, action: MessageAction, type: MessageType? = null, toggle: Boolean) {
        return toggleConfig.set(getKey(player, action, type), toggle)
    }

    private fun getKey(player: OfflinePlayer, action: MessageAction, type: MessageType?): String {
        return if (type != null)
            "${player.uniqueId}.${action.name}.${type.name}"
        else
            "${player.uniqueId}.${action.name}.enabled"
    }

}
