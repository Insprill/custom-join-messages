package net.insprill.cjm.compatibility.velocityvanish

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.PluginHook

open class VelocityVanishHook(plugin: CustomJoinMessages) : PluginHook {

    override val authHook = null
    override val vanishHook = VelocityVanishVanishHook(plugin)
    override val jailHook = null

}
