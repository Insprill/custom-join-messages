package net.insprill.cjm.compatibility

import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.advancedvanish.AdvancedVanishHook
import net.insprill.cjm.compatibility.authme.AuthMeHook
import net.insprill.cjm.compatibility.cmi.CmiHook
import net.insprill.cjm.compatibility.essentials.EssentialsHook
import net.insprill.cjm.compatibility.hook.PluginHook
import net.insprill.cjm.compatibility.supervanish.SuperVanishHook
import net.insprill.cjm.compatibility.vanishnopacket.VanishNoPacketHook
import net.insprill.cjm.compatibility.velocityvanish.VelocityVanishHook
import net.insprill.cjm.util.ServiceProviderUtils.getRegisteredServiceProvider
import net.swiftzer.semver.SemVer
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass

enum class Dependency(
    val pluginName: String,
    val pluginHookClass: KClass<out PluginHook>? = null,
    val registeredServiceProviderName: String? = null,
    private val minVersion: SemVer? = null
) {
    // Make sure all dependencies are added to the
    // `softdepend` list in the plugin.yml!
    ADVANCED_VANISH("AdvancedVanish", AdvancedVanishHook::class),
    AUTH_ME("AuthMe", AuthMeHook::class),
    CMI("CMI", CmiHook::class, minVersion = SemVer(9, 7, 14)), // 9.7.14.3 added a new method to get vanished status
    ESSENTIALS("Essentials", EssentialsHook::class),
    PAPI("PlaceholderAPI"),
    PREMIUM_VANISH("PremiumVanish", SuperVanishHook::class),
    SAYAN_VANISH("SayanVanish"),
    SUPER_VANISH("SuperVanish", SuperVanishHook::class),
    VANISH_NO_PACKET("VanishNoPacket", VanishNoPacketHook::class),
    VAULT("Vault", null, "net.milkbowl.vault.chat.Chat"),
    VELOCITY_VANISH("VelocityVanish", VelocityVanishHook::class),
    ;

    var isIntegrationActive = false
        private set
    var pluginHook: PluginHook? = null
        private set

    // Lazy init this to give whatever plugin implements it time to register
    val registeredServiceProvider: Any?
        get() = registeredServiceProviderName
            ?.takeIf { isIntegrationActive }
            ?.let { getRegisteredServiceProvider(it)?.provider }

    private fun init(cjm: Plugin) {
        isIntegrationActive = isEnabled && isVersionCompatible(cjm)
        pluginHook = if (isIntegrationActive) {
            pluginHookClass?.java?.getConstructor(CustomJoinMessages::class.java)?.newInstance(cjm)
        } else {
            null // Must explicitly set this back to null to not cross-contaminate tests
        }
    }

    private val isEnabled get() = Bukkit.getPluginManager().isPluginEnabled(pluginName)

    private fun isVersionCompatible(cjm: Plugin): Boolean {
        if (minVersion != null) {
            val version = Bukkit.getPluginManager()
                .getPlugin(pluginName)
                ?.description
                ?.version
                ?.replace(Regex("^((?:[^.]+\\.){2}[^.]+)\\.[^.]+$"), "$1")
            if (version == null)
                return true
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

    companion object {
        fun initAll(cjm: Plugin) {
            Dependency.entries.forEach { it.init(cjm) }
        }
    }

}
