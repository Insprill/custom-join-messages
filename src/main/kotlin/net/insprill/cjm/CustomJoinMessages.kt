package net.insprill.cjm

import net.insprill.cjm.compatibility.Dependency
import net.insprill.cjm.compatibility.hook.HookManager
import net.insprill.cjm.compatibility.hook.PluginHook
import net.insprill.cjm.listener.JoinEvent
import net.insprill.cjm.listener.QuitEvent
import net.insprill.cjm.listener.WorldChangeEvent
import net.insprill.cjm.message.MessageSender
import net.insprill.cjm.messages.types.ActionbarMessage
import net.insprill.cjm.messages.types.ChatMessage
import net.insprill.cjm.messages.types.SoundMessage
import net.insprill.cjm.messages.types.TitleMessage
import net.insprill.xenlib.XenLib
import net.insprill.xenlib.commands.Command
import net.insprill.xenlib.files.YamlFile
import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class CustomJoinMessages : JavaPlugin() {

    lateinit var messageSender: MessageSender

    lateinit var hookManager: HookManager

    override fun onEnable() {
        val metrics = Metrics(this, 6346) // todo: can the id be a gradle property?
        metrics.addCustomChart(SimplePie("worldBasedMessages") {
            YamlFile.CONFIG.getBoolean("World-Based-Messages.Enabled").toString()
        })

        XenLib.init(this)

        val pluginHooks = getPluginHooks()
        hookManager = HookManager(pluginHooks)

        registerListeners()

        Command("cjm", "net.insprill.cjm.commands")

        val messageTypes = listOf(ActionbarMessage(), ChatMessage(), SoundMessage(), TitleMessage())

        for (msg in messageTypes) {
            metrics.addCustomChart(SimplePie("message_type_" + msg.name) { msg.isEnabled.toString() })
        }

        messageSender = MessageSender(this, messageTypes)
    }

    private fun getPluginHooks(): List<PluginHook> {
        val hooks = ArrayList<PluginHook>()
        for (dependency in Dependency.values()) {
            val hook = dependency.pluginHookClass?.getConstructor(javaClass)?.newInstance(this) ?: continue
            hooks.add(hook)
        }
        return Collections.unmodifiableList(hooks)
    }

    private fun registerListeners() {
        if (hookManager.authHooks.isEmpty()) {
            Bukkit.getPluginManager().registerEvents(JoinEvent(this), this)
        }
        Bukkit.getPluginManager().registerEvents(QuitEvent(this), this)
        Bukkit.getPluginManager().registerEvents(WorldChangeEvent(this), this)
        hookManager.vanishHooks
            .filterIsInstance<Listener>()
            .forEach {
                if (YamlFile.CONFIG.getBoolean("Addons.Vanish.Fake-Messages.Enabled")) {
                    Bukkit.getPluginManager().registerEvents(it, this)
                }
            }
    }

}
