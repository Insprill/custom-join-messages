package net.insprill.cjm

import co.aikar.commands.BukkitCommandManager
import co.aikar.commands.PaperCommandManager
import de.leonhard.storage.SimplixBuilder
import de.leonhard.storage.Yaml
import de.leonhard.storage.internal.settings.ConfigSettings
import de.leonhard.storage.internal.settings.DataType
import de.leonhard.storage.internal.settings.ReloadSettings
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Collections
import kotlin.io.path.exists
import net.insprill.cjm.command.CjmCommand
import net.insprill.cjm.command.CommandCompletion
import net.insprill.cjm.command.CommandContext
import net.insprill.cjm.compatibility.Dependency
import net.insprill.cjm.compatibility.hook.HookManager
import net.insprill.cjm.compatibility.hook.PluginHook
import net.insprill.cjm.extension.getMessage
import net.insprill.cjm.formatting.Formatter
import net.insprill.cjm.formatting.FormatterType
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
import net.swiftzer.semver.SemVer
import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

open class CustomJoinMessages : JavaPlugin() {

    lateinit var messageSender: MessageSender
    lateinit var hookManager: HookManager
    lateinit var toggleHandler: ToggleHandler
    lateinit var config: Yaml
    lateinit var updateChecker: UpdateChecker
    lateinit var commandManager: BukkitCommandManager
    lateinit var worldChangeEvent: WorldChangeEvent
    lateinit var formatter: Formatter
    private lateinit var metrics: Metrics

    override fun onEnable() {
        if (!checkCompatible())
            return

        val configPath = Paths.get("$dataFolder/config.yml")
        if (!handleLegacyConfig(configPath))
            return

        var cnfgLoaded = false
        config = SimplixBuilder.fromPath(configPath)
            .addInputStreamFromResource("config.yml")
            .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
            .setDataType(DataType.SORTED)
            .reloadCallback {
                if (!cnfgLoaded) return@reloadCallback
                // Don't allow reloading from within the reload callback.
                // If we don't do this, calling #getEnum here can, in unknown cases, cause a stackoverflow.
                val prevReloadSettings = it.reloadSettings
                it.reloadSettings = ReloadSettings.MANUALLY
                var formatterType: FormatterType
                try {
                    formatterType = it.getEnum("formatting.formatter", FormatterType::class.java)
                    val formatterCompatResult = formatterType.isCompatible.invoke()
                    if (!formatterCompatResult.status) {
                        logger.severe(formatterCompatResult.message)
                        formatterType = FormatterType.LEGACY
                        logger.severe("Falling back to $formatterType")
                    }
                } finally {
                    it.reloadSettings = prevReloadSettings
                }
                this.formatter = formatterType.formatter
            }
            .createYaml()
            .addDefaultsFromInputStream()
        cnfgLoaded = true
        config.forceReload()

        if (!ServerEnvironment.isMockBukkit()) {
            metrics = Metrics(this, BuildParameters.BSTATS_ID.toInt())
            metrics.addCustomChart(SimplePie("worldBasedMessages") {
                config.getBoolean("World-Based-Messages.Enabled").toString()
            })
            metrics.addCustomChart(SimplePie("config_formatting_formatter") {
                config.getEnum("formatting.formatter", FormatterType::class.java).prettyName
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

        val platform = UpdateChecker.Platform.valueOf(BuildParameters.TARGET_PLATFORM.uppercase())
        updateChecker = platform.factory.invoke(this)
        checkForUpdates()
    }

    private fun checkCompatible(): Boolean {
        if (!ServerEnvironment.isSpigot() && !ServerEnvironment.isPaper()) {
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
        for (dependency in Dependency.entries) {
            if (!dependency.isEnabled)
                continue
            if (!dependency.isVersionCompatible(this))
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
        worldChangeEvent = WorldChangeEvent(this)
        Bukkit.getPluginManager().registerEvents(worldChangeEvent, this)

        for (listener in hookManager.vanishHooks) {
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
            if (MinecraftVersion.isOlderThan(MinecraftVersion.v1_20_6))
                enableUnstableAPI("brigadier")
        }

        CommandContext(this).register(commandManager)
        CommandCompletion(this).register(commandManager)

        val cjmCommand = CjmCommand(commandManager, this)
        cjmCommand.updateLocale()
        commandManager.registerCommand(cjmCommand)
    }

    private fun handleLegacyConfig(path: Path): Boolean {
        if (!path.exists())
            return true
        val tmpConfig = SimplixBuilder.fromPath(path).createYaml()
        val version = SemVer.parse(tmpConfig.getString("version"))
        if (version.major >= 3)
            return true
        logger.severe("It appears you've recently upgraded to CJM ${BuildParameters.VERSION} from an older version.")
        val renamedFile = File(dataFolder.parentFile, "$name-old")
        if (dataFolder.renameTo(renamedFile)) {
            logger.severe("Since the configuration has changed in layout, your old config folder has been renamed to \"$renamedFile\" and a new one has been generated.")
            logger.severe("Please manually setup the new configuration and restart your server to re-enable the plugin.")
            onEnable() // Generate new configs
        } else {
            logger.severe("Failed to rename old configuration folder. Place rename/remove it manually.")
        }
        Bukkit.getPluginManager().disablePlugin(this)
        return false
    }

    private fun checkForUpdates() {
        if (!updateChecker.isEnabled(UpdateChecker.NotificationType.CONSOLE))
            return

        updateChecker.checkForUpdates { data, _ ->
            commandManager.getMessage(
                "cjm.update-checker.console.text",
                "%version%", data.version, "%url%", updateChecker.resourceUrl
            ).split("\n").forEach { msg -> logger.warning(msg) }
        }
    }

}
