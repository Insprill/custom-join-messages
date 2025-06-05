package net.insprill.cjm.message.types

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.spigotutils.MinecraftVersion
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.lang.reflect.Method

@Suppress("PrivatePropertyName") // Underscores in reflection property names
class SoundMessage(private val plugin: CustomJoinMessages) : MessageType(plugin, "sound", "Sounds") {

    private val sound_class: Class<*>? = Class.forName("org.bukkit.Sound")
    private val sound_getKey: Method? = try {
        sound_class?.getMethod("getKey")
    } catch (_: ReflectiveOperationException) {
        null
    }
    private val sound_valueOf: Method? = try {
        sound_class?.getMethod("valueOf", String::class.java)
    } catch (_: ReflectiveOperationException) {
        null
    }

    override fun handle(primaryPlayer: Player, recipients: List<Player>, chosenPath: String, visibility: MessageVisibility) {
        val rawSound = config.getString("$chosenPath.Sound")

        if (MinecraftVersion.isAtLeast(MinecraftVersion.v1_16_5))
            handle(primaryPlayer, recipients, chosenPath, rawSound)
        else
            handleLegacy(primaryPlayer, recipients, chosenPath, rawSound)
    }

    private fun handle(primaryPlayer: Player, recipients: List<Player>, chosenPath: String, rawSound: String) {
        var soundKey = NamespacedKey.fromString(rawSound)
        if (soundKey == null) {
            soundKey = try {
                @Suppress("DEPRECATION")
                if (MinecraftVersion.isAtLeast(MinecraftVersion.v1_21_3))
                    (sound_class?.getField(rawSound.uppercase())?.get(null) as Sound).key
                else {
                    sound_getKey?.invoke(getSound(rawSound)) as NamespacedKey?
                }
            } catch (_: ReflectiveOperationException) {
                null
            }
        }
        val sound = soundKey?.let { Registry.SOUNDS.get(soundKey) }
        if (sound == null) {
            plugin.logger.severe("Unknown sound $rawSound! Please consult the wiki for valid sounds.")
            return
        }

        playSound(primaryPlayer, recipients, chosenPath, sound)
    }

    private fun handleLegacy(primaryPlayer: Player, recipients: List<Player>, chosenPath: String, rawSound: String) {
        val sound = try {
            getSound(rawSound)
        } catch (_: ReflectiveOperationException) {
            plugin.logger.severe("Unknown sound $rawSound! Please consult the wiki for valid sounds.")
            return
        }

        playSound(primaryPlayer, recipients, chosenPath, sound)
    }

    private fun playSound(primaryPlayer: Player, recipients: List<Player>, chosenPath: String, sound: Sound) {
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

    private fun getSound(enumName: String): Sound {
        return sound_valueOf?.invoke(null, enumName.uppercase()) as Sound
    }
}
