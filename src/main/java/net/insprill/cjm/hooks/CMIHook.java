package net.insprill.cjm.hooks;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.commands.list.vanishedit;
import com.Zrips.CMI.events.CMIPlayerUnVanishEvent;
import com.Zrips.CMI.events.CMIPlayerVanishEvent;
import net.insprill.cjm.handlers.VanishHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CMIHook implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVanish(CMIPlayerVanishEvent e) {
        VanishHandler.handleVanishToggle(e.getPlayer(), true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVanish(CMIPlayerUnVanishEvent e) {
        VanishHandler.handleVanishToggle(e.getPlayer(), false);
    }

    /**
     * Checks if a Player is vanished via CMI.
     *
     * @param player Player to check.
     * @return True if the player is vanished, false otherwise.
     */
    public static boolean isVanished(Player player) {
        return CMI.getInstance().getPlayerManager().getUser(player).getVanish().is(vanishedit.VanishAction.isVanished);
    }

    /**
     * Checks if a player is jailed.
     *
     * @param player Player to check.
     * @return True if the player is in Jail, false otherwise.
     */
    public static boolean isJailed(Player player) {
        return CMI.getInstance().getPlayerManager().getUser(player).isJailed();
    }

}
