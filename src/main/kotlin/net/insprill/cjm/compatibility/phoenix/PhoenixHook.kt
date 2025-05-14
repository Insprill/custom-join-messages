package net.insprill.cjm.compatibility.phoenix

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.PluginHook

class PhoenixHook(plugin: CustomJoinMessages) : PluginHook {

    override val authHook = null
    override val vanishHook = PhoenixVanishHook(plugin)
    override val jailHook = null

}
