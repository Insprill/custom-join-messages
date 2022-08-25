package net.insprill.cjm.compatibility.locklogin

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.PluginHook

class LockLoginHook(private val plugin: CustomJoinMessages) : PluginHook {

    override val authHook = LockLoginAuthHook(plugin, LockLoginModule()) //todo: this needs to be instantiated by LockLogin, not us
    override val vanishHook = null
    override val jailHook = null

}
