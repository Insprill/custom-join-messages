package net.insprill.cjm.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerHandler {

    /**
     * Gets a list of all players within a specified radius.
     *
     * @param player Source player.
     * @param radius Radius (in blocks) around player to search. Anything less than 1 will return everyone online.
     * @return A list of Player's around the source player within the specified radius.
     */
    public static List<Player> getNearbyPlayers(Player player, double radius, boolean sameWorldOnly) {
        if (radius < 1) {
            if (!sameWorldOnly) {
                return new ArrayList<>(Bukkit.getOnlinePlayers());
            }
            return player.getWorld().getPlayers().stream()
                    .filter(p -> p != player)
                    .collect(Collectors.toList());
        }
        return player.getNearbyEntities(radius, radius, radius).parallelStream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .collect(Collectors.toList());
    }

}
