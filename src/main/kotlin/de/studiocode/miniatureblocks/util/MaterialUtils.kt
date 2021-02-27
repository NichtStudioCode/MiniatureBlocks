package de.studiocode.miniatureblocks.util

import org.bukkit.Material
import org.bukkit.Material.AIR

fun Material.isSeeTrough() = MaterialUtils.seeThroughMaterials.contains(this)

fun Material.isGlass() = MaterialUtils.glassMaterials.contains(this)

object MaterialUtils {
    
    val glassMaterials = ArrayList<Material>()
    val seeThroughMaterials = ArrayList<Material>()
    
    init {
        glassMaterials.addAll(Material.values().filter { it.name.contains("glass", true) })
        
        seeThroughMaterials.add(AIR)
        seeThroughMaterials.addAll(glassMaterials)
        seeThroughMaterials.addAll(Material.values().filter { it.name.endsWith("slab", true) })
        seeThroughMaterials.addAll(Material.values().filter { it.name.endsWith("stairs", true) })
    }
    
    
}
