package net.insprill.cjm.listeners;

import lombok.Getter;
import net.insprill.cjm.CustomJoinMessages;
import net.insprill.cjm.message.MessageAction;
import net.insprill.xenlib.XenScheduler;
import net.insprill.xenlib.files.YamlFile;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.File;
import java.util.List;

public class WorldChangeEvent implements Listener {

    @Getter
    private static final YamlFile worldLogConfig = new YamlFile("data" + File.separator + "worlds.yml");

    private final CustomJoinMessages plugin;

    public WorldChangeEvent(CustomJoinMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeWorld(PlayerTeleportEvent e) {
        if (!YamlFile.CONFIG.getBoolean("World-Based-Messages.Enabled"))
            return;

        Location from = e.getFrom();
        Location to = e.getTo();

        if (to == null || to.getWorld() == null || from.getWorld() == null)
            return;
        if (to.getWorld().equals(from.getWorld()))
            return;

        String fromName = from.getWorld().getName();
        String toName = to.getWorld().getName();

        List<String> toPlayers = worldLogConfig.getStringList(toName);
        String uuid = e.getPlayer().getUniqueId().toString();
        boolean hasJoinedWorldBefore = toPlayers.contains(uuid);
        if (!hasJoinedWorldBefore) {
            toPlayers.add(uuid);
            worldLogConfig.set(fromName, toPlayers);
            worldLogConfig.set(toName, toPlayers);
            worldLogConfig.save();
        }

        List<String> blacklist = YamlFile.CONFIG.getStringList("World-Blacklist");
        boolean whitelist = YamlFile.CONFIG.getBoolean("World-Blacklist-As-Whitelist");
        if (isDifferentGroup(toName, fromName)) {
            if (whitelist ^ !blacklist.contains(fromName)) {
                plugin.getMessageSender().sendMessages(e.getPlayer(), MessageAction.QUIT, true);
            }
            if (whitelist ^ !blacklist.contains(toName)) {
                XenScheduler.runTaskLater(() -> plugin.getMessageSender().sendMessages(e.getPlayer(), hasJoinedWorldBefore ? MessageAction.JOIN : MessageAction.FIRST_JOIN, true), 10L);
            }
        }
    }

    private boolean isDifferentGroup(String toName, String fromName) {
        for (String key : YamlFile.CONFIG.getKeys("World-Based-Messages.Groups")) {
            List<String> group = YamlFile.CONFIG.getStringList("World-Based-Messages.Groups." + key);
            if (group.contains(toName) && group.contains(fromName)) {
                return false;
            }
        }
        return true;
    }

}
