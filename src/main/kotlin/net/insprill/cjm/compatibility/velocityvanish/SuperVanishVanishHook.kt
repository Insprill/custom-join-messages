package net.insprill.cjm.compatibility.velocityvanish

import ir.syrent.velocityvanish.spigot.VelocityVanishSpigot
import ir.syrent.velocityvanish.spigot.event.PostUnVanishEvent
import ir.syrent.velocityvanish.spigot.event.PostVanishEvent
import ir.syrent.velocityvanish.spigot.event.PreUnVanishEvent
import ir.syrent.velocityvanish.spigot.event.PreVanishEvent
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.VanishHook
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class VelocityVanishVanishHook(override val plugin: CustomJoinMessages) : VanishHook, Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPreVanish(e: PreVanishEvent) {
        e.sendQuitMessage = false
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPreUnvanish(e: PreUnVanishEvent) {
        e.sendJoinMessage = false
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onVanish(e: PostVanishEvent) {
        handleToggle(e.player, true)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onUnvanish(e: PostUnVanishEvent) {
        handleToggle(e.player, false)
    }

    override fun isVanished(player: Player): Boolean {
        return VelocityVanishSpigot.instance.vanishedNames.contains(player.name)
    }

}
