package net.insprill.cjm.compatibility.locklogin

import eu.locklogin.api.module.PluginModule

//TODO: Make compile
class LockLoginModule : PluginModule() {
    override fun enable() {
        plugin.registerListener(LockLoginAuthHook())
    }

    override fun disable() {

    }
}
