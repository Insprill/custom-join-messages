package net.insprill.cjm.message.types

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.util.EnumUtils.tryGetEnum
import org.bukkit.Sound
import org.bukkit.entity.Player

class SoundMessage(private val plugin: CustomJoinMessages) : MessageType(plugin, "sound", "Sounds") {

    override fun handle(primaryPlayer: Player, recipients: List<Player>, chosenPath: String, visibility: MessageVisibility) {
        val sound = tryGetEnum(plugin, config.getString("$chosenPath.Sound"), Sound::class) ?: return
        val volume = config.getOrDefault("$chosenPath.Volume", 1.0f)
        val pitch = config.getOrDefault("$chosenPath.Pitch", 1.0f)

        if (config.getBoolean("$chosenPath.Global")) {
            for (player in recipients) {
                player.playSound(player.location, sound, volume, pitch)
            }
        } else {
            primaryPlayer.world.playSound(primaryPlayer.location, sound, volume, pitch)
        }

    }
}
