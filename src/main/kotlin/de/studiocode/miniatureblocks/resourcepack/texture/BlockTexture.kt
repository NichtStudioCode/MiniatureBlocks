package de.studiocode.miniatureblocks.resourcepack.texture

import de.studiocode.miniatureblocks.resourcepack.model.Cube.Direction
import de.studiocode.miniatureblocks.resourcepack.model.Cube.Direction.NORTH
import de.studiocode.miniatureblocks.resourcepack.model.Cube.Direction.UP
import org.bukkit.Material

@Suppress("unused")
enum class BlockTexture {

    //TODO: missing blocks: sand, gravel, concrete powder
    
    ACACIA_LOG(Material.ACACIA_LOG, "acacia_log_top", "acacia_log", UP),
    ACACIA_PLANKS(Material.ACACIA_PLANKS),
    ACACIA_WOOD(Material.ACACIA_WOOD, "acacia_log", UP),
    ANCIENT_DEBRIS(Material.ANCIENT_DEBRIS, "ancient_debris_top", "ancient_debris_side"),
    ANDESITE(Material.ANDESITE),
    BARREL(Material.BARREL, "barrel_top", "barrel_bottom", "barrel_side"),
    BASALT(Material.BASALT, "basalt_top", "basalt_side", UP),
    BEACON(Material.BEACON), //not really
    BEDROCK(Material.BEDROCK),
    BEEHIVE(Material.BEEHIVE, "beehive_end", "beehive_end", "beehive_front", "beehive_side"),
    BEE_NEST(Material.BEE_NEST, "bee_nest_top", "bee_nest_bottom", "bee_nest_front", "bee_nest_side"),
    BIRCH_LOG(Material.BIRCH_LOG, "birch_log_top", "birch_log", UP),
    BIRCH_PLANKS(Material.BIRCH_PLANKS),
    BIRCH_WOOD(Material.BIRCH_WOOD, "birch_log", UP),
    BLACKSTONE(Material.BLACKSTONE, "blackstone_top", "blackstone"),
    BLACK_CONCRETE(Material.BLACK_CONCRETE),
    BLACK_GLAZED_TERRACOTTA(Material.BLACK_GLAZED_TERRACOTTA),
    BLACK_SHULKER_BOX(Material.BLACK_SHULKER_BOX), //has multiple textures in-game but i could only find one texture in the assets
    BLACK_STAINED_GLASS(Material.BLACK_STAINED_GLASS),
    BLACK_TERRACOTTA(Material.BLACK_TERRACOTTA),
    BLACK_WOOL(Material.BLACK_WOOL),
    BLAST_FURNACE(Material.BLAST_FURNACE, true, false, true),
    BLUE_CONCRETE(Material.BLUE_CONCRETE),
    BLUE_GLAZED_TERRACOTTA(Material.BLUE_GLAZED_TERRACOTTA),
    BLUE_ICE(Material.BLUE_ICE),
    BLUE_SHULKER_BOX(Material.BLUE_SHULKER_BOX),
    BLUE_STAINED_GLASS(Material.BLUE_STAINED_GLASS),
    BLUE_TERRACOTTA(Material.BLUE_TERRACOTTA),
    BLUE_WOOL(Material.BLUE_WOOL),
    BONE_BLOCK(Material.BONE_BLOCK, true, false, false, UP),
    BOOKSHELF(Material.BOOKSHELF, "oak_planks", "bookshelf"),
    BRAIN_CORAL_BLOCK(Material.BRAIN_CORAL_BLOCK),
    BRICKS(Material.BRICKS),
    BROWN_CONCRETE(Material.BROWN_CONCRETE),
    BROWN_GLAZED_TERRACOTTA(Material.BROWN_GLAZED_TERRACOTTA),
    BROWN_MUSHROOM_BLOCK(Material.BROWN_MUSHROOM_BLOCK),
    BROWN_SHULKER_BOX(Material.BROWN_SHULKER_BOX),
    BROWN_STAINED_GLASS(Material.BROWN_STAINED_GLASS),
    BROWN_TERRACOTTA(Material.BROWN_TERRACOTTA),
    BROWN_WOOL(Material.BROWN_WOOL),
    BUBBLE_CORAL_BLOCK(Material.BUBBLE_CORAL_BLOCK),
    CARTOGRAPHY_TABLE(Material.CARTOGRAPHY_TABLE, "cartography_table_top", "dark_oak_planks", "cartography_table_side1", "cartography_table_side3", "cartography_table_side3", "cartography_table_side2"),
    CARVED_PUMPKIN(Material.CARVED_PUMPKIN, "pumpkin_top", "pumpkin_top", "carved_pumpkin", "pumpkin_side"),
    CHISELED_NETHER_BRICKS(Material.CHISELED_NETHER_BRICKS),
    CHISELED_POLISHED_BLACKSTONE(Material.CHISELED_POLISHED_BLACKSTONE),
    CHISELED_QUARTZ_BLOCK(Material.CHISELED_QUARTZ_BLOCK, "chiseled_quartz_block_top", "chiseled_quartz_block"),
    CHISELED_RED_SANDSTONE(Material.CHISELED_RED_SANDSTONE),
    CHISELED_SANDSTONE(Material.CHISELED_SANDSTONE),
    CHISELED_STONE_BRICKS(Material.CHISELED_STONE_BRICKS),
    CLAY(Material.CLAY),
    BLOCK_OF_COAL(Material.COAL_BLOCK),
    COAL_ORE(Material.COAL_ORE),
    COARSE_DIRT(Material.COARSE_DIRT),
    COBBLESTONE(Material.COBBLESTONE),
    CRACKED_NETHER_BRICKS(Material.CRACKED_NETHER_BRICKS),
    CRACKED_POLISHED_BLACKSTONE_BRICKS(Material.CRACKED_POLISHED_BLACKSTONE_BRICKS),
    CRACKED_STONE_BRICKS(Material.CRACKED_STONE_BRICKS),
    CRAFTING_TABLE(Material.CRAFTING_TABLE, "crafting_table_top", "oak_planks", "crafting_table_front", "crafting_table_side"),
    CRIMSON_HYPHAE(Material.CRIMSON_HYPHAE, UP),
    CRIMSON_NYLIUM(Material.CRIMSON_NYLIUM, "crimson_nylium", "netherrack", "crimson_nylium_side"),
    CRIMSON_PLANKS(Material.CRIMSON_PLANKS),
    CRIMSON_STEM(Material.CRIMSON_STEM, "crimson_stem_top", "crimson_stem_top", "crimson_stem", UP),
    CRYING_OBSIDIAN(Material.CRYING_OBSIDIAN),
    CUT_RED_SANDSTONE(Material.CUT_RED_SANDSTONE, "red_sandstone_top", "red_sandstone_top", "cut_red_sandstone"),
    CUT_SANDSTONE(Material.CUT_SANDSTONE, "sandstone_top", "sandstone_top", "cut_sandstone"),
    CYAN_CONCRETE(Material.CYAN_CONCRETE),
    CYAN_GLAZED_TERRACOTTA(Material.CYAN_GLAZED_TERRACOTTA),
    CYAN_SHULKER_BOX(Material.CYAN_SHULKER_BOX),
    CYAN_STAINED_GLASS(Material.CYAN_STAINED_GLASS),
    CYAN_TERRACOTTA(Material.CYAN_TERRACOTTA),
    CYAN_WOOL(Material.CYAN_WOOL),
    DARK_OAK_LOG(Material.DARK_OAK_LOG, "dark_oak_log_top", "dark_oak_log", UP),
    DARK_OAK_PLANKS(Material.DARK_OAK_PLANKS),
    DARK_OAK_WOOD(Material.DARK_OAK_WOOD, "dark_oak_log", UP),
    DARK_PRISMARINE(Material.DARK_PRISMARINE),
    DEAD_BRAIN_CORAL_BLOCK(Material.DEAD_BRAIN_CORAL_BLOCK),
    DEAD_BUBBLE_CORAL_BLOCK(Material.DEAD_BUBBLE_CORAL_BLOCK),
    DEAD_FIRE_CORAL_BLOCK(Material.DEAD_FIRE_CORAL_BLOCK),
    DEAD_HORN_CORAL_BLOCK(Material.DEAD_HORN_CORAL_BLOCK),
    DEAD_TUBE_CORAL(Material.DEAD_TUBE_CORAL),
    DIAMOND_BLOCK(Material.DIAMOND_BLOCK),
    DIAMOND_ORE(Material.DIAMOND_ORE),
    DIORITE(Material.DIORITE),
    DIRT(Material.DIRT),
    DISPENSER(Material.DISPENSER, "furnace_top", "furnace_top", "dispenser_front", "furnace_side"),
    DRIED_KELP_BLOCK(Material.DRIED_KELP_BLOCK, "dried_kelp_top", "dried_kelp_bottom", "dried_kelp_side"),
    DROPPER(Material.DROPPER, "furnace_top", "furnace_top", "dropper_front", "furnace_side"),
    EMERALD_BLOCK(Material.EMERALD_BLOCK),
    EMERALD_ORE(Material.EMERALD_ORE),
    END_STONE(Material.END_STONE),
    END_STONE_BRICKS(Material.END_STONE_BRICKS),
    FARMLAND(Material.FARMLAND, "farmland_moist", "dirt", "dirt"),
    FIRE_CORAL_BLOCK(Material.FIRE_CORAL_BLOCK),
    FLETCHING_TABLE(Material.FLETCHING_TABLE, "fletching_table_top", "birch_planks", "fletching_table_front", "fletching_table_front", "fletching_table_side", "fletching_table_side"),
    FURNACE(Material.FURNACE, "furnace_top", "furnace_top", "furnace_front", "furnace_side"),
    GILDED_BLACKSTONE(Material.GILDED_BLACKSTONE),
    GLASS(Material.GLASS),
    GLOWSTONE(Material.GLOWSTONE),
    GOLD_BLOCK(Material.GOLD_BLOCK),
    GOLD_ORE(Material.GOLD_ORE),
    GRANITE(Material.GRANITE),
    GRASS_BLOCK(Material.GRASS_BLOCK, "lime_wool", "dirt", "grass_block_side"), //lime wool because it is gray otherwise Todo: find alternative
    GRASS_PATH(Material.GRASS_PATH, "grass_path_top", "dirt", "grass_path_side"),
    GRAY_CONCRETE(Material.GRAY_CONCRETE),
    GRAY_GLAZED_TERRACOTTA(Material.GRAY_GLAZED_TERRACOTTA),
    GRAY_SHULKER_BOX(Material.GRAY_SHULKER_BOX),
    GRAY_STAINED_GLASS(Material.GRAY_STAINED_GLASS),
    GRAY_TERRACOTTA(Material.GRAY_TERRACOTTA),
    GRAY_WOOL(Material.GRAY_WOOL),
    GREEN_CONCRETE(Material.GREEN_CONCRETE),
    GREEN_GLAZED_TERRACOTTA(Material.GREEN_GLAZED_TERRACOTTA),
    GREEN_SHULKER_BOX(Material.GREEN_SHULKER_BOX),
    GREEN_STAINED_GLASS(Material.GREEN_STAINED_GLASS),
    GREEN_TERRACOTTA(Material.GREEN_TERRACOTTA),
    GREEN_WOOL(Material.GREEN_WOOL),
    HAY_BLOCK(Material.HAY_BLOCK, true, false, false, UP),
    HONEYCOMB_BLOCK(Material.HONEYCOMB_BLOCK),
    HONEY_BLOCK(Material.HONEY_BLOCK),
    HORN_CORAL_BLOCK(Material.HORN_CORAL_BLOCK),
    ICE(Material.ICE),
    IRON_BLOCK(Material.IRON_BLOCK),
    IRON_ORE(Material.IRON_ORE),
    JACK_O_LANTERN(Material.JACK_O_LANTERN, "pumpkin_top", "pumpkin_top", "jack_o_lantern", "pumpkin_side"),
    JIGSAW_BLOCK(Material.JIGSAW, true, true, false),
    JUKEBOX(Material.JUKEBOX, "jukebox_top", "jukebox_side", "jukebox_side"),
    JUNGLE_LOG(Material.JUNGLE_LOG, "jungle_log_top", "jungle_log", UP),
    JUNGLE_PLANKS(Material.JUNGLE_PLANKS),
    JUNGLE_WOOD(Material.JUNGLE_WOOD, "jungle_log", UP),
    LAPIS_BLOCK(Material.LAPIS_BLOCK),
    LAPIS_ORE(Material.LAPIS_ORE),
    LIGHT_BLUE_CONCRETE(Material.LIGHT_BLUE_CONCRETE),
    LIGHT_BLUE_GLAZED_TERRACOTTA(Material.LIGHT_BLUE_GLAZED_TERRACOTTA),
    LIGHT_BLUE_SHULKER_BOX(Material.LIGHT_BLUE_SHULKER_BOX),
    LIGHT_BLUE_STAINED_GLASS(Material.LIGHT_BLUE_STAINED_GLASS),
    LIGHT_BLUE_TERRACOTTA(Material.LIGHT_BLUE_TERRACOTTA),
    LIGHT_BLUE_WOOL(Material.LIGHT_BLUE_WOOL),
    LIGHT_GRAY_CONCRETE(Material.LIGHT_GRAY_CONCRETE),
    LIGHT_GRAY_GLAZED_TERRACOTTA(Material.LIGHT_GRAY_GLAZED_TERRACOTTA),
    LIGHT_GRAY_SHULKER_BOX(Material.LIGHT_GRAY_SHULKER_BOX),
    LIGHT_GRAY_STAINED_GLASS(Material.LIGHT_GRAY_STAINED_GLASS),
    LIGHT_GRAY_TERRACOTTA(Material.LIGHT_GRAY_TERRACOTTA),
    LIGHT_GRAY_WOOL(Material.LIGHT_GRAY_WOOL),
    LIME_CONCRETE(Material.LIME_CONCRETE),
    LIME_GLAZED_TERRACOTTA(Material.LIME_GLAZED_TERRACOTTA),
    LIME_SHULKER_BOX(Material.LIME_SHULKER_BOX),
    LIME_STAINED_GLASS(Material.LIME_STAINED_GLASS),
    LIME_TERRACOTTA(Material.LIME_TERRACOTTA),
    LIME_WOOL(Material.LIME_WOOL),
    LODESTONE(Material.LODESTONE, true, false, false),
    LOOM(Material.LOOM, true, true, true),
    MAGENTA_CONCRETE(Material.MAGENTA_CONCRETE),
    MAGENTA_GLAZED_TERRACOTTA(Material.MAGENTA_GLAZED_TERRACOTTA),
    MAGENTA_SHULKER_BOX(Material.MAGENTA_SHULKER_BOX),
    MAGENTA_STAINED_GLASS(Material.MAGENTA_STAINED_GLASS),
    MAGENTA_TERRACOTTA(Material.MAGENTA_TERRACOTTA),
    MAGENTA_WOOL(Material.MAGENTA_WOOL),
    MAGMA_BLOCK(Material.MAGMA_BLOCK),
    MELON(Material.MELON, true, false, true),
    MOSSY_COBBLESTONE(Material.MOSSY_COBBLESTONE),
    MOSSY_STONE_BRICKS(Material.MOSSY_STONE_BRICKS),
    MUSHROOM_STEM(Material.MUSHROOM_STEM),
    MYCELIUM(Material.MYCELIUM, "mycelium_top", "dirt", "mycelium_side"),
    NETHERITE_BLOCK(Material.NETHERITE_BLOCK),
    NETHERRACK(Material.NETHERRACK),
    NETHER_BRICKS(Material.NETHER_BRICKS),
    NETHER_GOLD_ORE(Material.NETHER_GOLD_ORE),
    NETHER_QUARTZ_ORE(Material.NETHER_QUARTZ_ORE),
    NETHER_WART_BLOCK(Material.NETHER_WART_BLOCK),
    NOTE_BLOCK(Material.NOTE_BLOCK),
    OAK_LOG(Material.OAK_LOG, "oak_log_top", "oak_log", UP),
    OAK_PLANKS(Material.OAK_PLANKS),
    OAK_WOOD(Material.OAK_WOOD, "oak_log", UP),
    OBSIDIAN(Material.OBSIDIAN),
    ORANGE_CONCRETE(Material.ORANGE_CONCRETE),
    ORANGE_GLAZED_TERRACOTTA(Material.ORANGE_GLAZED_TERRACOTTA),
    ORANGE_SHULKER_BOX(Material.ORANGE_SHULKER_BOX),
    ORANGE_STAINED_GLASS(Material.ORANGE_STAINED_GLASS),
    ORANGE_TERRACOTTA(Material.ORANGE_TERRACOTTA),
    ORANGE_WOOL(Material.ORANGE_WOOL),
    PACKED_ICE(Material.PACKED_ICE),
    PINK_CONCRETE(Material.PINK_CONCRETE),
    PINK_GLAZED_TERRACOTTA(Material.PINK_GLAZED_TERRACOTTA),
    PINK_SHULKER_BOX(Material.PINK_SHULKER_BOX),
    PINK_STAINED_GLASS(Material.PINK_STAINED_GLASS),
    PINK_TERRACOTTA(Material.PINK_TERRACOTTA),
    PINK_WOOL(Material.PINK_WOOL),
    PISTON(Material.PISTON, true, true, false, UP),
    PODZOL(Material.PODZOL, "podzol_top", "dirt", "podzol_side"),
    POLISHED_ANDESITE(Material.POLISHED_ANDESITE),
    POLISHED_BASALT(Material.POLISHED_BASALT, true, false, false, UP),
    POLISHED_BLACKSTONE(Material.POLISHED_BLACKSTONE),
    POLISHED_BLACKSTONE_BRICKS(Material.POLISHED_BLACKSTONE_BRICKS),
    POLISHED_DIORITE(Material.POLISHED_DIORITE),
    POLISHED_GRANITE(Material.POLISHED_GRANITE),
    PRISMARINE(Material.PRISMARINE),
    PRISMARINE_BRICKS(Material.PRISMARINE_BRICKS),
    PUMPKIN(Material.PUMPKIN, true, false, false),
    PURPLE_CONCRETE(Material.PURPLE_CONCRETE),
    PURPLE_GLAZED_TERRACOTTA(Material.PURPLE_GLAZED_TERRACOTTA),
    PURPLE_SHULKER_BOX(Material.PURPLE_SHULKER_BOX),
    PURPLE_STAINED_GLASS(Material.PURPLE_STAINED_GLASS),
    PURPLE_TERRACOTTA(Material.PURPLE_TERRACOTTA),
    PURPLE_WOOL(Material.PURPLE_WOOL),
    PURPUR_BLOCK(Material.PURPUR_BLOCK),
    PURPUR_PILLAR(Material.PURPUR_PILLAR, "purpur_pillar_top", "purpur_pillar", UP),
    QUARTZ_BLOCK(Material.QUARTZ_BLOCK),
    QUARTZ_BRICKS(Material.QUARTZ_BRICKS),
    QUARTZ_PILLAR(Material.QUARTZ_PILLAR, "quartz_pillar_top", "quartz_pillar", UP),
    REDSTONE_BLOCK(Material.REDSTONE_BLOCK),
    REDSTONE_LAMP(Material.REDSTONE_LAMP),
    REDSTONE_ORE(Material.REDSTONE_ORE),
    RED_CONCRETE(Material.RED_CONCRETE),
    RED_GLAZED_TERRACOTTA(Material.RED_GLAZED_TERRACOTTA),
    RED_MUSHROOM_BLOCK(Material.RED_MUSHROOM_BLOCK),
    RED_NETHER_BRICKS(Material.RED_NETHER_BRICKS),
    RED_SANDSTONE(Material.RED_SANDSTONE, "red_sandstone_top", "red_sandstone_bottom", "red_sandstone"),
    RED_SHULKER_BOX(Material.RED_SHULKER_BOX),
    RED_STAINED_GLASS(Material.RED_STAINED_GLASS),
    RED_TERRACOTTA(Material.RED_TERRACOTTA),
    RED_WOOL(Material.RED_WOOL),
    RESPAWN_ANCHOR(Material.RESPAWN_ANCHOR, "respawn_anchor_top_off", "respawn_anchor_bottom", "respawn_anchor_side0"),
    SANDSTONE(Material.SANDSTONE, "sandstone_top", "red_sandstone_bottom", "sandstone"),
    SEA_LANTERN(Material.SEA_LANTERN),
    SHROOMLIGHT(Material.SHROOMLIGHT),
    SHULKER_BOX(Material.SHULKER_BOX),
    SLIME_BLOCK(Material.SLIME_BLOCK),
    SMITHING_TABLE(Material.SMITHING_TABLE, "smithing_table_top", "smithing_table_bottom", "smithing_table_front", "smithing_table_front", "smithing_table_side", "smithing_table_side"),
    SMOKER(Material.SMOKER, true, true, true),
    SMOOTH_QUARTZ(Material.SMOOTH_QUARTZ),
    SMOOTH_RED_SANDSTONE(Material.SMOOTH_RED_SANDSTONE),
    SMOOTH_SANDSTONE(Material.SMOOTH_SANDSTONE),
    SMOOTH_STONE(Material.SMOOTH_STONE),
    SNOW_BLOCK(Material.SNOW_BLOCK),
    SOUL_SAND(Material.SOUL_SAND),
    SOUL_SOIL(Material.SOUL_SOIL),
    SPAWNER(Material.SPAWNER),
    SPONGE(Material.SPONGE),
    SPRUCE_LOG(Material.SPRUCE_LOG, "spruce_log_top", "spruce_log", UP),
    SPRUCE_PLANKS(Material.SPRUCE_PLANKS),
    SPRUCE_WOOD(Material.SPRUCE_WOOD, "spruce_og", UP),
    STICKY_PISTON(Material.STICKY_PISTON, "piston_top_sticky", "piston_bottom", "piston_side", UP),
    STONE(Material.STONE),
    STONE_BRICKS(Material.STONE_BRICKS),
    STONE_SLAB(Material.STONE_SLAB),
    STRIPPED_ACACIA_LOG(Material.STRIPPED_ACACIA_LOG, "stripped_acacia_log_top", "stripped_acacia_log", UP),
    STRIPPED_ACACIA_WOOD(Material.STRIPPED_ACACIA_WOOD, "stripped_acacia_log", UP),
    STRIPPED_BIRCH_LOG(Material.STRIPPED_BIRCH_LOG, "stripped_birch_log_top", "stripped_birch_log", UP),
    STRIPPED_BIRCH_WOOD(Material.STRIPPED_BIRCH_WOOD, "stripped_birch_log", UP),
    STRIPPED_CRIMSON_HYPHAE(Material.STRIPPED_CRIMSON_HYPHAE, "stripped_crimson_stem", UP),
    STRIPPED_CRIMSON_STEM(Material.STRIPPED_CRIMSON_STEM, "stripped_crimson_stem_top", "stripped_crimson_stem", UP),
    STRIPPED_DARK_OAK_LOG(Material.STRIPPED_DARK_OAK_LOG, "stripped_dark_oak_log_top", "stripped_dark_oak_log", UP),
    STRIPPED_DARK_OAK_WOOD(Material.STRIPPED_DARK_OAK_WOOD, "stripped_dark_oak_log", UP),
    STRIPPED_JUNGLE_LOG(Material.STRIPPED_JUNGLE_LOG, "stripped_jungle_log_top", "stripped_jungle_log", UP),
    STRIPPED_JUNGLE_WOOD(Material.STRIPPED_JUNGLE_WOOD, "stripped_jungle_log", UP),
    STRIPPED_OAK_LOG(Material.STRIPPED_OAK_LOG, "stripped_oak_log_top", "stripped_oak_log", UP),
    STRIPPED_OAK_WOOD(Material.STRIPPED_OAK_WOOD, "stripped_oak_log", UP),
    STRIPPED_SPRUCE_LOG(Material.STRIPPED_SPRUCE_LOG, "stripped_spruce_log_top", "stripped_spruce_log", UP),
    STRIPPED_SPRUCE_WOOD(Material.STRIPPED_SPRUCE_WOOD, "stripped_spruce_log", UP),
    STRIPPED_WARPED_HYPHAE(Material.STRIPPED_WARPED_HYPHAE, "stripped_warped_stem", UP),
    STRIPPED_WARPED_STEM(Material.STRIPPED_WARPED_STEM, "stripped_warped_stem_top", "stripped_warped_stem", UP),
    STRUCTURE_BLOCK(Material.STRUCTURE_BLOCK),
    TARGET(Material.TARGET),
    TERRACOTTA(Material.TERRACOTTA),
    TNT(Material.TNT, true, true, false),
    DEAD_TUBE_CORAL_BLOCK(Material.TUBE_CORAL_BLOCK),
    WARPED_HYPHAE(Material.WARPED_HYPHAE, UP),
    WARPED_NYLIUM(Material.WARPED_NYLIUM, "warped_nylium", "netherrack", "warped_nylium_side"),
    WARPED_PLANKS(Material.WARPED_PLANKS),
    WARPED_STEM(Material.WARPED_STEM, "warped_stem_top", "warped_stem", UP),
    WARPED_WART_BLOCK(Material.WARPED_WART_BLOCK),
    WATER(Material.WATER),
    WET_SPONGE(Material.WET_SPONGE),
    WHITE_CONCRETE(Material.WHITE_CONCRETE),
    WHITE_GLAZED_TERRACOTTA(Material.WHITE_GLAZED_TERRACOTTA),
    WHITE_SHULKER_BOX(Material.WHITE_SHULKER_BOX),
    WHITE_STAINED_GLASS(Material.WHITE_STAINED_GLASS),
    WHITE_TERRACOTTA(Material.WHITE_TERRACOTTA),
    WHITE_WOOL(Material.WHITE_WOOL),
    YELLOW_CONCRETE(Material.YELLOW_CONCRETE),
    YELLOW_GLAZED_TERRACOTTA(Material.YELLOW_GLAZED_TERRACOTTA),
    YELLOW_SHULKER_BOX(Material.YELLOW_SHULKER_BOX),
    YELLOW_STAINED_GLASS(Material.YELLOW_STAINED_GLASS),
    YELLOW_TERRACOTTA(Material.YELLOW_TERRACOTTA),
    YELLOW_WOOL(Material.YELLOW_WOOL);

