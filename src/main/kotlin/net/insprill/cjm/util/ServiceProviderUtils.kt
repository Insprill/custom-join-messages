package net.insprill.cjm.util

import org.bukkit.Bukkit
import org.bukkit.plugin.RegisteredServiceProvider

object ServiceProviderUtils {

    fun getRegisteredServiceProvider(clazzName: String): RegisteredServiceProvider<out Any>? {
        return try {
            val clazz = Class.forName(clazzName)
            Bukkit.getServicesManager().getRegistration(clazz)
        } catch (e: ClassNotFoundException) {
            null
        }
    }

}
