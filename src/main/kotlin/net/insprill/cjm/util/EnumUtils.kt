package net.insprill.cjm.util

import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass

object EnumUtils {

    inline fun <reified T : Enum<T>> tryGetEnum(plugin: Plugin, str: String?, type: KClass<T>): T? {
        val uppercase = str?.uppercase()
        if (uppercase == null || enumValues<T>().none { it.name == uppercase }) {
            plugin.logger.severe("Unknown ${type.simpleName} '$uppercase'! Please choose from one of the following: ${type.java.enumConstants.contentToString()}")
            return null
        }
        return enumValueOf<T>(uppercase)
    }

}
