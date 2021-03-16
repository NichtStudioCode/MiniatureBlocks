package de.studiocode.miniatureblocks.util

import de.studiocode.miniatureblocks.resourcepack.model.part.impl.MISC_MATERIALS
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bukkit.Material

fun Material.hasSixTextures() = BlockTexture.has(this) && BlockTexture.of(this).textures.size == 6

fun Material.isTranslucent() = MaterialUtils.translucentMaterials.contains(this)

fun Material.isGlass() = MaterialUtils.glassMaterials.contains(this)

fun Material.isCrossMaterial() = MaterialUtils.crossMaterials.contains(this)

fun Material.isFlat() = MaterialUtils.flatMaterials.contains(this)

fun Material.isMiscMaterial() = MISC_MATERIALS.contains(this)

fun Material.isCarpet() = name.endsWith("CARPET")

fun Material.isPressurePlate() = name.endsWith("PRESSURE_PLATE")

fun Material.isPot() = name.startsWith("POTTED") || name == "FLOWER_POT"

fun Material.isGlassBlock() = name.endsWith("GLASS")

fun Material.isGlassPane() = name.endsWith("GLASS_PANE")

fun Material.isFence() = name.endsWith("FENCE")

fun Material.isTraversable() = isAir || name == "WATER" || name == "LAVA"

object MaterialUtils {
    
    val translucentMaterials = ArrayList<Material>()
    val glassMaterials = ArrayList<Material>()
    val crossMaterials = ArrayList<Material>()
    val flatMaterials = ArrayList<Material>()
    
    init {
        glassMaterials.addAll(Material.values().filter { it.name.endsWith("GLASS") || it.name.endsWith("GLASS_PANE") })
        flatMaterials.addAll(listOfMaterials("LADDER", "VINE", "LILY_PAD"))
        crossMaterials.addAll(Material.values().filter {
            (it.name.endsWith("SAPLING") && !it.name.startsWith("POTTED")) || it.name.contains("coral", true)
        })
        crossMaterials.addAll(listOfMaterials(
            "GRASS", "FERN", "DEAD_BUSH", "SEAGRASS", "DANDELION", "POPPY", "BLUE_ORCHID", "ALLIUM", "AZURE_BLUET",
            "RED_TULIP", "ORANGE_TULIP", "WHITE_TULIP", "OXEYE_DAISY", "CORNFLOWER", "LILY_OF_THE_VALLEY", "WITHER_ROSE",
            "CRIMSON_FUNGUS", "WARPED_FUNGUS", "BROWN_MUSHROOM", "RED_MUSHROOM", "CRIMSON_ROOTS", "WARPED_ROOTS",
            "NETHER_SPROUTS", "COBWEB", "SUGAR_CANE"
        ))
        
        translucentMaterials.addAll(glassMaterials)
        translucentMaterials.addAll(crossMaterials)
        translucentMaterials.addAll(flatMaterials)
        translucentMaterials.addAll(
            Material.values().filter {
                val name = it.name
                name.endsWith("LEAVES")
            }
        )
        translucentMaterials.addAll(listOfMaterials(
            "OAK_TRAPDOOR", "JUNGLE_TRAPDOOR", "ACACIA_TRAPDOOR", "CRIMSON_TRAPDOOR", "WARPED_TRAPDOOR", "IRON_TRAPDOOR",
            "OAK_DOOR", "JUNGLE_DOOR", "ACACIA_DOOR", "IRON_DOOR"
        ))
    }
    
    private fun listOfMaterials(vararg names: String) =
        names.mapNotNull { name -> Material.values().find { material -> name == material.name } }
    
}