    val material: Material
    val textureTop: String
    val textureBottom: String
    val textureFront: String
    val textureBack: String
    val textureRight: String
    val textureLeft: String
    val defaultRotation: Direction

    constructor(material: Material, textureTop: String, textureBottom: String, textureFront: String, textureBack: String,
                textureRight: String, textureLeft: String, defaultRotation: Direction = NORTH) {
        this.material = material
        this.textureTop = "block/$textureTop"
        this.textureBottom = "block/$textureBottom"
        this.textureFront = "block/$textureFront"
        this.textureBack = "block/$textureBack"
        this.textureRight = "block/$textureRight"
        this.textureLeft = "block/$textureLeft"
        this.defaultRotation = defaultRotation
    }

    constructor(material: Material, textureTop: String, textureBottom: String, textureFront: String, textureSide: String, defaultRotation: Direction = NORTH) :
            this(material, textureTop, textureBottom, textureFront, textureSide, textureSide, textureSide, defaultRotation)

    constructor(material: Material, textureTop: String, textureBottom: String, textureSide: String, defaultRotation: Direction = NORTH) :
            this(material, textureTop, textureBottom, textureSide, textureSide, defaultRotation)

    constructor(material: Material, textureTopBottom: String, textureSide: String, defaultRotation: Direction = NORTH) :
            this(material, textureTopBottom, textureTopBottom, textureSide, defaultRotation)

    constructor(material: Material, texture: String, defaultRotation: Direction = NORTH) : this(material, texture, texture, defaultRotation)

    constructor(material: Material, defaultRotation: Direction = NORTH) : this(material, material.toString().toLowerCase(), defaultRotation)

    constructor(material: Material, top: Boolean, bottom: Boolean, front: Boolean, defaultRotation: Direction = NORTH) {
        this.material = material

        val texture = "block/" + material.toString().toLowerCase()

        textureTop = texture + if (top) "_top" else ""
        textureBottom = texture + if (bottom) "_bottom" else if (top) "_top" else ""
        textureFront = texture + if (front) "_front" else "_side"

        val textureSide = texture + "_side"
        textureBack = textureSide
        textureRight = textureSide
        textureLeft = textureSide

        this.defaultRotation = defaultRotation
    }

    fun getAllTextures(): Array<String> {
        return arrayOf(textureTop, textureBottom, textureFront, textureBack, textureLeft, textureRight)
    }

    companion object {

        fun hasMaterial(material: Material): Boolean {
            return values().any { it.material == material }
        }

        fun getFromMaterial(material: Material): BlockTexture {
            return values().first { it.material == material }
        }

    }

}

