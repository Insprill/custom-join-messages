package net.insprill.cjm.messages.types;

import net.insprill.cjm.utils.CenteredMessages;
import net.insprill.cjm.utils.ChatUtils;
import net.insprill.xenlib.files.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class ChatMessage implements MessageType {

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
    public void handle(Player primaryPlayer, List<Player> players, String rootPath, String chosenPath) {
        List<String> messages = config.getStringList(chosenPath);
        messages = ChatUtils.setPlaceholders(primaryPlayer, messages);
        messages = CenteredMessages.centerMessages(messages);
        for (Player player : players) {
            for (String msg : messages) {
                if (msg.contains("{\"text\":")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:tellraw " + player.getName() + " " + msg);
                } else {
                    player.sendMessage(msg);
                }
            }
        }
    }

}
