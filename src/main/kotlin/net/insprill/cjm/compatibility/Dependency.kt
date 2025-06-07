package net.insprill.cjm.compatibility

import net.insprill.cjm.compatibility.advancedvanish.AdvancedVanishHook
import net.insprill.cjm.compatibility.authme.AuthMeHook
import net.insprill.cjm.compatibility.cmi.CmiHook
import net.insprill.cjm.compatibility.essentials.EssentialsHook
import net.insprill.cjm.compatibility.hook.PluginHook
import net.insprill.cjm.compatibility.supervanish.SuperVanishHook
import net.insprill.cjm.compatibility.vanishnopacket.VanishNoPacketHook
import net.insprill.cjm.compatibility.velocityvanish.VelocityVanishHook
import net.insprill.cjm.compatibility.phoenix.PhoenixHook
import net.insprill.cjm.util.ServiceProviderUtils.getRegisteredServiceProvider
import net.swiftzer.semver.SemVer
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

enum class Dependency(
    private val pluginName: String,
    val pluginHookClass: Class<out PluginHook?>? = null,
    val clazz: Any? = null,
    private val minVersion: SemVer? = null
) {
    // Make sure all dependencies are added to the
    // `softdepend` list in the plugin.yml!
    ADVANCED_VANISH("AdvancedVanish", AdvancedVanishHook::class.java),
    AUTH_ME("AuthMe", AuthMeHook::class.java),
    CMI("CMI", CmiHook::class.java, minVersion = SemVer(9, 7, 0)), // 9.7.0.0 moved the VanishAction class
    ESSENTIALS("Essentials", EssentialsHook::class.java),
    PAPI("PlaceholderAPI"),
    PREMIUM_VANISH("PremiumVanish", SuperVanishHook::class.java),
    SAYAN_VANISH("SayanVanish"),
    SUPER_VANISH("SuperVanish", SuperVanishHook::class.java),
    VANISH_NO_PACKET("VanishNoPacket", VanishNoPacketHook::class.java),
    VAULT("Vault", null, getRegisteredServiceProvider("net.milkbowl.vault.chat.Chat")?.provider),
    VELOCITY_VANISH("VelocityVanish", VelocityVanishHook::class.java),
    PHOENIX("Phoenix", PhoenixHook::class.java),
    ;

    val isEnabled get() = Bukkit.getPluginManager().isPluginEnabled(pluginName)

    fun isVersionCompatible(cjm: Plugin): Boolean {
        if (minVersion != null) {
            val version = Bukkit.getPluginManager()
                .getPlugin(pluginName)
                ?.description
                ?.version
                ?.replace("""\.\d+$""".toRegex(), "")
                ?: return true
            val semVersion = SemVer.parseOrNull(version)
            if (semVersion == null) {
                cjm.logger.warning("Failed to parse version of $pluginName ($version)! Enabling support anyways, although it may be broken!")
                return true
            }
            if (semVersion < minVersion) {
                cjm.logger.severe("$pluginName is outdated! Please update it to at least $minVersion to use its integration with CJM.")
                return false
            }
        }
        return true
    }

}
