package net.insprill.cjm.compatibility

import com.Zrips.CMI.CMI
import com.Zrips.CMI.commands.list.vanishedit
import de.myzelyam.api.vanish.VanishAPI
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.CmiHook
import net.insprill.cjm.messages.MessageAction
import net.insprill.cjm.utils.Dependency
import net.insprill.xenlib.files.YamlFile
import org.bukkit.entity.Player
import org.kitteh.vanish.VanishPlugin

class Vanish(private val plugin: CustomJoinMessages) {

    fun isVanished(player: Player): Boolean {
        if (Dependency.SUPER_VANISH.isEnabled || Dependency.PREMIUM_VANISH.isEnabled)
            return VanishAPI.isInvisible(player)
        if (Dependency.VANISH_NO_PACKET.isEnabled)
            return VanishPlugin.getPlugin(VanishPlugin::class.java).manager.isVanished(player)
        if (Dependency.CMI.isEnabled)
            return CMI.getInstance().playerManager.getUser(player).vanish.`is`(vanishedit.VanishAction.isVanished)
        return player.getMetadata("vanished").any { m -> m.asBoolean() }
    }

    fun handleToggle(player: Player, isVanishing: Boolean) {
        val path = "Addons.Vanish.Fake-Messages"
        if (!YamlFile.CONFIG.getBoolean("$path.Enabled", true)) return
        if (isVanishing) {
            if (YamlFile.CONFIG.getBoolean("$path.Vanish", true)) {
                plugin.messageSender.sendMessages(player, MessageAction.QUIT, false)
            }
        } else {
            if (YamlFile.CONFIG.getBoolean("$path.Unvanish", true)) {
                plugin.messageSender.sendMessages(player, MessageAction.JOIN, false)
            }
        }
    }

}
