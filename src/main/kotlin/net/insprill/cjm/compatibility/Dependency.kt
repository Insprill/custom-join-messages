package net.insprill.cjm.compatibility

import net.insprill.cjm.compatibility.authme.AuthMeHook
import net.insprill.cjm.compatibility.cmi.CmiHook
import net.insprill.cjm.compatibility.essentials.EssentialsHook
import net.insprill.cjm.compatibility.hook.PluginHook
import net.insprill.cjm.compatibility.supervanish.SuperVanishHook
import net.insprill.cjm.compatibility.vanishnopacket.VanishNoPacketHook
import net.milkbowl.vault.chat.Chat
import org.bukkit.Bukkit

enum class Dependency(private val pluginName: String, val pluginHookClass: Class<out PluginHook?>? = null, val clazz: Any? = null) {
    AUTH_ME("AuthMe", AuthMeHook::class.java),
    CMI("CMI", CmiHook::class.java),
    ESSENTIALS("Essentials", EssentialsHook::class.java),
    LOCK_LOGIN("LockLogin"),
    PAPI("PlaceholderAPI"),
    PREMIUM_VANISH("PremiumVanish", SuperVanishHook::class.java),
    SUPER_VANISH("SuperVanish", SuperVanishHook::class.java),
    VANISH_NO_PACKET("VanishNoPacket", VanishNoPacketHook::class.java),
    VAULT("Vault", null, Bukkit.getServicesManager().getRegistration(Chat::class.java)?.provider);

    val isEnabled get() = Bukkit.getPluginManager().isPluginEnabled(pluginName)

}
