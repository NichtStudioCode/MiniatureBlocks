package de.studiocode.miniatureblocks.resourcepack.texture

import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.Direction.NORTH
import de.studiocode.miniatureblocks.resourcepack.model.Direction.UP
import de.studiocode.miniatureblocks.storage.PermanentStorage
import org.bukkit.Material
import java.util.*
import kotlin.collections.HashSet

class BlockTexture {
    
    val material: Material?
    val defaultRotation: Direction
    val textures: Array<String>
    
    constructor(material: String, textures: Array<String>, defaultRotation: Direction = NORTH) {
        this.material = findMaterialByName(material)
        this.defaultRotation = defaultRotation
        this.textures = textures
    }
    
    constructor(material: String, textureTop: String, textureBottom: String, textureFront: String, textureBack: String,
                textureRight: String, textureLeft: String, defaultRotation: Direction = NORTH, prependBlock: Boolean = true) {
        
        this.material = findMaterialByName(material)
        if (this.material == null) println("Couldn't find a material for $material")
        this.defaultRotation = defaultRotation
        
        val textureUp = (if (prependBlock) "block/" else "") + textureTop
        val textureDown = (if (prependBlock) "block/" else "") + textureBottom
        val textureNorth = (if (prependBlock) "block/" else "") + textureFront
        val textureSouth = (if (prependBlock) "block/" else "") + textureBack
        val textureEast = (if (prependBlock) "block/" else "") + textureRight
        val textureWest = (if (prependBlock) "block/" else "") + textureLeft
        
        textures = arrayOf(textureNorth, textureEast, textureSouth, textureWest, textureUp, textureDown)
    }
    
    constructor(material: String, textureTop: String, textureBottom: String, textureFront: String, textureSide: String, defaultRotation: Direction = NORTH) :
        this(material, textureTop, textureBottom, textureFront, textureSide, textureSide, textureSide, defaultRotation)
    
    constructor(material: String, textureTop: String, textureBottom: String, textureSide: String, defaultRotation: Direction = NORTH) :
        this(material, textureTop, textureBottom, textureSide, textureSide, defaultRotation)
    
    constructor(material: String, textureTopBottom: String, textureSide: String, defaultRotation: Direction = NORTH) :
        this(material, textureTopBottom, textureTopBottom, textureSide, defaultRotation)
    
    constructor(material: String, texture: String, defaultRotation: Direction = NORTH) :
        this(material, texture, texture, defaultRotation)
    
    constructor(material: String, defaultRotation: Direction = NORTH) :
        this(material, material.toLowerCase(), defaultRotation)
    
    private constructor(material: String, top: Boolean, bottom: Boolean, front: Boolean, defaultRotation: Direction = NORTH) {
        this.material = findMaterialByName(material)
        this.defaultRotation = defaultRotation
        
        val texture = "block/" + material.toLowerCase()
        val textureUp = texture + if (top) "_top" else ""
        val textureDown = texture + if (bottom) "_bottom" else if (top) "_top" else ""
        val textureNorth = texture + if (front) "_front" else "_side"
        val textureSide = texture + "_side"
        
        textures = arrayOf(textureNorth, textureSide, textureSide, textureSide, textureUp, textureDown)
    }
    
    fun getTexture(direction: Direction) = textures[direction.ordinal]
    
    fun copyOfChange(direction: Direction, textureName: String, prependBlock: Boolean = false): BlockTexture {
        val textureLocation = (if (prependBlock) "block/" else "") + textureName
        val texturesCopy = textures.copyOf().also { it[direction.ordinal] = textureLocation }
        return BlockTexture(material?.name ?: "", texturesCopy, defaultRotation)
    }
    
    override fun equals(other: Any?): Boolean {
        return if (other is BlockTexture) {
            material == other.material
                && defaultRotation == other.defaultRotation
                && textures.contentEquals(other.textures)
        } else this === other
    }
    
    override fun hashCode(): Int {
        var result = 3
        result = 31 * result + (material?.hashCode() ?: 0)
        result = 31 * result + defaultRotation.hashCode()
        result = 31 * result + textures.contentHashCode()
        return result
    }
    
    private fun findMaterialByName(name: String): Material? {
        return Material.values().find { it.name == name }
    }
    
