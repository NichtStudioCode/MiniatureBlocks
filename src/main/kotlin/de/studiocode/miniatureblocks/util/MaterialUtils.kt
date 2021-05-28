package de.studiocode.miniatureblocks.util

import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bukkit.Material

fun Material.hasSixTextures() = BlockTexture.has(this) && BlockTexture.of(this).textures.size == 6

fun Material.isTranslucent() = MaterialUtils.translucentMaterials.contains(this)

fun Material.isGlass() = MaterialUtils.glassMaterials.contains(this)

fun Material.isFlat() = MaterialUtils.flatMaterials.contains(this)

fun Material.isPot() = name.startsWith("POTTED") || name == "FLOWER_POT"

fun Material.isFence() = name.endsWith("FENCE")

fun Material.isWall() = name.endsWith("WALL")

fun Material.isTraversable() = isAir || name == "WATER" || name == "LAVA"

fun Material.isFluid() =  name == "WATER" || name == "LAVA"

fun Material.isHead() = name.endsWith("HEAD") || name.endsWith("SKULL")

fun Material.isBeaconBase() = MaterialUtils.beaconBaseMaterials.contains(this)

fun Material.isCrop() = MaterialUtils.cropMaterials.contains(this)

object MaterialUtils {
    
    val translucentMaterials = ArrayList<Material>()
    val glassMaterials = ArrayList<Material>()
    val flatMaterials = ArrayList<Material>()
    val beaconBaseMaterials = listOfMaterials("IRON_BLOCK", "GOLD_BLOCK", "DIAMOND_BLOCK", "NETHERITE_BLOCK", "EMERALD_BLOCK")
    val cropMaterials = listOfMaterials("BEETROOTS", "CARROTS", "NETHER_WART", "POTATOES", "WHEAT", "SWEET_BERRY_BUSH")
    
    init {
        glassMaterials.addAll(Material.values().filter { it.name.endsWith("GLASS") || it.name.endsWith("GLASS_PANE") })
        flatMaterials.addAll(listOfMaterials("LADDER", "VINE", "LILY_PAD"))
        
        translucentMaterials.addAll(glassMaterials)
        translucentMaterials.addAll(flatMaterials)
        translucentMaterials.addAll(
            Material.values().filter {
                val name = it.name
                name.endsWith("LEAVES") 
                    || (it.name.contains("coral", true) && !it.name.endsWith("BLOCK"))
            }
        )
        translucentMaterials.addAll(listOfMaterials(
            "GRASS", "FERN", "DEAD_BUSH", "SEAGRASS", "DANDELION", "POPPY", "BLUE_ORCHID", "ALLIUM", "AZURE_BLUET",
            "RED_TULIP", "ORANGE_TULIP", "WHITE_TULIP", "OXEYE_DAISY", "CORNFLOWER", "LILY_OF_THE_VALLEY", "WITHER_ROSE",
            "CRIMSON_FUNGUS", "WARPED_FUNGUS", "BROWN_MUSHROOM", "RED_MUSHROOM", "CRIMSON_ROOTS", "WARPED_ROOTS",
            "NETHER_SPROUTS", "COBWEB", "SUGAR_CANE", "OAK_TRAPDOOR", "JUNGLE_TRAPDOOR", "ACACIA_TRAPDOOR",
            "CRIMSON_TRAPDOOR", "WARPED_TRAPDOOR", "IRON_TRAPDOOR", "OAK_DOOR", "JUNGLE_DOOR", "ACACIA_DOOR",
            "IRON_DOOR", "BEACON", "REDSTONE_WIRE", "ICE", "SLIME_BLOCK", "HONEY_BLOCK"
        ))
    }
    
    private fun listOfMaterials(vararg names: String) =
        names.mapNotNull { name -> Material.values().find { material -> name == material.name } }
    
}