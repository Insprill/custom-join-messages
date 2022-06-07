package net.insprill.cjm.messages.types;

import net.insprill.cjm.messages.MessageVisibility;
import net.insprill.xenlib.XenUtils;
import net.insprill.xenlib.files.YamlFile;
import net.insprill.xenlib.logging.Logger;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class SoundMessage implements MessageType {

    private final YamlFile config = new YamlFile("messages" + File.separator + "sound.yml", false);

    @Override
    public YamlFile getConfig() {
        return config;
    }

    @Override
    public String getKey() {
        return "Sounds";
    }

    @Override
    public String getName() {
        return "sound";
    }

    @Override
    public void handle(Player primaryPlayer, List<Player> players, String rootPath, String chosenPath, MessageVisibility visibility) {
        boolean global = config.getBoolean(chosenPath + ".Global");

        String soundString = config.getString(chosenPath + ".Sound");
        if (!XenUtils.isValidEnum(Sound.class, soundString)) {
            Logger.severe("Sound " + soundString + " doesn't exist!");
            return;
        }

        Sound sound = Sound.valueOf(soundString);
        for (Player player : players) {
            if (global) {
                primaryPlayer.getLocation().getWorld().playSound(primaryPlayer.getLocation(), sound, (float) config.getDouble(chosenPath + ".Volume"), (float) config.getDouble(chosenPath + ".Pitch"));
            } else {
                player.playSound(player.getLocation(), sound, (float) config.getDouble(chosenPath + ".Volume"), (float) config.getDouble(chosenPath + ".Pitch"));
            }
        }
    }

}
