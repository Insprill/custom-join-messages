package net.insprill.cjm.listeners;

import net.insprill.cjm.CustomJoinMessages;
import net.insprill.cjm.message.MessageAction;
import net.insprill.xenlib.XenScheduler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    private final CustomJoinMessages plugin;

    public JoinEvent(CustomJoinMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage("");
        XenScheduler.runTaskLater(() -> {
            plugin.getMessageSender().sendMessages(e.getPlayer(), e.getPlayer().hasPlayedBefore() ? MessageAction.JOIN : MessageAction.FIRST_JOIN, true);
        }, 10L);
    }

}
