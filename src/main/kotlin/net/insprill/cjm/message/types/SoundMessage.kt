package net.insprill.cjm.message.types

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.xenlib.XenUtils
import net.insprill.xenlib.files.YamlFile
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.io.File

class SoundMessage(private val plugin: CustomJoinMessages) : MessageType {

    override val config = YamlFile("messages" + File.separator + "sound.yml", false)
    override val key = "Sounds"
    override val name = "sound"

    override fun handle(primaryPlayer: Player, players: List<Player>, rootPath: String?, chosenPath: String, visibility: MessageVisibility) {
        val global = config.getBoolean("$chosenPath.Global")
        val soundString = config.getString("$chosenPath.Sound")
        if (!XenUtils.isValidEnum(Sound::class.java, soundString)) {
            plugin.logger.severe("Sound $soundString doesn't exist!")
            return
        }

        val sound = Sound.valueOf(soundString!!)

        if (global) {
            primaryPlayer.world.playSound(
                primaryPlayer.location,
                sound,
                config.getDouble("$chosenPath.Volume").toFloat(),
                config.getDouble("$chosenPath.Pitch").toFloat()
            )
            return
        }

        for (player in players) {
            player.playSound(
                player.location,
                sound,
                config.getDouble("$chosenPath.Volume").toFloat(),
                config.getDouble("$chosenPath.Pitch").toFloat()
            )
        }
    }
}
