package net.insprill.cjm.compatibility.supervanish

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.PluginHook

open class SuperVanishHook(plugin: CustomJoinMessages) : PluginHook {

    override val authHook = null
    override val vanishHook = SuperVanishVanishHook(plugin)
    override val jailHook = null

}
