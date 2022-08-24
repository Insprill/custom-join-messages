package net.insprill.cjm.messages.types;

import net.insprill.cjm.message.MessageVisibility;
import net.insprill.cjm.utils.ChatUtils;
import net.insprill.xenlib.CenteredMessages;
import net.insprill.xenlib.files.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class ChatMessage implements MessageType {

    private static final String CENTER_PREFIX = "center:";

    private final YamlFile config = new YamlFile("messages" + File.separator + "chat.yml", false);

    @Override
    public YamlFile getConfig() {
        return config;
    }

    @Override
    public String getKey() {
        return "Messages";
    }

    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public void handle(Player primaryPlayer, List<Player> players, String rootPath, String chosenPath, MessageVisibility visibility) {
        List<String> messages = config.getStringList(chosenPath);
        ChatUtils.setPlaceholders(primaryPlayer, messages);
        messages.replaceAll(s -> (s.startsWith(CENTER_PREFIX))
                ? CenteredMessages.centerMessage(s.substring(CENTER_PREFIX.length()))
                : s);
        for (String msg : messages) {
            boolean isJson = msg.contains("{\"text\":");
            for (Player player : players) {
                if (isJson) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:tellraw " + player.getName() + " " + msg);
                } else {
                    player.sendMessage(msg);
                }
            }
            if (visibility == MessageVisibility.PUBLIC && !isJson) {
                Bukkit.getConsoleSender().sendMessage(msg);
            }
        }
    }

}
