package net.insprill.cjm

import co.aikar.commands.PaperCommandManager
import de.leonhard.storage.SimplixBuilder
import de.leonhard.storage.Yaml
import de.leonhard.storage.internal.settings.ConfigSettings
import de.leonhard.storage.internal.settings.DataType
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
import net.insprill.cjm.toggle.ToggleHandler
import net.insprill.spigotutils.MinecraftVersion
import net.insprill.spigotutils.ServerEnvironment
import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.nio.file.Path
import java.util.Collections
import java.util.jar.JarFile

class CustomJoinMessages : JavaPlugin() {

    lateinit var messageSender: MessageSender
    lateinit var hookManager: HookManager
    lateinit var toggleHandler: ToggleHandler
    lateinit var config: Yaml

    override fun onEnable() {
        if (!checkCompatible())
            return

        config = SimplixBuilder.fromPath(Path.of("$dataFolder/config.yml"))
            .addInputStreamFromResource("config.yml")
            .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
            .setDataType(DataType.SORTED)
            .createYaml()

        if (!migrateFromLegacyConfiguration())
            return

        val manifest = JarFile(file).manifest.mainAttributes
        val metrics = Metrics(this, manifest.getValue("bStats-Id").toInt())
        metrics.addCustomChart(SimplePie("worldBasedMessages") {
            config.getBoolean("World-Based-Messages.Enabled").toString()
        })

        val pluginHooks = getPluginHooks()
        hookManager = HookManager(pluginHooks)

        registerListeners()

        toggleHandler = ToggleHandler(this)

        messageSender = MessageSender(this)

        val messageTypes = listOf(
            ActionbarMessage(this),
            BossbarMessage(this),
            ChatMessage(this),
            SoundMessage(this),
            TitleMessage(this),
        )

        messageTypes.forEach {
            metrics.addCustomChart(SimplePie("message_type_" + it.name) { it.isEnabled.toString() })
            messageSender.registerType(it)
        }

        registerCommands()
    }

    private fun checkCompatible(): Boolean {
        if (!ServerEnvironment.isSpigot()) {
            logger.severe("Custom Join Messages only works on Spigot, or forks of Spigot like Paper! Please upgrade to one of the two.")
            Bukkit.getPluginManager().disablePlugin(this)
            return false
        }

        if (MinecraftVersion.isOlderThan(MinecraftVersion.v1_9_0)) {
            logger.severe("Custom Join Messages only supports 1.9+ servers! (https://howoldisminecraft188.today/)")
            Bukkit.getPluginManager().disablePlugin(this)
            return false
        }
        return true
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

        val cjmCommand = CjmCommand(manager, this)
        manager.registerCommand(cjmCommand)
    }

    private fun migrateFromLegacyConfiguration(): Boolean {
        val version = config.getString("version")
        if (!version.startsWith("1.") && !version.startsWith("2."))
            return true
        dataFolder.renameTo(File(dataFolder.parentFile, "$name-old"))
        logger.severe("It appears you've recently upgraded to CJM 17 from an older version.")
        logger.severe("Since the configuration has changed in layout, your old config folder has been renamed to \"$name-old\" and a new one has been generated.")
        logger.severe("Please manually setup the new configuration and restart your server to re-enabled the plugin.")
        onEnable()
        Bukkit.getPluginManager().disablePlugin(this)
        return false
    }

}
