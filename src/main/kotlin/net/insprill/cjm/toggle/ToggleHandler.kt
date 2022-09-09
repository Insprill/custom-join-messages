package net.insprill.cjm.toggle

import de.leonhard.storage.SimplixBuilder
import de.leonhard.storage.internal.FlatFile
import de.leonhard.storage.internal.settings.ReloadSettings
import net.insprill.cjm.message.MessageAction
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.Plugin
import java.nio.file.Path

class ToggleHandler(plugin: Plugin) {

    private val toggleConfig: FlatFile = SimplixBuilder.fromPath(Path.of("${plugin.dataFolder}/data/toggles.json"))
        .setReloadSettings(ReloadSettings.MANUALLY)
        .createJson()

    fun isToggled(player: OfflinePlayer, action: MessageAction): Boolean {
        return toggleConfig.getOrDefault("${player.uniqueId}.${action.name}", true)
    }

    fun setToggle(player: OfflinePlayer, action: MessageAction, toggle: Boolean) {
        return toggleConfig.set("${player.uniqueId}.${action.name}", toggle)
    }

}
