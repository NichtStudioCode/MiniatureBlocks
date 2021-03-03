package de.studiocode.miniatureblocks.util

import de.studiocode.miniatureblocks.resourcepack.model.part.impl.MISC_MATERIALS
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bukkit.Material

fun Material.hasSixTextures() = BlockTexture.has(this) && BlockTexture.of(this).textures.size == 6

fun Material.isTranslucent() = MaterialUtils.translucentMaterials.contains(this)

fun Material.isCrossMaterial() = MaterialUtils.crossMaterials.contains(this)

fun Material.isMiscMaterial() = MISC_MATERIALS.contains(this)

fun Material.isCarpet() = name.endsWith("CARPET")

fun Material.isPressurePlate() = name.endsWith("PRESSURE_PLATE")

object MaterialUtils {
    
    val translucentMaterials = ArrayList<Material>()
    val crossMaterials = ArrayList<Material>()
    
    init {
        translucentMaterials.addAll(Material.values().filter { it.name.contains("glass", true) })
        crossMaterials.addAll(Material.values().filter { it.name.contains("sapling", true) || it.name.contains("coral", true) })
        crossMaterials.addAll(listOfMaterials(
            "GRASS", "FERN", "DEAD_BUSH", "SEAGRASS", "DANDELION", "POPPY", "BLUE_ORCHID", "ALLIUM", "AZURE_BLUET",
            "RED_TULIP", "ORANGE_TULIP", "WHITE_TULIP", "OXEYE_DAISY", "CORNFLOWER", "LILY_OF_THE_VALLEY", "WITHER_ROSE",
            "CRIMSON_FUNGUS", "WARPED_FUNGUS", "BROWN_MUSHROOM", "RED_MUSHROOM", "CRIMSON_ROOTS", "WARPED_ROOTS",
            "NETHER_SPROUTS", "COBWEB", "SUGAR_CANE"
        ))
    }
    
    private fun listOfMaterials(vararg names: String) =
        names.mapNotNull { name -> Material.values().find { material -> name == material.name } }
    
}
