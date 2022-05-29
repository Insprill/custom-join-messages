package net.insprill.cjm.utils;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import net.insprill.xenlib.ColourUtils;
import net.milkbowl.vault.chat.Chat;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@UtilityClass
public class ChatUtils {

    /**
     * Set placeholders in a String.
     *
     * @param player Player to set placeholders for.
     * @param msg    String to insert placeholders on.
     * @return String with placeholder set.
     */
    public String setPlaceholders(Player player, String msg) {
        String prefix = "";
        String suffix = "";
        if (Dependency.VAULT.isEnabled() && Dependency.VAULT.getClazz() != null) {
            Chat chat = (Chat) Dependency.VAULT.getClazz();
            prefix = chat.getPlayerPrefix(player);
            suffix = chat.getPlayerSuffix(player);
        }
        msg = StringUtils.replace(msg, "%displayname%", (player.getCustomName() != null) ? player.getCustomName() : player.getDisplayName());
        msg = StringUtils.replace(msg, "%name%", player.getName());
        msg = StringUtils.replace(msg, "%uniquejoins%", "" + Bukkit.getOfflinePlayers().length);
        msg = StringUtils.replace(msg, "%prefix%", prefix);
        msg = StringUtils.replace(msg, "%suffix%", suffix);

        if (Dependency.PAPI.isEnabled()) {
            msg = ColourUtils.format(PlaceholderAPI.setPlaceholders(player, msg));
        }

        return msg;
    }

    /**
     * Set placeholders in a list of strings.
     *
     * @param player  Player to set placeholders for.
     * @param strings Strings to insert placeholders on.
     * @return String with placeholder set.
     */
    public List<String> setPlaceholders(Player player, List<String> strings) {
        strings.replaceAll(msg -> setPlaceholders(player, msg));
        return strings;
    }

}
