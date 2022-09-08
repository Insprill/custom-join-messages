package net.insprill.cjm.compatibility.hook

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageAction
import org.bukkit.entity.Player

interface VanishHook {

    val plugin: CustomJoinMessages

    fun isVanished(player: Player): Boolean

    fun handleToggle(player: Player, isVanishing: Boolean) {
        val path = "Addons.Vanish.Fake-Messages"
        if (!plugin.config.getOrDefault("$path.Enabled", true)) return
        if (isVanishing) {
            if (plugin.config.getOrDefault("$path.Vanish", true)) {
                plugin.messageSender.trySendMessages(player, MessageAction.QUIT, false)
            }
        } else {
            if (plugin.config.getOrDefault("$path.Unvanish", true)) {
                plugin.messageSender.trySendMessages(player, MessageAction.JOIN, false)
            }
        }
    }

}
