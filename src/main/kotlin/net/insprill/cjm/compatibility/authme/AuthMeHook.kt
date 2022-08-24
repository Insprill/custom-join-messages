package net.insprill.cjm.compatibility.authme

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.PluginHook

class AuthMeHook(private val plugin: CustomJoinMessages) : PluginHook {

    override val authHook = AuthMeAuthHook(plugin)
    override val vanishHook = null
    override val jailHook = null

}
