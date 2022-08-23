package net.insprill.cjm.compatibility

import com.Zrips.CMI.CMI
import com.earth2me.essentials.Essentials
import net.insprill.cjm.utils.Dependency
import org.bukkit.entity.Player

class Jail {

    fun isJailed(player: Player): Boolean {
        return (Dependency.CMI.isEnabled && CMI.getInstance().playerManager.getUser(player).isJailed)
                && (Dependency.ESSENTIALS.isEnabled && Essentials.getPlugin(Essentials::class.java).getUser(player).isJailed)
    }

}
