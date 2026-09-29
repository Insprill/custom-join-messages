package net.insprill.cjm.util

import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault

object PermissionUtil {
    fun registerIfMissing(perm: String, default: PermissionDefault) {
        val pm = Bukkit.getPluginManager()
        if (pm.getPermission(perm) == null) {
            pm.addPermission(Permission(perm, default))
        }
    }
}
