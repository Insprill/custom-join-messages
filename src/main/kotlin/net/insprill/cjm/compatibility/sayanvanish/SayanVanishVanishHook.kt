package net.insprill.cjm.compatibility.sayanvanish

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.VanishHook
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.sayandev.sayanvanish.api.SayanVanishAPI
import org.sayandev.sayanvanish.bukkit.api.event.BukkitUserUnVanishEvent
import org.sayandev.sayanvanish.bukkit.api.event.BukkitUserVanishEvent

class SayanVanishVanishHook(override val plugin: CustomJoinMessages) : VanishHook {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerVanish(e: BukkitUserVanishEvent) {
        e.user.player()?.let { handleToggle(it, true) }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerUnvanish(e: BukkitUserUnVanishEvent) {
        e.user.player()?.let { handleToggle(it, false) }
    }

    override fun isVanished(player: Player): Boolean {
        return SayanVanishAPI.getInstance().isVanished(player.uniqueId, true)
    }

}
