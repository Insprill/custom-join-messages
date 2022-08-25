package net.insprill.cjm.compatibility.vanishnopacket

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.PluginHook

class VanishNoPacketHook(plugin: CustomJoinMessages) : PluginHook {

    override val authHook = null
    override val vanishHook = VanishNoPacketVanishHook(plugin)
    override val jailHook = null

}
