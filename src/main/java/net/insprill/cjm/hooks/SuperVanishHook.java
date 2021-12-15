package net.insprill.cjm.hooks;

import de.myzelyam.api.vanish.PlayerVanishStateChangeEvent;
import net.insprill.cjm.handlers.VanishHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SuperVanishHook implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVanishToggle(PlayerVanishStateChangeEvent e) {
        VanishHandler.handleVanishToggle(Bukkit.getPlayer(e.getUUID()), e.isVanishing());
    }

}
