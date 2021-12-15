package net.insprill.cjm.handlers;

import de.myzelyam.api.vanish.VanishAPI;
import net.insprill.cjm.hooks.CMIHook;
import net.insprill.cjm.messages.MessageAction;
import net.insprill.cjm.messages.MessageSender;
import net.insprill.cjm.utils.Dependency;
import net.insprill.xenlib.files.YamlFile;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.kitteh.vanish.VanishPlugin;

public class VanishHandler {

    /**
     * Checks if a player is vanished.
     *
     * @param player Player to check.
     * @return True if the Player is vanished, false otherwise.
     */
    public static boolean isVanished(Player player) {
        if (Dependency.SUPER_VANISH.isEnabled() || Dependency.PREMIUM_VANISH.isEnabled())
            return VanishAPI.isInvisible(player);
        if (Dependency.VANISH_NO_PACKET.isEnabled())
            return VanishPlugin.getPlugin(VanishPlugin.class).getManager().isVanished(player);
        if (Dependency.CMI.isEnabled()) {
            return CMIHook.isVanished(player);
        } else {
            return player.getMetadata("vanished").stream()
                    .anyMatch(MetadataValue::asBoolean);
        }
    }

    /**
     * Handles sending fake messages when a player vanishes/ unvanishes.
     *
     * @param player      Player who vanished/ unvanished.
     * @param isVanishing Whether the player is going into vanish mode.
     */
    public static void handleVanishToggle(Player player, boolean isVanishing) {
        String path = "Addons.Vanish.Fake-Messages";
        if (!YamlFile.CONFIG.getBoolean(path + ".Enabled", true))
            return;
        if (isVanishing) {
            if (YamlFile.CONFIG.getBoolean(path + ".Vanish", true)) {
                MessageSender.getInstance().sendMessages(player, MessageAction.QUIT, false);
            }
        } else {
            if (YamlFile.CONFIG.getBoolean(path + ".Unvanish", true)) {
                MessageSender.getInstance().sendMessages(player, MessageAction.JOIN, false);
            }
        }
    }

}
