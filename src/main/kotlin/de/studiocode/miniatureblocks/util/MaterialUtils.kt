package de.studiocode.miniatureblocks.util

import org.bukkit.Material
import org.bukkit.Material.*

fun Material.isSeeTrough() = MaterialUtils.seeThroughMaterials.contains(this)

fun Material.isGlass() = MaterialUtils.glassMaterials.contains(this)

fun Material.isCrossMaterial() = MaterialUtils.crossMaterials.contains(this)

object MaterialUtils {
    
    val glassMaterials = ArrayList<Material>()
    val seeThroughMaterials = ArrayList<Material>()
    val crossMaterials = ArrayList<Material>()
    
    init {
        glassMaterials.addAll(Material.values().filter { it.name.contains("glass", true) })
        crossMaterials.addAll(Material.values().filter { it.name.contains("sapling", true) || it.name.contains("coral", true) })
        crossMaterials.addAll(listOf(
            GRASS, FERN, DEAD_BUSH, SEAGRASS, DANDELION, POPPY, BLUE_ORCHID, ALLIUM, AZURE_BLUET, RED_TULIP, ORANGE_TULIP,
            WHITE_TULIP, OXEYE_DAISY, CORNFLOWER, LILY_OF_THE_VALLEY, WITHER_ROSE, CRIMSON_FUNGUS, WARPED_FUNGUS,
            BROWN_MUSHROOM, RED_MUSHROOM, CRIMSON_ROOTS, WARPED_ROOTS, NETHER_SPROUTS
        ))
        
        seeThroughMaterials.add(AIR)
        seeThroughMaterials.addAll(glassMaterials)
        seeThroughMaterials.addAll(crossMaterials)
        seeThroughMaterials.addAll(Material.values().filter { it.name.endsWith("slab", true) })
        seeThroughMaterials.addAll(Material.values().filter { it.name.endsWith("stairs", true) })
        seeThroughMaterials.addAll(Material.values().filter { it.name.endsWith("trapdoor", true) })
    }
    
    
}
