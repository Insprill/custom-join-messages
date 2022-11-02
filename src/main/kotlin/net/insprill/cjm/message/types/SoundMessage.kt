package net.insprill.cjm.message.types

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import org.bukkit.Sound
import org.bukkit.entity.Player

class SoundMessage(private val plugin: CustomJoinMessages) : MessageType(plugin, "sound", "Sounds") {

    override fun handle(primaryPlayer: Player, players: List<Player>, chosenPath: String, visibility: MessageVisibility) {
        val soundString = config.getString("$chosenPath.Sound")
        if (enumValues<Sound>().none { it.name == soundString }) {
            plugin.logger.severe("Sound $soundString doesn't exist!")
            return
        }

        val sound = Sound.valueOf(soundString)
        val volume = config.getOrDefault("$chosenPath.Volume", 1.0f)
        val pitch = config.getOrDefault("$chosenPath.Pitch", 1.0f)

        if (config.getBoolean("$chosenPath.Global")) {
            primaryPlayer.world.playSound(primaryPlayer.location, sound, volume, pitch)
            return
        }

        for (player in players) {
            player.playSound(player.location, sound, volume, pitch)
        }
    }
}
