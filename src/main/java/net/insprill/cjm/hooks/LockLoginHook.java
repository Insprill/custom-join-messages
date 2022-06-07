package net.insprill.cjm.hooks;

import eu.locklogin.api.module.PluginModule;
import eu.locklogin.api.module.plugin.api.event.ModuleEventHandler;
import eu.locklogin.api.module.plugin.api.event.user.UserPostJoinEvent;
import eu.locklogin.api.module.plugin.api.event.util.EventListener;
import net.insprill.cjm.handlers.PlayerHandler;
import net.insprill.cjm.messages.MessageAction;
import net.insprill.cjm.messages.MessageSender;
import org.bukkit.entity.Player;

public class LockLoginHook implements EventListener {

    public static void init() {

    }

    @ModuleEventHandler(priority = ModuleEventHandler.Priority.LAST)
    public void onLogin(UserPostJoinEvent e) {
        sendMessage(e.getPlayer().getPlayer());
    }

    private void sendMessage(Player player) {
        PlayerHandler.setStatus(player.getUniqueId(), PlayerHandler.Status.LOGGED_IN);
        MessageSender.getInstance().sendMessages(player, (player.hasPlayedBefore()) ? MessageAction.JOIN : MessageAction.FIRST_JOIN, true);
    }

    public static class Module extends PluginModule {

        @Override
        public void enable() {
            getPlugin().registerListener(new LockLoginHook());
        }

        @Override
        public void disable() {
        }

    }

}
