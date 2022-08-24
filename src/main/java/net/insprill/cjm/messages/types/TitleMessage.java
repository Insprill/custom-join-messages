package net.insprill.cjm.messages.types;

import net.insprill.cjm.message.MessageVisibility;
import net.insprill.cjm.placeholder.Placeholders;
import net.insprill.xenlib.MinecraftVersion;
import net.insprill.xenlib.files.YamlFile;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class TitleMessage implements MessageType {

    private static final YamlFile config = new YamlFile("messages" + File.separator + "title.yml", false);

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
        return "title";
    }

    @Override
    public void handle(Player primaryPlayer, List<Player> players, String rootPath, String chosenPath, MessageVisibility visibility) {
        String title = config.getString(chosenPath + ".Title");
        String subTitle = config.getString(chosenPath + ".SubTitle");
        int fadeIn = config.getInt(chosenPath + ".Fade-In");
        int stay = config.getInt(chosenPath + ".Stay");
        int fadeOut = config.getInt(chosenPath + ".Fade-Out");

        title = Placeholders.Companion.fillPlaceholders(primaryPlayer, title);
        subTitle = Placeholders.Companion.fillPlaceholders(primaryPlayer, subTitle);

        for (Player p : players) {
            sendTitle(p, title, subTitle, fadeIn, stay, fadeOut);
        }
    }

    @SuppressWarnings("deprecation")
    private void sendTitle(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        if (MinecraftVersion.isAtLeast(MinecraftVersion.v1_9_0)) {
            player.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
        } else {
            player.sendTitle(title, subTitle);
        }
    }

}
