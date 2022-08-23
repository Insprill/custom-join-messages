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

    private static final Set<UUID> loggedInPlayers = new HashSet<>();

    /**
     * Marks a player as logged in.
     *
     * @param uuid   UUID of the Player to set status for.
     * @param status Status to set.
     */
    public static void setStatus(UUID uuid, Status status) {
        if (status == Status.LOGGED_IN) {
            loggedInPlayers.add(uuid);
        } else if (status == Status.LOGGED_OUT) {
            loggedInPlayers.remove(uuid);
        }
    }

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

    public enum Status {
        LOGGED_IN,
        LOGGED_OUT
    }

}
