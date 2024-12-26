package net.insprill.cjm.message.types

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.spigotutils.MinecraftVersion
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.Sound
import org.bukkit.entity.Player
import javax.management.ReflectionException

class SoundMessage(private val plugin: CustomJoinMessages) : MessageType(plugin, "sound", "Sounds") {

    override val isEnabled: Boolean get() = super.isEnabled && MinecraftVersion.isAtLeast(MinecraftVersion.v1_12_0)

    override fun handle(primaryPlayer: Player, recipients: List<Player>, chosenPath: String, visibility: MessageVisibility) {
        val rawSound = config.getString("$chosenPath.Sound")
        var soundKey = NamespacedKey.fromString(rawSound)
        if (soundKey == null) {
            soundKey = try {
                (Sound::class.java.getField(rawSound.uppercase()).get(null) as Sound).key
            } catch (exception: ReflectionException) {
                null
            }
        }
        val sound = soundKey?.let { Registry.SOUNDS.get(soundKey) }
        if (sound == null) {
            plugin.logger.severe("Unknown sound $rawSound! Please consult the wiki for valid sounds.")
            return
        }
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
