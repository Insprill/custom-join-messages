package net.insprill.cjm.listeners;

import lombok.Getter;
import net.insprill.cjm.messages.MessageAction;
import net.insprill.cjm.messages.MessageSender;
import net.insprill.xenlib.XenScheduler;
import net.insprill.xenlib.files.YamlFile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.File;
import java.util.List;

public class WorldChangeEvent implements Listener {

    @Getter
    private static final YamlFile worldLogConfig = new YamlFile("data" + File.separator + "worlds.yml");

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeWorld(PlayerTeleportEvent e) {
        if (!YamlFile.CONFIG.getBoolean("World-Based-Messages.Enabled"))
            return;

        String fromFullName = e.getFrom().getWorld().getName();
        String toFullName = e.getTo().getWorld().getName();

        String fromName = (fromFullName.indexOf('_') == -1) ? fromFullName : fromFullName.substring(0, fromFullName.indexOf('_'));
        String toName = (toFullName.indexOf('_') == -1) ? toFullName : toFullName.substring(0, toFullName.indexOf('_'));

        List<String> players = worldLogConfig.getStringList(toName);
        String uuid = e.getPlayer().getUniqueId().toString();
        boolean hasJoinedWorldBefore = players.contains(uuid);
        if (!hasJoinedWorldBefore) {
            players.add(uuid);
            worldLogConfig.set(toName, players);
            worldLogConfig.save();
        }

        List<String> blacklist = YamlFile.CONFIG.getStringList("World-Blacklist");
        boolean whitelist = YamlFile.CONFIG.getBoolean("World-Blacklist-As-Whitelist");
        if (!fromName.equals(toName)) {
            if (whitelist ^ !blacklist.contains(fromName)) {
                MessageSender.getInstance().sendMessages(e.getPlayer(), MessageAction.QUIT, true);
            }
            if (whitelist ^ !blacklist.contains(toName)) {
                XenScheduler.runTaskLater(() -> MessageSender.getInstance().sendMessages(e.getPlayer(), hasJoinedWorldBefore ? MessageAction.JOIN : MessageAction.FIRST_JOIN, true), 10L);
            }
        }
    }

}
