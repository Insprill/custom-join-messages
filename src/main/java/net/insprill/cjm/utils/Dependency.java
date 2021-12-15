package net.insprill.cjm.utils;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Arrays;


public enum Dependency {

    AUTH_ME("AuthMe"),
    CMI("CMI"),
    ESSENTIALS("Essentials"),
    LOCK_LOGIN("LockLogin"),
    PAPI("PlaceholderAPI"),
    PREMIUM_VANISH("PremiumVanish"),
    SUPER_VANISH("SuperVanish"),
    VANISH_NO_PACKET("VanishNoPacket"),
    VAULT("Vault");

    private final String name;
    private Object clazz;

    Dependency(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }

    public Object getClazz() {
        return clazz;
    }

    public static void initClasses() {
        Arrays.stream(Dependency.values()).filter(Dependency::isEnabled).forEach(value -> {
            switch (value) {
                case VAULT: {
                    RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServicesManager().getRegistration(Chat.class);
                    if (chatProvider != null)
                        value.clazz = chatProvider.getProvider();
                    break;
                }
            }
        });
    }

}
