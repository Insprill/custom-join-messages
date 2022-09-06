package net.insprill.cjm.message.types

import de.themoep.minedown.MineDown
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.message.MessageVisibility
import net.insprill.cjm.placeholder.Placeholders.Companion.fillPlaceholders
import net.md_5.bungee.api.ChatMessageType
import org.bukkit.entity.Player

class ActionbarMessage(plugin: CustomJoinMessages) : MessageType(plugin) {

    override val key = "Messages"
    override val name = "actionbar"

    override fun handle(primaryPlayer: Player, players: List<Player>, rootPath: String?, chosenPath: String, visibility: MessageVisibility) {
        var msg = config.getString("$chosenPath.Message")!!
        msg = fillPlaceholders(primaryPlayer, msg)
        val components = MineDown.parse(msg)
        for (player in players) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *components)
        }
    }

}
