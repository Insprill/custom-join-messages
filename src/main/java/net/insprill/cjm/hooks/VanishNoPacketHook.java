package net.insprill.cjm.hooks;

import net.insprill.cjm.handlers.VanishHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

public class VanishNoPacketHook implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVanishToggle(VanishStatusChangeEvent e) {
        VanishHandler.handleVanishToggle(e.getPlayer(), e.isVanishing());
    }

}
