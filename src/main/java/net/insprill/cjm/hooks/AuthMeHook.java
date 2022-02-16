package net.insprill.cjm.hooks;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import net.insprill.cjm.handlers.PlayerHandler;
import net.insprill.cjm.messages.MessageAction;
import net.insprill.cjm.messages.MessageSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AuthMeHook implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(LoginEvent e) {
        sendMessage(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage("");
        if (AuthMeApi.getInstance().isUnrestricted(e.getPlayer())) {
            sendMessage(e.getPlayer());
        }
    }

    private void sendMessage(Player player) {
        PlayerHandler.setStatus(player.getUniqueId(), PlayerHandler.Status.LOGGED_IN);
        MessageSender.getInstance().sendMessages(player, (player.hasPlayedBefore()) ? MessageAction.JOIN : MessageAction.FIRST_JOIN, true);
    }

}
