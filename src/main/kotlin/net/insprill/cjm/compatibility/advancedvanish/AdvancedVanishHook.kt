package net.insprill.cjm.compatibility.advancedvanish

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.PluginHook

open class AdvancedVanishHook(plugin: CustomJoinMessages) : PluginHook {

    override val authHook = null
    override val vanishHook = AdvancedVanishVanishHook(plugin)
    override val jailHook = null

}
