package net.insprill.cjm.compatibility.sayanvanish

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.PluginHook

open class SayanVanishHook(plugin: CustomJoinMessages) : PluginHook {

    override val authHook = null
    override val vanishHook = SayanVanishVanishHook(plugin)
    override val jailHook = null

}
