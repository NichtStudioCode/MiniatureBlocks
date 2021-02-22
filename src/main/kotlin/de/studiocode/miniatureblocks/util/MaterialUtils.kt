package de.studiocode.miniatureblocks.util

import org.bukkit.Material
import org.bukkit.Material.AIR
import org.bukkit.block.Block
import org.bukkit.block.data.type.Slab

fun Material.isSeeTrough() = MaterialUtils.seeThroughMaterials.contains(this)

fun Material.isGlass() = MaterialUtils.glassMaterials.contains(this)

fun Block.isSlab(acceptDoubleSlabs: Boolean) = type.name.endsWith("slab", true)
        && if (acceptDoubleSlabs) true else (blockData as Slab).type != Slab.Type.DOUBLE

fun Block.isStair() = type.name.endsWith("stairs", true)

object MaterialUtils {
    
    val glassMaterials = ArrayList<Material>()
    val seeThroughMaterials = ArrayList<Material>()
    
    init {
        glassMaterials.addAll(Material.values().filter { it.name.contains("glass", true) })
    
        seeThroughMaterials.add(AIR)
        seeThroughMaterials.addAll(glassMaterials)
        seeThroughMaterials.addAll(Material.values().filter { it.name.endsWith("slab", true) })
        seeThroughMaterials.addAll(Material.values().filter { it.name.endsWith("stairs", true)})
    }
    
    
}
