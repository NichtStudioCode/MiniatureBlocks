package de.studiocode.miniatureblocks.utils

import org.bukkit.Material

fun Material.isSeeTrough(): Boolean {
    return MaterialUtils.seeThroughMaterials.contains(this)
}

object MaterialUtils {
    
    val seeThroughMaterials = ArrayList<Material>()
    
    init {
        seeThroughMaterials.addAll(Material.values().filter { it.toString().contains("glass", true) })
        seeThroughMaterials.add(Material.AIR)
    }
    
    
}
