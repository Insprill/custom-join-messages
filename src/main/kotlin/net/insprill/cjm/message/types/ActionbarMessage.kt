package net.insprill.cjm.message.types

import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.placeholder.Placeholders.Companion.fillPlaceholders
import net.insprill.xenlib.MinecraftVersion
import net.insprill.xenlib.files.YamlFile
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player
import java.io.File

class ActionbarMessage : MessageType {

    override val config = YamlFile("messages" + File.separator + "actionbar.yml").setModifiable(false)
    override val key = "Messages"
    override val name = "actionbar"

    override fun handle(primaryPlayer: Player, players: List<Player>, rootPath: String?, chosenPath: String, visibility: MessageVisibility) {
        var msg = config.getString("$chosenPath.Message")!!
        msg = fillPlaceholders(primaryPlayer, msg)
        for (p in players) {
            handle(msg, p)
        }
    }

    private fun handle(msg: String, player: Player) {
        if (config.getBoolean("MiniMessage") && MinecraftVersion.isAtLeast(MinecraftVersion.v1_16_0)) {
            val component = MiniMessage.miniMessage().deserialize(msg)
            player.sendActionBar(component)
        } else {
            @Suppress("DEPRECATION") // Legacy support
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(msg))
        }
    }

}
