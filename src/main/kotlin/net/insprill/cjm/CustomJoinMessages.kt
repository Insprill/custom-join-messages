package net.insprill.cjm

import net.insprill.cjm.compatibility.Auth
import net.insprill.cjm.compatibility.Jail
import net.insprill.cjm.compatibility.Vanish
import net.insprill.cjm.compatibility.hook.AuthMeHook
import net.insprill.cjm.compatibility.hook.CmiHook
import net.insprill.cjm.compatibility.hook.EssentialsHook
import net.insprill.cjm.compatibility.hook.SuperVanishHook
import net.insprill.cjm.compatibility.hook.VanishNoPacketHook
import net.insprill.cjm.listeners.JoinEvent
import net.insprill.cjm.listeners.QuitEvent
import net.insprill.cjm.listeners.WorldChangeEvent
import net.insprill.cjm.message.MessageSender
import net.insprill.cjm.messages.types.ActionbarMessage
import net.insprill.cjm.messages.types.ChatMessage
import net.insprill.cjm.messages.types.SoundMessage
import net.insprill.cjm.messages.types.TitleMessage
import net.insprill.cjm.utils.Dependency
import net.insprill.xenlib.XenLib
import net.insprill.xenlib.commands.Command
import net.insprill.xenlib.files.YamlFile
import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class CustomJoinMessages : JavaPlugin() {

    private val BSTATS_PLUGIN_ID = 6346

    lateinit var metrics: Metrics
    lateinit var messageSender: MessageSender
    lateinit var auth: Auth
    lateinit var jail: Jail
    lateinit var vanish: Vanish
    lateinit var cmiHook: CmiHook

    override fun onEnable() {
        metrics = Metrics(this, BSTATS_PLUGIN_ID)
        metrics.addCustomChart(SimplePie("worldBasedMessages") {
            YamlFile.CONFIG.getBoolean("World-Based-Messages.Enabled").toString()
        })
        auth = Auth()
        jail = Jail()
        vanish = Vanish(this)

        XenLib.init(this)

        Dependency.initClasses()

        registerListeners()

        Command("cjm", "net.insprill.cjm.commands")
        messageSender = MessageSender(this, ActionbarMessage(), ChatMessage(), SoundMessage(), TitleMessage())
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)
    }

    private fun registerListeners() {
        Bukkit.getPluginManager().registerEvents(QuitEvent(this), this)
        Bukkit.getPluginManager().registerEvents(WorldChangeEvent(this), this)
        if (Dependency.AUTH_ME.isEnabled && YamlFile.CONFIG.getBoolean("Addons.AuthMe.Use-Login-Event")) {
            Bukkit.getPluginManager().registerEvents(AuthMeHook(this), this)
        } else {
            Bukkit.getPluginManager().registerEvents(JoinEvent(this), this)
        }
        if (YamlFile.CONFIG.getBoolean("Addons.Vanish.Fake-Messages.Enabled")) {
            if (Dependency.CMI.isEnabled) {
                cmiHook = CmiHook(this)
                Bukkit.getPluginManager().registerEvents(cmiHook, this)
            }
            if (Dependency.SUPER_VANISH.isEnabled) Bukkit.getPluginManager().registerEvents(SuperVanishHook(this), this)
            if (Dependency.VANISH_NO_PACKET.isEnabled) Bukkit.getPluginManager()
                .registerEvents(VanishNoPacketHook(this), this)
            if (Dependency.ESSENTIALS.isEnabled) Bukkit.getPluginManager().registerEvents(EssentialsHook(this), this)
        }
    }

}
