package net.insprill.cjm;

import net.insprill.cjm.hooks.AuthMeHook;
import net.insprill.cjm.hooks.CMIHook;
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
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CJM extends JavaPlugin {

    private static final int BSTATS_PLUGIN_ID = 6346;

    private static CJM instance;
    public static CJM getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        new Metrics(this, BSTATS_PLUGIN_ID);

        new XenLib(this);

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
