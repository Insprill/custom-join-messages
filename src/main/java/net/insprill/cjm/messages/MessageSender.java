package net.insprill.cjm.messages;

import com.google.common.reflect.ClassPath;
import lombok.Getter;
import net.insprill.cjm.handlers.PlayerHandler;
import net.insprill.cjm.handlers.RandomHandler;
import net.insprill.cjm.handlers.VanishHandler;
import net.insprill.cjm.messages.types.MessageType;
import net.insprill.xenlib.XenLib;
import net.insprill.xenlib.XenMath;
import net.insprill.xenlib.files.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MessageSender implements Listener {

    private static MessageSender instance;

    public static MessageSender getInstance() {
        return instance;
    }

    @Getter
    private final Map<String, MessageType> messageTypes = new HashMap<>();

    @SuppressWarnings("UnstableApiUsage")
    public MessageSender() {
        if (instance == null)
            instance = this;

        try {
            ClassPath.from(XenLib.getPlugin().getClass().getClassLoader())
                    .getAllClasses()
                    .parallelStream()
                    .filter(clazz -> clazz.getPackageName().equalsIgnoreCase("net.insprill.cjm.messages.types"))
                    .map(ClassPath.ClassInfo::load)
                    .filter(MessageType.class::isAssignableFrom)
                    .filter(c -> c != MessageType.class)
                    .map(clazz -> {
                        try {
                            return clazz.getConstructor().newInstance();
                        } catch (ReflectiveOperationException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .forEach(message -> messageTypes.put(((MessageType) message).getName(), (MessageType) message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles sending messages for a player.
     *
     * @param player      Player who joined/ left.
     * @param action      Action the player performed.
     * @param vanishCheck Whether to check if the player is vanished before sending messages.
     */
    public void sendMessages(Player player, MessageAction action, boolean vanishCheck) {
        if (action == MessageAction.QUIT && !PlayerHandler.isPlayerLoggedIn(player.getUniqueId()))
            return;
        if (vanishCheck && VanishHandler.isVanished(player))
            return;
        if (!YamlFile.CONFIG.getBoolean("Addons.Jail.Send-Messages-For-Jailed-Players") && PlayerHandler.isJailed(player))
            return;

        for (MessageVisibility visibility : MessageVisibility.values()) {
            if (action == MessageAction.QUIT && visibility == MessageVisibility.PRIVATE)
                continue;

            for (MessageType msg : messageTypes.values()) {
                String path = visibility.getConfigSection() + "." + action.getConfigSection();

                // Get the highest priority message the player has access to.
                int hp = msg.getConfig().getKeys(path).stream()
                        .filter(XenMath::isInteger)
                        .filter(num -> PlayerHandler.hasPermission(player, msg.getConfig().getString(path + "." + num + ".Permission")))
                        .mapToInt(Integer::parseInt)
                        .max()
                        .orElse(-1);
                if (hp == -1)
                    continue;

                String messagePath = path + "." + hp;

                int maxPlayers = msg.getConfig().getInt(messagePath + ".Max-Players");
                if (maxPlayers > 0 && Bukkit.getOnlinePlayers().size() > maxPlayers)
                    continue;

                double radius = msg.getConfig().getDouble(messagePath + ".Radius");
                List<Player> players = (visibility == MessageVisibility.PUBLIC)
                        ? PlayerHandler.getNearbyPlayers(player, radius, YamlFile.CONFIG.getBoolean("World-Based"))
                        : Collections.singletonList(player);

                if (action == MessageAction.QUIT && YamlFile.CONFIG.getBoolean("World-Based")) {
                    players.remove(player);
                }

                String randomKey = RandomHandler.getRandomKey(msg.getConfig(), messagePath + "." + msg.getKey());
                if (randomKey == null)
                    continue;

                msg.handle(player, players, path, randomKey);
            }
        }
    }

}
