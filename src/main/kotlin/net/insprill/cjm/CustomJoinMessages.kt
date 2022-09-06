package net.insprill.cjm

import co.aikar.commands.PaperCommandManager
import de.leonhard.storage.SimplixBuilder
import de.leonhard.storage.internal.FlatFile
import net.insprill.cjm.command.CjmCommand
import net.insprill.cjm.command.CommandCompletion
import net.insprill.cjm.command.CommandContext
import net.insprill.cjm.compatibility.Dependency
import net.insprill.cjm.compatibility.hook.HookManager
import net.insprill.cjm.compatibility.hook.PluginHook
import net.insprill.cjm.listener.JoinEvent
import net.insprill.cjm.listener.QuitEvent
import net.insprill.cjm.listener.WorldChangeEvent
import net.insprill.cjm.message.MessageSender
import net.insprill.cjm.message.types.ActionbarMessage
import net.insprill.cjm.message.types.BossbarMessage
import net.insprill.cjm.message.types.ChatMessage
import net.insprill.cjm.message.types.SoundMessage
import net.insprill.cjm.message.types.TitleMessage
import net.insprill.spigotutils.MinecraftVersion
import net.insprill.xenlib.XenLib
import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Path
import java.util.*
import java.util.jar.JarFile

class CustomJoinMessages : JavaPlugin() {

    lateinit var messageSender: MessageSender
    lateinit var hookManager: HookManager
    lateinit var config: FlatFile

    override fun onEnable() {
        config = SimplixBuilder.fromPath(Path.of("$dataFolder/config.yml"))
            .addInputStreamFromResource("config.yml")
            .createYaml()

        val manifest = JarFile(file).manifest.mainAttributes

        val metrics = Metrics(this, manifest.getValue("bStats-Id").toInt())
        metrics.addCustomChart(SimplePie("worldBasedMessages") {
            config.getBoolean("World-Based-Messages.Enabled").toString()
        })

        XenLib.init(this)

        if (MinecraftVersion.isOlderThan(MinecraftVersion.v1_9_0)) {
            logger.severe("Custom Join Messages only supports 1.9+ servers! (https://howoldisminecraft188.today/)")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        val pluginHooks = getPluginHooks()
        hookManager = HookManager(pluginHooks)

        registerListeners()

        val messageTypes = listOf(
            ActionbarMessage(this),
            BossbarMessage(this),
            ChatMessage(this),
            SoundMessage(this),
            TitleMessage(this),
        )

        for (msg in messageTypes) {
            metrics.addCustomChart(SimplePie("message_type_" + msg.name) { msg.isEnabled.toString() })
        }

        messageSender = MessageSender(this, messageTypes)
        messageSender.setupPermissions()

        registerCommands()
    }

    private fun getPluginHooks(): List<PluginHook> {
        val hooks = ArrayList<PluginHook>()
        for (dependency in Dependency.values()) {
            if (!dependency.isEnabled)
                continue
            val hook = dependency.pluginHookClass?.getConstructor(javaClass)?.newInstance(this) ?: continue
            hooks.add(hook)
        }
        return Collections.unmodifiableList(hooks)
    }

    private fun registerListeners() {
        var registeredAuthListener = false
        for (listener in hookManager.authHooks) {
            if (!config.getBoolean("Addons.Auth.Wait-For-Login"))
                continue
            Bukkit.getPluginManager().registerEvents(listener, this)
            registeredAuthListener = true
        }
        if (!registeredAuthListener) {
            Bukkit.getPluginManager().registerEvents(JoinEvent(this), this)
        }

        Bukkit.getPluginManager().registerEvents(QuitEvent(this), this)
        Bukkit.getPluginManager().registerEvents(WorldChangeEvent(this), this)

        for (listener in hookManager.vanishHooks.filterIsInstance<Listener>()) {
            if (!config.getBoolean("Addons.Vanish.Fake-Messages.Enabled"))
                continue
            Bukkit.getPluginManager().registerEvents(listener, this)
        }
    }

    private fun registerCommands() {
        // Commands
        val manager = PaperCommandManager(this)

        @Suppress("DEPRECATION")
        manager.run {
            enableUnstableAPI("help")
            enableUnstableAPI("brigadier")
        }

        CommandContext(this).register(manager)
        CommandCompletion(this).register(manager)

        val command = CjmCommand(manager, this)
        command.updateLocale()
        manager.registerCommand(command)
    }

}
