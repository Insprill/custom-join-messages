package net.insprill.cjm

import net.insprill.cjm.hooks.*
import net.insprill.cjm.listeners.JoinEvent
import net.insprill.cjm.listeners.QuitEvent
import net.insprill.cjm.listeners.WorldChangeEvent
import net.insprill.cjm.messages.MessageSender
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

    override fun onEnable() {
        metrics = Metrics(this, BSTATS_PLUGIN_ID)
        metrics.addCustomChart(SimplePie("worldBasedMessages") {
            YamlFile.CONFIG.getBoolean("World-Based-Messages.Enabled").toString()
        })

        XenLib.init(this)

        Dependency.initClasses()

        registerListeners()

        Command("cjm", "net.insprill.cjm.commands")
        MessageSender(this)
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)
    }

    private fun registerListeners() {
        Bukkit.getPluginManager().registerEvents(QuitEvent(), this)
        Bukkit.getPluginManager().registerEvents(WorldChangeEvent(), this)
        if (Dependency.AUTH_ME.isEnabled && YamlFile.CONFIG.getBoolean("Addons.AuthMe.Use-Login-Event")) {
            Bukkit.getPluginManager().registerEvents(AuthMeHook(), this)
        } else {
            Bukkit.getPluginManager().registerEvents(JoinEvent(), this)
        }
        if (YamlFile.CONFIG.getBoolean("Addons.Vanish.Fake-Messages.Enabled")) {
            if (Dependency.CMI.isEnabled) Bukkit.getPluginManager().registerEvents(CMIHook(), this)
            if (Dependency.SUPER_VANISH.isEnabled) Bukkit.getPluginManager().registerEvents(SuperVanishHook(), this)
            if (Dependency.VANISH_NO_PACKET.isEnabled) Bukkit.getPluginManager()
                .registerEvents(VanishNoPacketHook(), this)
            if (Dependency.ESSENTIALS.isEnabled) Bukkit.getPluginManager().registerEvents(EssentialsHook(), this)
        }
    }

}