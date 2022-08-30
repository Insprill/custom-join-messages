package net.insprill.cjm.compatibility

import net.insprill.cjm.compatibility.authme.AuthMeHook
import net.insprill.cjm.compatibility.cmi.CmiHook
import net.insprill.cjm.compatibility.essentials.EssentialsHook
import net.insprill.cjm.compatibility.hook.PluginHook
import net.insprill.cjm.compatibility.supervanish.SuperVanishHook
import net.insprill.cjm.compatibility.vanishnopacket.VanishNoPacketHook
import net.insprill.cjm.util.ServiceProviderUtils.getRegisteredServiceProvider
import org.bukkit.Bukkit

enum class Dependency(private val pluginName: String, val pluginHookClass: Class<out PluginHook?>? = null, val clazz: Any? = null) {
    AUTH_ME("AuthMe", AuthMeHook::class.java),
    CMI("CMI", CmiHook::class.java),
    ESSENTIALS("Essentials", EssentialsHook::class.java),
    PAPI("PlaceholderAPI"),
    PREMIUM_VANISH("PremiumVanish", SuperVanishHook::class.java),
    SUPER_VANISH("SuperVanish", SuperVanishHook::class.java),
    VANISH_NO_PACKET("VanishNoPacket", VanishNoPacketHook::class.java),
    VAULT("Vault", null, getRegisteredServiceProvider("net.milkbowl.vault.chat.Chat")?.provider),
    ;

    val isEnabled get() = Bukkit.getPluginManager().isPluginEnabled(pluginName)

}
