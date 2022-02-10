package net.insprill.cjm.hooks;


import com.earth2me.essentials.Essentials;
import net.ess3.api.events.VanishStatusChangeEvent;
import net.insprill.cjm.handlers.VanishHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class EssentialsHook implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVanish(VanishStatusChangeEvent e) {
        VanishHandler.handleVanishToggle(e.getAffected().getBase(), e.getValue());
    }

    /**
     * Checks if a player is jailed.
     *
     * @param player Player to check.
     * @return True if the player is in Jail, false otherwise.
     */
    public static boolean isJailed(Player player) {
        return Essentials.getPlugin(Essentials.class).getUser(player).isJailed();
    }

}
