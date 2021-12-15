package net.insprill.cjm.listeners;

import net.insprill.cjm.handlers.PlayerHandler;
import net.insprill.cjm.messages.MessageAction;
import net.insprill.cjm.messages.MessageSender;
import net.insprill.xenlib.files.YamlFile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage("");
        MessageSender.getInstance().sendMessages(e.getPlayer(), MessageAction.QUIT, true);
        PlayerHandler.setStatus(e.getPlayer().getUniqueId(), PlayerHandler.Status.LOGGED_OUT);
    }

}
