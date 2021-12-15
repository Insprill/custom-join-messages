package net.insprill.cjm.handlers;

import net.insprill.cjm.hooks.CMIHook;
import net.insprill.cjm.hooks.EssentialsHook;
import net.insprill.cjm.utils.Dependency;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

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
     * Checks if a player is logged in or not.
     *
     * @param uuid UUID of player to check.
     * @return True if the player has been marked as logged in, false otherwise.
     */
    public static boolean isPlayerLoggedIn(UUID uuid) {
        return loggedInPlayers.contains(uuid);
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
            String playerWorld = StringUtils.substringBefore(player.getWorld().getName(), "_");
            return Bukkit.getOnlinePlayers().stream()
                    .filter(p -> playerWorld.equals(StringUtils.substringBefore(p.getWorld().getName(), "_")))
                    .collect(Collectors.toList());
        }
        return player.getNearbyEntities(radius, radius, radius).parallelStream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a player has a permission.
     *
     * @param player     Player to check.
     * @param permission Permission to check.
     * @return True if the player has the permission, or the permission is null/empty. False otherwise.
     */
    public static boolean hasPermission(Player player, @Nullable String permission) {
        if (permission == null || permission.isEmpty())
            return true;
        return player.hasPermission(permission);
    }

    /**
     * Checks if a player is jailed.
     *
     * @param player Player to check.
     * @return True if the player is in Jail, false otherwise.
     */
    public static boolean isJailed(Player player) {
        if (Dependency.CMI.isEnabled()) {
            return CMIHook.isJailed(player);
        } else if (Dependency.ESSENTIALS.isEnabled()) {
            return EssentialsHook.isJailed(player);
        }
        return false;
    }

    public enum Status {
        LOGGED_IN,
        LOGGED_OUT
    }

}
