package de.studiocode.miniatureblocks.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

object WorldUtils {

    fun existsWorld(name: String): Boolean {
        return Bukkit.getWorld(name) != null
    }
    
}
