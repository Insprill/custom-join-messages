package net.insprill.cjm.compatibility.hook

import net.insprill.cjm.compatibility.Dependency
import org.bukkit.entity.Player

class HookManager(pluginHooks: List<PluginHook>) {

    val hooks = pluginHooks.associateBy { hook -> Dependency.values().first { it.pluginHookClass == hook.javaClass } }

    val authHooks: List<AuthHook> = pluginHooks
        .filter { it.authHook != null }
        .map { it.authHook!! }

    val vanishHooks: List<VanishHook> = pluginHooks
        .filter { it.vanishHook != null }
        .map { it.vanishHook!! }

    private val jailHooks: List<JailHook> = pluginHooks
        .filter { it.jailHook != null }
        .map { it.jailHook!! }

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
