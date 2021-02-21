package de.studiocode.miniatureblocks.util

import org.bukkit.Bukkit

object WorldUtils {
    
    fun existsWorld(name: String): Boolean {
        return Bukkit.getWorld(name) != null
    }
    
}
