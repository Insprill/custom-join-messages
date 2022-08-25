package net.insprill.cjm.compatibility.essentials

import com.earth2me.essentials.Essentials
import com.earth2me.essentials.User
import net.insprill.cjm.CustomJoinMessages
import net.insprill.cjm.compatibility.hook.PluginHook
import org.bukkit.entity.Player

class EssentialsHook(plugin: CustomJoinMessages) : PluginHook {

    override val authHook = null
    override val vanishHook = EssentialsVanishHook(plugin, this)
    override val jailHook = EssentialsJailHook(this)

    fun getUser(player: Player): User {
        return Essentials.getPlugin(Essentials::class.java).getUser(player)
    }

}
