package net.insprill.cjm.formatting

import net.insprill.spigotutils.MinecraftVersion
import net.insprill.spigotutils.ServerEnvironment
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.chat.ComponentSerializer

class MiniMessageFormatter : Formatter {

    private val gsonSerializer: GsonComponentSerializer? = if (isCompatible()) {
        GsonComponentSerializer.gson()
    } else {
        null
    }

    override fun format(str: String): Array<BaseComponent> {
        if (!isCompatible()) throw IllegalStateException("MiniMessageFormatter isn't compatible with this server!")
        return convertToLegacy(MiniMessage.miniMessage().deserialize(convertLegacyCodes(str)))
    }

    fun convertToLegacy(component: Component): Array<BaseComponent> {
        val json = gsonSerializer?.serialize(component)
        return ComponentSerializer.parse(json)
    }

    private fun convertLegacyCodes(str: String): String {
        var newStr = str

        LEGACY_REPLACEMENTS.forEach {
            newStr = newStr.replace("&${it.key}", it.value)
        }

        return newStr
    }

    companion object {
        val LEGACY_REPLACEMENTS = HashMap<String, String>()

        init {
            LEGACY_REPLACEMENTS["0"] = "<black>"
            LEGACY_REPLACEMENTS["1"] = "<dark_blue>"
            LEGACY_REPLACEMENTS["2"] = "<dark_green>"
            LEGACY_REPLACEMENTS["3"] = "<dark_aqua>"
            LEGACY_REPLACEMENTS["4"] = "<dark_red>"
            LEGACY_REPLACEMENTS["5"] = "<dark_purple>"
            LEGACY_REPLACEMENTS["6"] = "<gold>"
            LEGACY_REPLACEMENTS["7"] = "<gray>"
            LEGACY_REPLACEMENTS["8"] = "<dark_gray>"
            LEGACY_REPLACEMENTS["9"] = "<blue>"
            LEGACY_REPLACEMENTS["a"] = "<green>"
            LEGACY_REPLACEMENTS["b"] = "<aqua>"
            LEGACY_REPLACEMENTS["c"] = "<red>"
            LEGACY_REPLACEMENTS["d"] = "<light_purple>"
            LEGACY_REPLACEMENTS["e"] = "<yellow>"
            LEGACY_REPLACEMENTS["f"] = "<white>"
            LEGACY_REPLACEMENTS["k"] = "<magic>"
            LEGACY_REPLACEMENTS["l"] = "<bold>"
            LEGACY_REPLACEMENTS["m"] = "<strikethrough>"
            LEGACY_REPLACEMENTS["n"] = "<underline>"
            LEGACY_REPLACEMENTS["o"] = "<italic>"
            LEGACY_REPLACEMENTS["r"] = "<reset>"
        }

        fun isCompatible(): Boolean {
            return ServerEnvironment.isPaper() && MinecraftVersion.isAtLeast(MinecraftVersion.v1_18_2)
        }
    }

}
