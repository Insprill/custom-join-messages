package net.insprill.cjm.hooks;


import com.earth2me.essentials.Essentials;
import org.bukkit.entity.Player;

public class EssentialsHook {

    /**
     * Checks if a player is jailed.
     *
     * @param player Player to check.
     * @return True if the player is in Jail, false otherwise.
     */
    public static boolean isJailed(Player player) {
        return Essentials.getPlugin(Essentials.class).getUser(player).isJailed();
    }

}
