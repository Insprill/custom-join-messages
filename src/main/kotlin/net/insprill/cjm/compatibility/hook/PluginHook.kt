package net.insprill.cjm.compatibility.hook

interface PluginHook {

    val authHook: AuthHook?
    val vanishHook: VanishHook?
    val jailHook: JailHook?

}
