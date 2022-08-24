package net.insprill.cjm.compatibility.hook

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
        return authHooks.any { it.isLoggedIn(player) }
    }

    fun isVanished(player: Player): Boolean {
        return vanishHooks.any { it.isVanished(player) }
    }

    fun isJailed(player: Player): Boolean {
        return jailHooks.any { it.isJailed(player) }
    }

}
