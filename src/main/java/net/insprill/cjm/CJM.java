package net.insprill.cjm;

import lombok.Getter;
import net.insprill.cjm.hooks.AuthMeHook;
import net.insprill.cjm.hooks.CMIHook;
import net.insprill.cjm.hooks.EssentialsHook;
import net.insprill.cjm.hooks.SuperVanishHook;
import net.insprill.cjm.hooks.VanishNoPacketHook;
import net.insprill.cjm.listeners.JoinEvent;
import net.insprill.cjm.listeners.QuitEvent;
import net.insprill.cjm.listeners.WorldChangeEvent;
import net.insprill.cjm.messages.MessageSender;
import net.insprill.cjm.utils.Dependency;
import net.insprill.xenlib.XenLib;
import net.insprill.xenlib.commands.Command;
import net.insprill.xenlib.files.YamlFile;
import net.insprill.xenlib.files.YamlFolder;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CJM extends JavaPlugin {

    private static final int BSTATS_PLUGIN_ID = 6346;
    private static final String CONFIG_MAJOR_VERSION = "3";

    @Getter
    private static CJM instance;

    @Getter
    private Metrics metrics;

    @Override
    public void onEnable() {
        instance = this;

        metrics = new Metrics(this, BSTATS_PLUGIN_ID);
        metrics.addCustomChart(new SimplePie("worldBasedMessages", () -> YamlFile.CONFIG.getBoolean("World-Based-Messages.Enabled") + ""));

        XenLib.init(this);

        String version = YamlFile.CONFIG.getString("version");
        if (!version.split("\\.")[0].equals(CONFIG_MAJOR_VERSION)) {
            String newFolderName = getDataFolder().getName() + "-old";
            getDataFolder().renameTo(new File(getDataFolder().getParentFile(), getDataFolder().getName() + "-old"));
            YamlFile.CONFIG.reload();
            YamlFolder.LOCALE.reload();
            getLogger().warning("Your CJM config is outdated. It has been renamed to '" + newFolderName + "' and a new one has been generated.");
        }

        Dependency.initClasses();

        Bukkit.getPluginManager().registerEvents(new QuitEvent(), this);
        Bukkit.getPluginManager().registerEvents(new WorldChangeEvent(), this);

        if (Dependency.AUTH_ME.isEnabled() && YamlFile.CONFIG.getBoolean("Addons.AuthMe.Use-Login-Event")) {
            Bukkit.getPluginManager().registerEvents(new AuthMeHook(), this);
        } else {
            Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        }
        if (YamlFile.CONFIG.getBoolean("Addons.Vanish.Fake-Messages.Enabled")) {
            if (Dependency.CMI.isEnabled())
                Bukkit.getPluginManager().registerEvents(new CMIHook(), this);
            if (Dependency.SUPER_VANISH.isEnabled())
                Bukkit.getPluginManager().registerEvents(new SuperVanishHook(), this);
            if (Dependency.VANISH_NO_PACKET.isEnabled())
                Bukkit.getPluginManager().registerEvents(new VanishNoPacketHook(), this);
            if (Dependency.ESSENTIALS.isEnabled())
                Bukkit.getPluginManager().registerEvents(new EssentialsHook(), this);
        }

        new Command("cjm", "net.insprill.cjm.commands");
        new MessageSender();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        WorldChangeEvent.getWorldLogConfig().save();
    }

}
