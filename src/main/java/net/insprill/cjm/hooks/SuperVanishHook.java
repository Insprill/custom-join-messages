package net.insprill.cjm.hooks;

import de.myzelyam.api.vanish.PlayerVanishStateChangeEvent;
import net.insprill.cjm.handlers.VanishHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SuperVanishHook implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVanishToggle(PlayerVanishStateChangeEvent e) {
        Player player = Bukkit.getPlayer(e.getUUID());
        if (player == null)
            return; // Why the hell can it return a UUID for a player that doesn't exist??
        VanishHandler.handleVanishToggle(player, e.isVanishing());
    }

}
