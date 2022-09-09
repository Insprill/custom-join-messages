package net.insprill.cjm.compatibility.locklogin

import eu.locklogin.api.module.PluginModule
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.Dependency
import org.bukkit.plugin.java.JavaPlugin

class LockLoginModule : PluginModule() {

    override fun enable() {
        val cjm = JavaPlugin.getPlugin(CustomJoinMessages::class.java) // Sucks, but no good way around it.

        val authHook = LockLoginAuthHook(cjm)
        plugin.registerListener(authHook)

        val lockLoginHook = cjm.hookManager.hooks[Dependency.LOCK_LOGIN] as LockLoginHook
        lockLoginHook.authHook = authHook
    }

    override fun disable() {
        plugin.unregisterListeners()
    }

}
