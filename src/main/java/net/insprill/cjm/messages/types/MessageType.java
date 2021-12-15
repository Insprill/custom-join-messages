package net.insprill.cjm.messages.types;

import net.insprill.xenlib.files.YamlFile;
import org.bukkit.entity.Player;

import java.util.List;

public interface MessageType {

    /**
     * @return The name of the message type.
     */
    String getName();

    /**
     * @return The YamlFile associated with the message type.
     */
    YamlFile getConfig();

    /**
     * @return The primary key that all messages are under.
     */
    String getKey();

    /**
     * Called when a message should be sent.
     *
     * @param primaryPlayer Player joining/ leaving.
     * @param players       Everyone who should receive the message.
     * @param rootPath      Root path of the chosen message.
     * @param chosenPath    Full path to the chosen message.
     */
    void handle(Player primaryPlayer, List<Player> players, String rootPath, String chosenPath);

}
