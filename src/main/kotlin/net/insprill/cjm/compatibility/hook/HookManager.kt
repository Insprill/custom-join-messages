package net.insprill.cjm.compatibility.hook

import org.bukkit.entity.Player

class HookManager(pluginHooks: List<PluginHook>) {

    val authHooks: List<AuthHook> = pluginHooks.mapNotNull { it.authHook }
    val vanishHooks: List<VanishHook> = pluginHooks.mapNotNull { it.vanishHook }
    val jailHooks: List<JailHook> = pluginHooks.mapNotNull { it.jailHook }

    fun isLoggedIn(player: Player): Boolean {
        return authHooks.isEmpty() || authHooks.any { it.isLoggedIn(player) }
    }

    fun isVanished(player: Player): Boolean {
        return vanishHooks.any { it.isVanished(player) } || player.getMetadata("vanished").any { it.asBoolean() }
    }

    fun isJailed(player: Player): Boolean {
        return jailHooks.any { it.isJailed(player) }
    }

}
