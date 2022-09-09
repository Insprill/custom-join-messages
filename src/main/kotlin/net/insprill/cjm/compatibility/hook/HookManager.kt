package net.insprill.cjm.compatibility.hook

import net.insprill.spigotutils.ServerEnvironment
import org.bukkit.entity.Player

class HookManager(pluginHooks: List<PluginHook>) {

    val authHooks: List<AuthHook> = pluginHooks
        .filter { it.authHook != null }
        .map { it.authHook!! }

    val vanishHooks: List<VanishHook> = pluginHooks
        .filter { it.vanishHook != null }
        .map { it.vanishHook!! }

    val jailHooks: List<JailHook> = pluginHooks
        .filter { it.jailHook != null }
        .map { it.jailHook!! }

    fun isLoggedIn(player: Player): Boolean {
        return authHooks.isEmpty() || authHooks.any { it.isLoggedIn(player) }
    }

    fun isVanished(player: Player): Boolean {
        // TODO: Remove MockBukkit check when MockBukkit#628 is merged
        return vanishHooks.any { it.isVanished(player) } || (!ServerEnvironment.isMockBukkit() && player.getMetadata("vanished").any { it.asBoolean() })
    }

    fun isJailed(player: Player): Boolean {
        return jailHooks.any { it.isJailed(player) }
    }

}
