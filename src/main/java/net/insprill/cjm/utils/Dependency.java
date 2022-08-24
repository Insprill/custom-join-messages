package net.insprill.cjm.utils;

import net.insprill.cjm.compatibility.authme.AuthMeHook;
import net.insprill.cjm.compatibility.cmi.CmiHook;
import net.insprill.cjm.compatibility.essentials.EssentialsHook;
import net.insprill.cjm.compatibility.hook.PluginHook;
import net.insprill.cjm.compatibility.supervanish.SuperVanishHook;
import net.insprill.cjm.compatibility.vanishnopacket.VanishNoPacketHook;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Arrays;


public enum Dependency {

    AUTH_ME("AuthMe", AuthMeHook.class),
    CMI("CMI", CmiHook.class),
    ESSENTIALS("Essentials", EssentialsHook.class),
    LOCK_LOGIN("LockLogin"),
    PAPI("PlaceholderAPI"),
    PREMIUM_VANISH("PremiumVanish", SuperVanishHook.class),
    SUPER_VANISH("SuperVanish", SuperVanishHook.class),
    VANISH_NO_PACKET("VanishNoPacket", VanishNoPacketHook.class),
    VAULT("Vault");

    private final String pluginName;
    private final Class<? extends PluginHook> pluginHookClass;
    private Object clazz;

    Dependency(String pluginName) {
        this(pluginName, null);
    }

    Dependency(String pluginName, Class<? extends PluginHook> pluginHookClass) {
        this.pluginName = pluginName;
        this.pluginHookClass = pluginHookClass;
    }

    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled(pluginName);
    }

    public Object getClazz() {
        return clazz;
    }

    public Class<? extends PluginHook> getPluginHookClass() {
        return pluginHookClass;
    }

    public static void initClasses() {
        Arrays.stream(Dependency.values()).filter(Dependency::isEnabled).forEach(value -> {
            if (value == Dependency.VAULT) {
                RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServicesManager().getRegistration(Chat.class);
                if (chatProvider != null)
                    value.clazz = chatProvider.getProvider();
            }
        });
    }

}
