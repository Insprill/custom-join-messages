package net.insprill.cjm.formatting

import net.insprill.spigotutils.MinecraftVersion
import net.insprill.spigotutils.ServerEnvironment
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.chat.ComponentSerializer
import org.bukkit.Bukkit

class MiniMessageFormatter : Formatter {

    private val gsonSerializer: GsonComponentSerializer?

    init {
        if (isCompatible()) {
            @Suppress("DEPRECATION")
            val unsafe = Bukkit.getUnsafe()
            val clazz = unsafe::class.java
            gsonSerializer = clazz.getMethod("gsonComponentSerializer").invoke(unsafe) as GsonComponentSerializer
        } else {
            gsonSerializer = null
        }
    }

    override fun format(str: String): Array<BaseComponent> {
        if (!isCompatible()) throw IllegalStateException("MiniMessageFormatter isn't compatible with this server!")
        val json = gsonSerializer?.serialize(MiniMessage.miniMessage().deserialize(str))
        return ComponentSerializer.parse(json)
    }

    companion object {
        fun isCompatible(): Boolean {
            return ServerEnvironment.isPaper() && MinecraftVersion.isAtLeast(MinecraftVersion.v1_18_2)
        }
    }

}
