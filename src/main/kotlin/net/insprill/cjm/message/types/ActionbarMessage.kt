package net.insprill.cjm.message.types

import de.themoep.minedown.MineDown
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.placeholder.Placeholders.Companion.fillPlaceholders
import net.insprill.xenlib.files.YamlFile
import net.md_5.bungee.api.ChatMessageType
import org.bukkit.entity.Player
import java.io.File

class ActionbarMessage(private val plugin: CustomJoinMessages) : MessageType {

    override val config = YamlFile("messages" + File.separator + "actionbar.yml").setModifiable(false)
    override val key = "Messages"
    override val name = "actionbar"

    override fun handle(primaryPlayer: Player, players: List<Player>, rootPath: String?, chosenPath: String, visibility: MessageVisibility) {
        var msg = config.getString("$chosenPath.Message")!!
        msg = fillPlaceholders(primaryPlayer, msg)
        val components = MineDown.parse(msg)
        for (player in players) {
            @Suppress("DEPRECATION")
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *components) // TODO: 1.8 support
        }
    }

}
