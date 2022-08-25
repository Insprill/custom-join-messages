package net.insprill.cjm.compatibility.hook

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import net.insprill.xenlib.files.YamlFile
import org.bukkit.entity.Player

interface VanishHook {

    val plugin: CustomJoinMessages

    fun isVanished(player: Player): Boolean

    fun handleToggle(player: Player, isVanishing: Boolean) {
        val path = "Addons.Vanish.Fake-Messages"
        if (!YamlFile.CONFIG.getBoolean("$path.Enabled", true)) return
        if (isVanishing) {
            if (YamlFile.CONFIG.getBoolean("$path.Vanish", true)) {
                plugin.messageSender.trySendMessages(player, MessageAction.QUIT, false)
            }
        } else {
            if (YamlFile.CONFIG.getBoolean("$path.Unvanish", true)) {
                plugin.messageSender.trySendMessages(player, MessageAction.JOIN, false)
            }
        }
    }

}
