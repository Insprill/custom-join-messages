package net.insprill.cjm

import co.aikar.commands.BukkitCommandManager
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
import net.insprill.cjm.extension.getMessage
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
import net.insprill.cjm.update.UpdateChecker
import net.insprill.spigotutils.MinecraftVersion
import net.insprill.spigotutils.ServerEnvironment
import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File
import java.nio.file.Path
import java.util.Collections
import java.util.Properties

class CustomJoinMessages : JavaPlugin {

    lateinit var messageSender: MessageSender
    lateinit var hookManager: HookManager
    lateinit var toggleHandler: ToggleHandler
    lateinit var config: Yaml
    lateinit var updateChecker: UpdateChecker
    lateinit var commandManager: BukkitCommandManager
    private lateinit var metrics: Metrics

    override fun onEnable() {
        if (!checkCompatible())
            return

        config = SimplixBuilder.fromPath(Path.of("$dataFolder/config.yml"))
            .addInputStreamFromResource("config.yml")
            .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
            .setDataType(DataType.SORTED)
            .createYaml()
        config.addDefaultsFromInputStream()

        if (!migrateFromLegacyConfiguration())
            return

        val buildProps = Properties().apply { load(getResource("cjm.metadata")) }

        if (!ServerEnvironment.isMockBukkit()) {
            metrics = Metrics(this, buildProps.getProperty("bstats.id").toInt())
            metrics.addCustomChart(SimplePie("worldBasedMessages") {
                config.getBoolean("World-Based-Messages.Enabled").toString()
            })
        }

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
            if (!ServerEnvironment.isMockBukkit()) {
                metrics.addCustomChart(SimplePie("message_type_" + it.name) { it.isEnabled.toString() })
            }
            messageSender.registerType(it)
        }

        registerCommands()

        updateChecker = UpdateChecker(buildProps.getProperty("spigot.resource.id").toInt(), this)
        checkForUpdates()
    }

    private fun checkCompatible(): Boolean {
        if (!ServerEnvironment.isSpigot()) {
            logger.severe("Custom Join Messages only works on Spigot, or forks of Spigot like Paper! Please upgrade to one of the two.")
            Bukkit.getPluginManager().disablePlugin(this)
            return false
        }

        if (MinecraftVersion.isOlderThan(MinecraftVersion.v1_9_0)) {
            logger.severe("Custom Join Messages only supports 1.9+ servers! Please downgrade to CJM 16 for 1.8.x support. (https://howoldisminecraft188.today/)")
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
        commandManager = PaperCommandManager(this)

        @Suppress("DEPRECATION")
        commandManager.run {
            enableUnstableAPI("help")
            enableUnstableAPI("brigadier")
        }

        CommandContext(this).register(commandManager)
        CommandCompletion(this).register(commandManager)

        val cjmCommand = CjmCommand(commandManager, this)
        cjmCommand.updateLocale()
        commandManager.registerCommand(cjmCommand)
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

    private fun checkForUpdates() {
        if (!updateChecker.isEnabled(UpdateChecker.NotificationType.CONSOLE))
            return

        updateChecker.getVersion {
            if (!it.isNewer(this))
                return@getVersion
            commandManager.getMessage(
                "cjm.update-checker.console.text",
                "%version%", it.name, "%url%", updateChecker.getResourceUrl()
            ).split("\n").forEach { msg -> logger.warning(msg) }
        }
    }

    // region MockBukkit Constructors
    constructor() : super()

    constructor(
        loader: JavaPluginLoader,
        description: PluginDescriptionFile,
        dataFolder: File,
        file: File
    ) : super(
        loader,
        description,
        dataFolder,
        file
    )
    // endregion

}