    companion object {
        
        private val blockTextures: EnumMap<Material, BlockTexture> = EnumMap(Material::class.java)
        
        @Suppress("ReplaceWithEnumMap")
        private val textureOverrides = PermanentStorage.retrieve(HashMap<Material, BlockTexture>(), "textureOverrides")
        
        private val defaultTextureLocations: HashSet<String>
        private val customTextureLocations: ArrayList<String>
        val textureLocations: HashSet<String>
        val supportedMaterials: TreeSet<Material>
        
        init {
            createBlockTextures()
                .filter { it.material != null }
                .forEach { blockTextures[it.material!!] = it }
            
            supportedMaterials = TreeSet(blockTextures.keys)
            
            defaultTextureLocations = blockTextures.values
                .flatMap { it.textures.toList() }
                .toHashSet()
            
            customTextureLocations = PermanentStorage.retrieve(ArrayList(), "customTextures")
            textureLocations = HashSet(defaultTextureLocations)
            textureLocations.addAll(customTextureLocations)
        }
        
        fun addTextureLocation(location: String) {
            textureLocations.add(location)
            customTextureLocations.add(location)
            
            PermanentStorage.store("customTextures", customTextureLocations)
        }
        
        fun removeTextureLocation(location: String): Boolean {
            if (!defaultTextureLocations.contains(location)) {
                if (customTextureLocations.remove(location)) {
                    PermanentStorage.store("customTextures", customTextureLocations)
                    textureLocations.remove(location)
                    return true
                }
            }
            return false
        }
        
        fun has(material: Material) = blockTextures.containsKey(material)
        
        fun of(material: Material): BlockTexture {
            return textureOverrides[material] ?: blockTextures[material]!!
        }
        
        fun changeTextures(material: Material, run: (BlockTexture) -> BlockTexture) {
            val defaultTexture = blockTextures[material]!!
            val overrideTexture = textureOverrides[material]
            val currentTexture = overrideTexture ?: defaultTexture
            
            val newTexture = run(currentTexture)
            if (newTexture == defaultTexture) {
                textureOverrides.remove(material)
            } else textureOverrides[material] = newTexture
            
            PermanentStorage.store("textureOverrides", textureOverrides)
        }
        
        private fun createBlockTextures(): HashSet<BlockTexture> {
            return hashSetOf(
                BlockTexture("ACACIA_LOG", "acacia_log_top", "acacia_log", UP),
                BlockTexture("ACACIA_PLANKS"),
                BlockTexture("ACACIA_WOOD", "acacia_log", UP),
                BlockTexture("ANCIENT_DEBRIS", "ancient_debris_top", "ancient_debris_side"),
                BlockTexture("ANDESITE"),
                BlockTexture("BARREL", "barrel_top", "barrel_bottom", "barrel_side"),
                BlockTexture("BASALT", "basalt_top", "basalt_side", UP),
                BlockTexture("BEACON"),
                BlockTexture("BEDROCK"),
                BlockTexture("BEEHIVE", "beehive_end", "beehive_end", "beehive_front", "beehive_side"),
                BlockTexture("BEE_NEST", "bee_nest_top", "bee_nest_bottom", "bee_nest_front", "bee_nest_side"),
                BlockTexture("BIRCH_LOG", "birch_log_top", "birch_log", UP),
                BlockTexture("BIRCH_PLANKS"),
                BlockTexture("BIRCH_WOOD", "birch_log", UP),
                BlockTexture("BLACKSTONE", "blackstone_top", "blackstone"),
                BlockTexture("BLACK_CONCRETE"),
                BlockTexture("BLACK_GLAZED_TERRACOTTA"),
                BlockTexture("BLACK_SHULKER_BOX"), // has multiple textures in-game but i could only find one texture in the assets
                BlockTexture("BLACK_STAINED_GLASS"),
                BlockTexture("BLACK_TERRACOTTA"),
                BlockTexture("BLACK_WOOL"),
                BlockTexture("BLAST_FURNACE", top = true, bottom = false, front = true),
                BlockTexture("BLUE_CONCRETE"),
                BlockTexture("BLUE_GLAZED_TERRACOTTA"),
                BlockTexture("BLUE_ICE"),
                BlockTexture("BLUE_SHULKER_BOX"),
                BlockTexture("BLUE_STAINED_GLASS"),
                BlockTexture("BLUE_TERRACOTTA"),
                BlockTexture("BLUE_WOOL"),
                BlockTexture("BONE_BLOCK", top = true, bottom = false, front = false, defaultRotation = UP),
                BlockTexture("BOOKSHELF", "oak_planks", "bookshelf"),
                BlockTexture("BRAIN_CORAL_BLOCK"),
                BlockTexture("BRICKS"),
                BlockTexture("BROWN_CONCRETE"),
                BlockTexture("BROWN_GLAZED_TERRACOTTA"),
                BlockTexture("BROWN_MUSHROOM_BLOCK"),
                BlockTexture("BROWN_SHULKER_BOX"),
                BlockTexture("BROWN_STAINED_GLASS"),
                BlockTexture("BROWN_TERRACOTTA"),
                BlockTexture("BROWN_WOOL"),
                BlockTexture("BUBBLE_CORAL_BLOCK"),
                BlockTexture("CARTOGRAPHY_TABLE", "cartography_table_top", "dark_oak_planks", "cartography_table_side1", "cartography_table_side3", "cartography_table_side3", "cartography_table_side2"),
                BlockTexture("CARVED_PUMPKIN", "pumpkin_top", "pumpkin_top", "carved_pumpkin", "pumpkin_side"),
                BlockTexture("CHISELED_NETHER_BRICKS"),
                BlockTexture("CHISELED_POLISHED_BLACKSTONE"),
                BlockTexture("CHISELED_QUARTZ_BLOCK", "chiseled_quartz_block_top", "chiseled_quartz_block"),
                BlockTexture("CHISELED_RED_SANDSTONE"),
                BlockTexture("CHISELED_SANDSTONE"),
                BlockTexture("CHISELED_STONE_BRICKS"),
                BlockTexture("CLAY"),
                BlockTexture("COAL_BLOCK"),
                BlockTexture("COAL_ORE"),
                BlockTexture("COARSE_DIRT"),
                BlockTexture("COBBLESTONE"),
                BlockTexture("CRACKED_NETHER_BRICKS"),
                BlockTexture("CRACKED_POLISHED_BLACKSTONE_BRICKS"),
                BlockTexture("CRACKED_STONE_BRICKS"),
                BlockTexture("CRAFTING_TABLE", "crafting_table_top", "oak_planks", "crafting_table_front", "crafting_table_side"),
                BlockTexture("CRIMSON_HYPHAE", "crimson_stem"),
                BlockTexture("CRIMSON_NYLIUM", "crimson_nylium", "netherrack", "crimson_nylium_side"),
                BlockTexture("CRIMSON_PLANKS"),
                BlockTexture("CRIMSON_STEM", "crimson_stem_top", "crimson_stem_top", "crimson_stem", UP),
                BlockTexture("CRYING_OBSIDIAN"),
                BlockTexture("CUT_RED_SANDSTONE", "red_sandstone_top", "red_sandstone_top", "cut_red_sandstone"),
                BlockTexture("CUT_SANDSTONE", "sandstone_top", "sandstone_top", "cut_sandstone"),
                BlockTexture("CYAN_CONCRETE"),
                BlockTexture("CYAN_GLAZED_TERRACOTTA"),
                BlockTexture("CYAN_SHULKER_BOX"),
                BlockTexture("CYAN_STAINED_GLASS"),
                BlockTexture("CYAN_TERRACOTTA"),
                BlockTexture("CYAN_WOOL"),
                BlockTexture("DARK_OAK_LOG", "dark_oak_log_top", "dark_oak_log", UP),
                BlockTexture("DARK_OAK_PLANKS"),
                BlockTexture("DARK_OAK_WOOD", "dark_oak_log", UP),
                BlockTexture("DARK_PRISMARINE"),
                BlockTexture("DEAD_BRAIN_CORAL_BLOCK"),
                BlockTexture("DEAD_BUBBLE_CORAL_BLOCK"),
                BlockTexture("DEAD_FIRE_CORAL_BLOCK"),
                BlockTexture("DEAD_HORN_CORAL_BLOCK"),
                BlockTexture("DEAD_TUBE_CORAL_BLOCK"),
                BlockTexture("DIAMOND_BLOCK"),
                BlockTexture("DIAMOND_ORE"),
                BlockTexture("DIORITE"),
                BlockTexture("DIRT"),
                BlockTexture("DISPENSER", "furnace_top", "furnace_top", "dispenser_front", "furnace_side"),
                BlockTexture("DRIED_KELP_BLOCK", "dried_kelp_top", "dried_kelp_bottom", "dried_kelp_side"),
                BlockTexture("DROPPER", "furnace_top", "furnace_top", "dropper_front", "furnace_side"),
                BlockTexture("EMERALD_BLOCK"),
                BlockTexture("EMERALD_ORE"),
                BlockTexture("END_STONE"),
                BlockTexture("END_STONE_BRICKS"),
                BlockTexture("FARMLAND", "farmland_moist", "dirt", "dirt"),
                BlockTexture("FIRE_CORAL_BLOCK"),
                BlockTexture("FLETCHING_TABLE", "fletching_table_top", "birch_planks", "fletching_table_front", "fletching_table_front", "fletching_table_side", "fletching_table_side"),
                BlockTexture("FURNACE", "furnace_top", "furnace_top", "furnace_front", "furnace_side"),
                BlockTexture("GILDED_BLACKSTONE"),
                BlockTexture("GLASS"),
                BlockTexture("GLOWSTONE"),
                BlockTexture("GOLD_BLOCK"),
                BlockTexture("GOLD_ORE"),
                BlockTexture("GRANITE"),
                BlockTexture("GRASS_BLOCK", "modded/grass_block_top", "dirt", "grass_block_side"),
                BlockTexture("GRASS_PATH", "grass_path_top", "dirt", "grass_path_side"),
                BlockTexture("GRAY_CONCRETE"),
                BlockTexture("GRAY_GLAZED_TERRACOTTA"),
                BlockTexture("GRAY_SHULKER_BOX"),
                BlockTexture("GRAY_STAINED_GLASS"),
                BlockTexture("GRAY_TERRACOTTA"),
                BlockTexture("GRAY_WOOL"),
                BlockTexture("GREEN_CONCRETE"),
                BlockTexture("GREEN_GLAZED_TERRACOTTA"),
                BlockTexture("GREEN_SHULKER_BOX"),
                BlockTexture("GREEN_STAINED_GLASS"),
                BlockTexture("GREEN_TERRACOTTA"),
                BlockTexture("GREEN_WOOL"),
                BlockTexture("HAY_BLOCK", top = true, bottom = false, front = false, defaultRotation = UP),
                BlockTexture("HONEYCOMB_BLOCK"),
                BlockTexture("HONEY_BLOCK", "honey_block_top", "honey_block_bottom", "honey_block_side"),
                BlockTexture("HORN_CORAL_BLOCK"),
                BlockTexture("ICE"),
                BlockTexture("IRON_BLOCK"),
                BlockTexture("IRON_ORE"),
                BlockTexture("JACK_O_LANTERN", "pumpkin_top", "pumpkin_top", "jack_o_lantern", "pumpkin_side"),
                BlockTexture("JIGSAW", top = true, bottom = true, front = false),
                BlockTexture("JUKEBOX", "jukebox_top", "jukebox_side", "jukebox_side"),
                BlockTexture("JUNGLE_LOG", "jungle_log_top", "jungle_log", UP),
                BlockTexture("JUNGLE_PLANKS"),
                BlockTexture("JUNGLE_WOOD", "jungle_log", UP),
                BlockTexture("LAPIS_BLOCK"),
                BlockTexture("LAPIS_ORE"),
                BlockTexture("LIGHT_BLUE_CONCRETE"),
                BlockTexture("LIGHT_BLUE_GLAZED_TERRACOTTA"),
                BlockTexture("LIGHT_BLUE_SHULKER_BOX"),
                BlockTexture("LIGHT_BLUE_STAINED_GLASS"),
                BlockTexture("LIGHT_BLUE_TERRACOTTA"),
                BlockTexture("LIGHT_BLUE_WOOL"),
                BlockTexture("LIGHT_GRAY_CONCRETE"),
                BlockTexture("LIGHT_GRAY_GLAZED_TERRACOTTA"),
                BlockTexture("LIGHT_GRAY_SHULKER_BOX"),
                BlockTexture("LIGHT_GRAY_STAINED_GLASS"),
                BlockTexture("LIGHT_GRAY_TERRACOTTA"),
                BlockTexture("LIGHT_GRAY_WOOL"),
                BlockTexture("LIME_CONCRETE"),
                BlockTexture("LIME_GLAZED_TERRACOTTA"),
                BlockTexture("LIME_SHULKER_BOX"),
                BlockTexture("LIME_STAINED_GLASS"),
                BlockTexture("LIME_TERRACOTTA"),
                BlockTexture("LIME_WOOL"),
                BlockTexture("LODESTONE", top = true, bottom = false, front = false),
                BlockTexture("LOOM", top = true, bottom = true, front = true),
                BlockTexture("MAGENTA_CONCRETE"),
                BlockTexture("MAGENTA_GLAZED_TERRACOTTA"),
                BlockTexture("MAGENTA_SHULKER_BOX"),
                BlockTexture("MAGENTA_STAINED_GLASS"),
                BlockTexture("MAGENTA_TERRACOTTA"),
                BlockTexture("MAGENTA_WOOL"),
                BlockTexture("MAGMA_BLOCK", "magma"),
                BlockTexture("MELON", "melon_top", "melon_side"),
                BlockTexture("MOSSY_COBBLESTONE"),
                BlockTexture("MOSSY_STONE_BRICKS"),
                BlockTexture("MUSHROOM_STEM"),
                BlockTexture("MYCELIUM", "mycelium_top", "dirt", "mycelium_side"),
                BlockTexture("NETHERITE_BLOCK"),
                BlockTexture("NETHERRACK"),
                BlockTexture("NETHER_BRICKS"),
                BlockTexture("NETHER_GOLD_ORE"),
                BlockTexture("NETHER_QUARTZ_ORE"),
                BlockTexture("NETHER_WART_BLOCK"),
                BlockTexture("NOTE_BLOCK"),
                BlockTexture("OAK_LOG", "oak_log_top", "oak_log", UP),
                BlockTexture("OAK_PLANKS"),
                BlockTexture("OAK_WOOD", "oak_log", UP),
                BlockTexture("OBSIDIAN"),
                BlockTexture("ORANGE_CONCRETE"),
                BlockTexture("ORANGE_GLAZED_TERRACOTTA"),
                BlockTexture("ORANGE_SHULKER_BOX"),
                BlockTexture("ORANGE_STAINED_GLASS"),
                BlockTexture("ORANGE_TERRACOTTA"),
                BlockTexture("ORANGE_WOOL"),
                BlockTexture("PACKED_ICE"),
                BlockTexture("PINK_CONCRETE"),
                BlockTexture("PINK_GLAZED_TERRACOTTA"),
                BlockTexture("PINK_SHULKER_BOX"),
                BlockTexture("PINK_STAINED_GLASS"),
                BlockTexture("PINK_TERRACOTTA"),
                BlockTexture("PINK_WOOL"),
                BlockTexture("PISTON", top = true, bottom = true, front = false, defaultRotation = UP),
                BlockTexture("PODZOL", "podzol_top", "dirt", "podzol_side"),
                BlockTexture("POLISHED_ANDESITE"),
                BlockTexture("POLISHED_BASALT", top = true, bottom = false, front = false, defaultRotation = UP),
                BlockTexture("POLISHED_BLACKSTONE"),
                BlockTexture("POLISHED_BLACKSTONE_BRICKS"),
                BlockTexture("POLISHED_DIORITE"),
                BlockTexture("POLISHED_GRANITE"),
                BlockTexture("PRISMARINE"),
                BlockTexture("PRISMARINE_BRICKS"),
                BlockTexture("PUMPKIN", top = true, bottom = false, front = false),
                BlockTexture("PURPLE_CONCRETE"),
                BlockTexture("PURPLE_GLAZED_TERRACOTTA"),
                BlockTexture("PURPLE_SHULKER_BOX"),
                BlockTexture("PURPLE_STAINED_GLASS"),
                BlockTexture("PURPLE_TERRACOTTA"),
                BlockTexture("PURPLE_WOOL"),
                BlockTexture("PURPUR_BLOCK"),
                BlockTexture("PURPUR_PILLAR", "purpur_pillar_top", "purpur_pillar", UP),
                BlockTexture("QUARTZ_BLOCK", "quartz_block_top"),
                BlockTexture("QUARTZ_BRICKS"),
                BlockTexture("QUARTZ_PILLAR", "quartz_pillar_top", "quartz_pillar", UP),
                BlockTexture("REDSTONE_BLOCK"),
                BlockTexture("REDSTONE_LAMP"),
                BlockTexture("REDSTONE_ORE"),
                BlockTexture("RED_CONCRETE"),
                BlockTexture("RED_GLAZED_TERRACOTTA"),
                BlockTexture("RED_MUSHROOM_BLOCK"),
                BlockTexture("RED_NETHER_BRICKS"),
                BlockTexture("RED_SANDSTONE", "red_sandstone_top", "red_sandstone_bottom", "red_sandstone"),
                BlockTexture("RED_SHULKER_BOX"),
                BlockTexture("RED_STAINED_GLASS"),
                BlockTexture("RED_TERRACOTTA"),
                BlockTexture("RED_WOOL"),
                BlockTexture("RESPAWN_ANCHOR", "respawn_anchor_top_off", "respawn_anchor_bottom", "respawn_anchor_side0"),
                BlockTexture("SANDSTONE", "sandstone_top", "red_sandstone_bottom", "sandstone"),
                BlockTexture("SEA_LANTERN"),
                BlockTexture("SHROOMLIGHT"),
                BlockTexture("SHULKER_BOX"),
                BlockTexture("SLIME_BLOCK"),
                BlockTexture("SMITHING_TABLE", "smithing_table_top", "smithing_table_bottom", "smithing_table_front", "smithing_table_front", "smithing_table_side", "smithing_table_side"),
                BlockTexture("SMOKER", top = true, bottom = true, front = true),
                BlockTexture("SMOOTH_QUARTZ", "quartz_block_bottom"),
                BlockTexture("SMOOTH_RED_SANDSTONE", "red_sandstone_top"),
                BlockTexture("SMOOTH_SANDSTONE", "sandstone_top"),
                BlockTexture("SMOOTH_STONE"),
                BlockTexture("SNOW_BLOCK", "snow"),
                BlockTexture("SOUL_SAND"),
                BlockTexture("SOUL_SOIL"),
                BlockTexture("SPAWNER"),
                BlockTexture("SPONGE"),
                BlockTexture("SPRUCE_LOG", "spruce_log_top", "spruce_log", UP),
                BlockTexture("SPRUCE_PLANKS"),
                BlockTexture("SPRUCE_WOOD", "spruce_log", UP),
                BlockTexture("STICKY_PISTON", "piston_top_sticky", "piston_bottom", "piston_side", UP),
                BlockTexture("STONE"),
                BlockTexture("STONE_BRICKS"),
                BlockTexture("STRIPPED_ACACIA_LOG", "stripped_acacia_log_top", "stripped_acacia_log", UP),
                BlockTexture("STRIPPED_ACACIA_WOOD", "stripped_acacia_log", UP),
                BlockTexture("STRIPPED_BIRCH_LOG", "stripped_birch_log_top", "stripped_birch_log", UP),
                BlockTexture("STRIPPED_BIRCH_WOOD", "stripped_birch_log", UP),
                BlockTexture("STRIPPED_CRIMSON_HYPHAE", "stripped_crimson_stem", UP),
                BlockTexture("STRIPPED_CRIMSON_STEM", "stripped_crimson_stem_top", "stripped_crimson_stem", UP),
                BlockTexture("STRIPPED_DARK_OAK_LOG", "stripped_dark_oak_log_top", "stripped_dark_oak_log", UP),
                BlockTexture("STRIPPED_DARK_OAK_WOOD", "stripped_dark_oak_log", UP),
                BlockTexture("STRIPPED_JUNGLE_LOG", "stripped_jungle_log_top", "stripped_jungle_log", UP),
                BlockTexture("STRIPPED_JUNGLE_WOOD", "stripped_jungle_log", UP),
                BlockTexture("STRIPPED_OAK_LOG", "stripped_oak_log_top", "stripped_oak_log", UP),
                BlockTexture("STRIPPED_OAK_WOOD", "stripped_oak_log", UP),
                BlockTexture("STRIPPED_SPRUCE_LOG", "stripped_spruce_log_top", "stripped_spruce_log", UP),
                BlockTexture("STRIPPED_SPRUCE_WOOD", "stripped_spruce_log", UP),
                BlockTexture("STRIPPED_WARPED_HYPHAE", "stripped_warped_stem", UP),
                BlockTexture("STRIPPED_WARPED_STEM", "stripped_warped_stem_top", "stripped_warped_stem", UP),
                BlockTexture("STRUCTURE_BLOCK"),
                BlockTexture("TARGET", "target_top", "target_side"),
                BlockTexture("TERRACOTTA"),
                BlockTexture("TNT", top = true, bottom = true, front = false),
                BlockTexture("TUBE_CORAL_BLOCK"),
                BlockTexture("WARPED_HYPHAE", "warped_stem", UP),
                BlockTexture("WARPED_NYLIUM", "warped_nylium", "netherrack", "warped_nylium_side"),
                BlockTexture("WARPED_PLANKS"),
                BlockTexture("WARPED_STEM", "warped_stem_top", "warped_stem", UP),
                BlockTexture("WARPED_WART_BLOCK"),
                BlockTexture("WET_SPONGE"),
                BlockTexture("WHITE_CONCRETE"),
                BlockTexture("WHITE_GLAZED_TERRACOTTA"),
                BlockTexture("WHITE_SHULKER_BOX"),
                BlockTexture("WHITE_STAINED_GLASS"),
                BlockTexture("WHITE_TERRACOTTA"),
                BlockTexture("WHITE_WOOL"),
                BlockTexture("YELLOW_CONCRETE"),
                BlockTexture("YELLOW_GLAZED_TERRACOTTA"),
                BlockTexture("YELLOW_SHULKER_BOX"),
                BlockTexture("YELLOW_STAINED_GLASS"),
                BlockTexture("YELLOW_TERRACOTTA"),
                BlockTexture("YELLOW_WOOL"),
                BlockTexture("WHITE_CONCRETE_POWDER"),
                BlockTexture("ORANGE_CONCRETE_POWDER"),
                BlockTexture("MAGENTA_CONCRETE_POWDER"),
                BlockTexture("LIGHT_BLUE_CONCRETE_POWDER"),
                BlockTexture("YELLOW_CONCRETE_POWDER"),
                BlockTexture("LIME_CONCRETE_POWDER"),
                BlockTexture("PINK_CONCRETE_POWDER"),
                BlockTexture("GRAY_CONCRETE_POWDER"),
                BlockTexture("LIGHT_GRAY_CONCRETE_POWDER"),
                BlockTexture("CYAN_CONCRETE_POWDER"),
                BlockTexture("PURPLE_CONCRETE_POWDER"),
                BlockTexture("BLUE_CONCRETE_POWDER"),
                BlockTexture("BROWN_CONCRETE_POWDER"),
                BlockTexture("GREEN_CONCRETE_POWDER"),
                BlockTexture("RED_CONCRETE_POWDER"),
                BlockTexture("BLACK_CONCRETE_POWDER"),
                BlockTexture("SAND"),
                BlockTexture("GRAVEL"),
                BlockTexture("OAK_SLAB", "oak_planks"),
                BlockTexture("SPRUCE_SLAB", "spruce_planks"),
                BlockTexture("BIRCH_SLAB", "birch_planks"),
                BlockTexture("JUNGLE_SLAB", "jungle_planks"),
                BlockTexture("ACACIA_SLAB", "acacia_planks"),
                BlockTexture("DARK_OAK_SLAB", "dark_oak_planks"),
                BlockTexture("CRIMSON_SLAB", "crimson_planks"),
                BlockTexture("WARPED_SLAB", "warped_planks"),
                BlockTexture("STONE_SLAB", "stone"),
                BlockTexture("SMOOTH_STONE_SLAB", "smooth_stone", "smooth_stone_slab_side"),
                BlockTexture("SANDSTONE_SLAB", "sandstone_top", "sandstone_bottom", "sandstone"),
                BlockTexture("CUT_SANDSTONE_SLAB", "sandstone_top", "cut_sandstone"),
                BlockTexture("SMOOTH_SANDSTONE_SLAB", "sandstone_top"),
                BlockTexture("RED_SANDSTONE_SLAB", "red_sandstone_top", "red_sandstone_bottom", "red_sandstone"),
                BlockTexture("CUT_RED_SANDSTONE_SLAB", "red_sandstone_top", "cut_red_sandstone"),
                BlockTexture("SMOOTH_RED_SANDSTONE_SLAB", "red_sandstone_top"),
                BlockTexture("PETRIFIED_OAK_SLAB", "oak_planks"),
                BlockTexture("COBBLESTONE_SLAB", "cobblestone"),
                BlockTexture("BRICK_SLAB", "bricks"),
                BlockTexture("STONE_BRICK_SLAB", "stone_bricks"),
                BlockTexture("NETHER_BRICK_SLAB", "nether_bricks"),
                BlockTexture("QUARTZ_SLAB", "quartz_block_top"),
                BlockTexture("SMOOTH_QUARTZ_SLAB", "quartz_block_bottom"),
                BlockTexture("PURPUR_SLAB", "purpur_block"),
                BlockTexture("PRISMARINE_SLAB", "prismarine"),
                BlockTexture("PRISMARINE_BRICK_SLAB", "prismarine_bricks"),
                BlockTexture("DARK_PRISMARINE_SLAB", "dark_prismarine"),
                BlockTexture("POLISHED_GRANITE_SLAB", "polished_granite"),
                BlockTexture("POLISHED_DIORITE_SLAB", "polished_diorite"),
                BlockTexture("POLISHED_ANDESITE_SLAB", "polished_andesite"),
                BlockTexture("POLISHED_BLACKSTONE_SLAB", "polished_blackstone"),
                BlockTexture("POLISHED_BLACKSTONE_BRICK_SLAB", "polished_blackstone_bricks"),
                BlockTexture("MOSSY_STONE_BRICK_SLAB", "mossy_stone_bricks"),
                BlockTexture("MOSSY_COBBLESTONE_SLAB", "mossy_cobblestone"),
                BlockTexture("END_STONE_BRICK_SLAB", "end_stone_bricks"),
                BlockTexture("GRANITE_SLAB", "granite"),
                BlockTexture("ANDESITE_SLAB", "andesite"),
                BlockTexture("RED_NETHER_BRICK_SLAB", "nether_bricks"),
                BlockTexture("DIORITE_SLAB", "diorite"),
                BlockTexture("BLACKSTONE_SLAB", "blackstone_top", "blackstone"),
                BlockTexture("ACACIA_STAIRS", "acacia_planks"),
                BlockTexture("ANDESITE_STAIRS", "andesite"),
                BlockTexture("BIRCH_STAIRS", "birch_planks"),
                BlockTexture("BLACKSTONE_STAIRS", "blackstone_top", "blackstone"),
                BlockTexture("BRICK_STAIRS", "bricks"),
                BlockTexture("COBBLESTONE_STAIRS", "cobblestone"),
                BlockTexture("CRIMSON_STAIRS", "crimson_planks"),
                BlockTexture("DARK_OAK_STAIRS", "dark_oak_planks"),
                BlockTexture("DARK_PRISMARINE_STAIRS", "dark_prismarine"),
                BlockTexture("DIORITE_STAIRS", "diorite"),
                BlockTexture("END_STONE_BRICK_STAIRS", "end_stone_bricks"),
                BlockTexture("GRANITE_STAIRS", "granite"),
                BlockTexture("JUNGLE_STAIRS", "jungle_planks"),
                BlockTexture("MOSSY_COBBLESTONE_STAIRS", "mossy_cobblestone"),
                BlockTexture("MOSSY_STONE_BRICK_STAIRS", "mossy_stone_bricks"),
                BlockTexture("NETHER_BRICK_STAIRS", "nether_bricks"),
                BlockTexture("OAK_STAIRS", "oak_planks"),
                BlockTexture("POLISHED_ANDESITE_STAIRS", "polished_andesite"),
                BlockTexture("POLISHED_BLACKSTONE_BRICK_STAIRS", "polished_blackstone_bricks"),
                BlockTexture("POLISHED_BLACKSTONE_STAIRS", "polished_blackstone"),
                BlockTexture("POLISHED_DIORITE_STAIRS", "polished_diorite"),
                BlockTexture("POLISHED_GRANITE_STAIRS", "polished_granite"),
                BlockTexture("PRISMARINE_BRICK_STAIRS", "prismarine_bricks"),
                BlockTexture("PRISMARINE_STAIRS", "prismarine"),
                BlockTexture("PURPUR_STAIRS", "purpur_block"),
                BlockTexture("QUARTZ_STAIRS", "quartz_block_top"),
                BlockTexture("RED_NETHER_BRICK_STAIRS", "red_nether_bricks"),
                BlockTexture("RED_SANDSTONE_STAIRS", "red_sandstone_top", "red_sandstone_bottom", "red_sandstone"),
                BlockTexture("SANDSTONE_STAIRS", "sandstone_top", "sandstone_bottom", "sandstone"),
                BlockTexture("SMOOTH_QUARTZ_STAIRS", "quartz_block_bottom"),
                BlockTexture("SMOOTH_RED_SANDSTONE_STAIRS", "red_sandstone_top"),
                BlockTexture("SMOOTH_SANDSTONE_STAIRS", "sandstone_top"),
                BlockTexture("SPRUCE_STAIRS", "spruce_planks"),
                BlockTexture("STONE_BRICK_STAIRS", "stone_bricks"),
                BlockTexture("STONE_STAIRS", "stone"),
                BlockTexture("WARPED_STAIRS", "warped_planks"),
                BlockTexture("OAK_SAPLING"),
                BlockTexture("SPRUCE_SAPLING"),
                BlockTexture("BIRCH_SAPLING"),
                BlockTexture("JUNGLE_SAPLING"),
                BlockTexture("ACACIA_SAPLING"),
                BlockTexture("DARK_OAK_SAPLING"),
                BlockTexture("GRASS", "modded/grass"),
                BlockTexture("FERN", "modded/fern"),
                BlockTexture("DEAD_BUSH"),
                BlockTexture("SEAGRASS"),
                BlockTexture("DANDELION"),
                BlockTexture("POPPY"),
                BlockTexture("BLUE_ORCHID"),
                BlockTexture("ALLIUM"),
                BlockTexture("AZURE_BLUET"),
                BlockTexture("RED_TULIP"),
                BlockTexture("ORANGE_TULIP"),
                BlockTexture("WHITE_TULIP"),
                BlockTexture("OXEYE_DAISY"),
                BlockTexture("CORNFLOWER"),
                BlockTexture("LILY_OF_THE_VALLEY"),
                BlockTexture("WITHER_ROSE"),
                BlockTexture("CRIMSON_FUNGUS"),
                BlockTexture("WARPED_FUNGUS"),
                BlockTexture("BROWN_MUSHROOM"),
                BlockTexture("RED_MUSHROOM"),
                BlockTexture("CRIMSON_ROOTS"),
                BlockTexture("WARPED_ROOTS"),
                BlockTexture("NETHER_SPROUTS"),
                BlockTexture("TUBE_CORAL"),
                BlockTexture("BRAIN_CORAL"),
                BlockTexture("BUBBLE_CORAL"),
                BlockTexture("FIRE_CORAL"),
                BlockTexture("TUBE_CORAL_FAN"),
                BlockTexture("BRAIN_CORAL_FAN"),
                BlockTexture("BUBBLE_CORAL_FAN"),
                BlockTexture("FIRE_CORAL_FAN"),
                BlockTexture("DEAD_TUBE_CORAL"),
                BlockTexture("DEAD_BRAIN_CORAL"),
                BlockTexture("DEAD_BUBBLE_CORAL"),
                BlockTexture("DEAD_FIRE_CORAL"),
                BlockTexture("DEAD_TUBE_CORAL_FAN"),
                BlockTexture("DEAD_BRAIN_CORAL_FAN"),
                BlockTexture("DEAD_BUBBLE_CORAL_FAN"),
                BlockTexture("DEAD_FIRE_CORAL_FAN"),
                BlockTexture("ACACIA_LEAVES", "modded/acacia_leaves"),
                BlockTexture("BIRCH_LEAVES", "modded/birch_leaves"),
                BlockTexture("DARK_OAK_LEAVES", "modded/dark_oak_leaves"),
                BlockTexture("JUNGLE_LEAVES", "modded/jungle_leaves"),
                BlockTexture("OAK_LEAVES", "modded/oak_leaves"),
                BlockTexture("SPRUCE_LEAVES", "modded/spruce_leaves")
            )
        }
        
    }
    
}

