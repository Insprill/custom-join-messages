package net.insprill.cjm.message

import net.insprill.cjm.CustomJoinMessages
import org.bukkit.entity.Player

enum class MessageAction(val configSection: String, private val function: (CustomJoinMessages, Player) -> Boolean = { _, _ -> true }) {
    FIRST_JOIN("First-Join"),
    JOIN("Join"),
    QUIT("Quit", { plugin, player -> plugin.hookManager.isLoggedIn(player) }),
    ;

    fun canRun(plugin: CustomJoinMessages, player: Player): Boolean {
        return function.invoke(plugin, player) && plugin.toggleHandler.isToggled(player, this)
    }

}
