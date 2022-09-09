package net.insprill.cjm.compatibility.locklogin

import eu.locklogin.api.module.LoadRule
import eu.locklogin.api.module.plugin.javamodule.ModuleLoader
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.AuthHook
import net.insprill.cjm.compatibility.hook.PluginHook

class LockLoginHook(private val plugin: CustomJoinMessages) : PluginHook {

    override lateinit var authHook: AuthHook internal set // Set in LockLoginModule
    override val vanishHook = null
    override val jailHook = null

    init {
        val classLockLogin = Class.forName("eu.locklogin.plugin.bukkit.LockLogin")
        val methodGetLoader = classLockLogin.getDeclaredMethod("getLoader")
        methodGetLoader.trySetAccessible()
        val moduleLoader = methodGetLoader.invoke(null) as ModuleLoader
        moduleLoader.loadModule(plugin.file, LoadRule.POSTPLUGIN)
    }

}
