package de.studiocode.miniatureblocks.utils

import org.bukkit.Bukkit

object WorldUtils {
    
    fun existsWorld(name: String): Boolean {
        return Bukkit.getWorld(name) != null
    }
    
}
