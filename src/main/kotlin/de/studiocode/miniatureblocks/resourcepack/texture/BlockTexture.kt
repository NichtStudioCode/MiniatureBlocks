package de.studiocode.miniatureblocks.resourcepack.texture

import de.studiocode.miniatureblocks.resourcepack.model.parser.Direction
import de.studiocode.miniatureblocks.resourcepack.model.parser.Direction.NORTH
import de.studiocode.miniatureblocks.resourcepack.model.parser.Direction.UP
import org.bukkit.Material

@Suppress("unused")
enum class BlockTexture {
    
    ACACIA_LOG("ACACIA_LOG", "acacia_log_top", "acacia_log", UP),
    ACACIA_PLANKS("ACACIA_PLANKS"),
    ACACIA_WOOD("ACACIA_WOOD", "acacia_log", UP),
    ANCIENT_DEBRIS("ANCIENT_DEBRIS", "ancient_debris_top", "ancient_debris_side"),
    ANDESITE("ANDESITE"),
    BARREL("BARREL", "barrel_top", "barrel_bottom", "barrel_side"),
    BASALT("BASALT", "basalt_top", "basalt_side", UP),
    BEACON("BEACON"), //not really
    BEDROCK("BEDROCK"),
    BEEHIVE("BEEHIVE", "beehive_end", "beehive_end", "beehive_front", "beehive_side"),
    BEE_NEST("BEE_NEST", "bee_nest_top", "bee_nest_bottom", "bee_nest_front", "bee_nest_side"),
    BIRCH_LOG("BIRCH_LOG", "birch_log_top", "birch_log", UP),
    BIRCH_PLANKS("BIRCH_PLANKS"),
    BIRCH_WOOD("BIRCH_WOOD", "birch_log", UP),
    BLACKSTONE("BLACKSTONE", "blackstone_top", "blackstone"),
    BLACK_CONCRETE("BLACK_CONCRETE"),
    BLACK_GLAZED_TERRACOTTA("BLACK_GLAZED_TERRACOTTA"),
    BLACK_SHULKER_BOX("BLACK_SHULKER_BOX"), //has multiple textures in-game but i could only find one texture in the assets
    BLACK_STAINED_GLASS("BLACK_STAINED_GLASS"),
    BLACK_TERRACOTTA("BLACK_TERRACOTTA"),
    BLACK_WOOL("BLACK_WOOL"),
    BLAST_FURNACE("BLAST_FURNACE", true, false, true),
    BLUE_CONCRETE("BLUE_CONCRETE"),
    BLUE_GLAZED_TERRACOTTA("BLUE_GLAZED_TERRACOTTA"),
    BLUE_ICE("BLUE_ICE"),
    BLUE_SHULKER_BOX("BLUE_SHULKER_BOX"),
    BLUE_STAINED_GLASS("BLUE_STAINED_GLASS"),
    BLUE_TERRACOTTA("BLUE_TERRACOTTA"),
    BLUE_WOOL("BLUE_WOOL"),
    BONE_BLOCK("BONE_BLOCK", true, false, false, UP),
    BOOKSHELF("BOOKSHELF", "oak_planks", "bookshelf"),
    BRAIN_CORAL_BLOCK("BRAIN_CORAL_BLOCK"),
    BRICKS("BRICKS"),
    BROWN_CONCRETE("BROWN_CONCRETE"),
    BROWN_GLAZED_TERRACOTTA("BROWN_GLAZED_TERRACOTTA"),
    BROWN_MUSHROOM_BLOCK("BROWN_MUSHROOM_BLOCK"),
    BROWN_SHULKER_BOX("BROWN_SHULKER_BOX"),
    BROWN_STAINED_GLASS("BROWN_STAINED_GLASS"),
    BROWN_TERRACOTTA("BROWN_TERRACOTTA"),
    BROWN_WOOL("BROWN_WOOL"),
    BUBBLE_CORAL_BLOCK("BUBBLE_CORAL_BLOCK"),
    CARTOGRAPHY_TABLE("CARTOGRAPHY_TABLE", "cartography_table_top", "dark_oak_planks", "cartography_table_side1", "cartography_table_side3", "cartography_table_side3", "cartography_table_side2"),
    CARVED_PUMPKIN("CARVED_PUMPKIN", "pumpkin_top", "pumpkin_top", "carved_pumpkin", "pumpkin_side"),
    CHISELED_NETHER_BRICKS("CHISELED_NETHER_BRICKS"),
    CHISELED_POLISHED_BLACKSTONE("CHISELED_POLISHED_BLACKSTONE"),
    CHISELED_QUARTZ_BLOCK("CHISELED_QUARTZ_BLOCK", "chiseled_quartz_block_top", "chiseled_quartz_block"),
    CHISELED_RED_SANDSTONE("CHISELED_RED_SANDSTONE"),
    CHISELED_SANDSTONE("CHISELED_SANDSTONE"),
    CHISELED_STONE_BRICKS("CHISELED_STONE_BRICKS"),
    CLAY("CLAY"),
    BLOCK_OF_COAL("COAL_BLOCK"),
    COAL_ORE("COAL_ORE"),
    COARSE_DIRT("COARSE_DIRT"),
    COBBLESTONE("COBBLESTONE"),
    CRACKED_NETHER_BRICKS("CRACKED_NETHER_BRICKS"),
    CRACKED_POLISHED_BLACKSTONE_BRICKS("CRACKED_POLISHED_BLACKSTONE_BRICKS"),
    CRACKED_STONE_BRICKS("CRACKED_STONE_BRICKS"),
    CRAFTING_TABLE("CRAFTING_TABLE", "crafting_table_top", "oak_planks", "crafting_table_front", "crafting_table_side"),
    CRIMSON_HYPHAE("CRIMSON_HYPHAE, UP"),
    CRIMSON_NYLIUM("CRIMSON_NYLIUM", "crimson_nylium", "netherrack", "crimson_nylium_side"),
    CRIMSON_PLANKS("CRIMSON_PLANKS"),
    CRIMSON_STEM("CRIMSON_STEM", "crimson_stem_top", "crimson_stem_top", "crimson_stem", UP),
    CRYING_OBSIDIAN("CRYING_OBSIDIAN"),
    CUT_RED_SANDSTONE("CUT_RED_SANDSTONE", "red_sandstone_top", "red_sandstone_top", "cut_red_sandstone"),
    CUT_SANDSTONE("CUT_SANDSTONE", "sandstone_top", "sandstone_top", "cut_sandstone"),
    CYAN_CONCRETE("CYAN_CONCRETE"),
    CYAN_GLAZED_TERRACOTTA("CYAN_GLAZED_TERRACOTTA"),
    CYAN_SHULKER_BOX("CYAN_SHULKER_BOX"),
    CYAN_STAINED_GLASS("CYAN_STAINED_GLASS"),
    CYAN_TERRACOTTA("CYAN_TERRACOTTA"),
    CYAN_WOOL("CYAN_WOOL"),
    DARK_OAK_LOG("DARK_OAK_LOG", "dark_oak_log_top", "dark_oak_log", UP),
    DARK_OAK_PLANKS("DARK_OAK_PLANKS"),
    DARK_OAK_WOOD("DARK_OAK_WOOD", "dark_oak_log", UP),
    DARK_PRISMARINE("DARK_PRISMARINE"),
    DEAD_BRAIN_CORAL_BLOCK("DEAD_BRAIN_CORAL_BLOCK"),
    DEAD_BUBBLE_CORAL_BLOCK("DEAD_BUBBLE_CORAL_BLOCK"),
    DEAD_FIRE_CORAL_BLOCK("DEAD_FIRE_CORAL_BLOCK"),
    DEAD_HORN_CORAL_BLOCK("DEAD_HORN_CORAL_BLOCK"),
    DEAD_TUBE_CORAL("DEAD_TUBE_CORAL"),
    DIAMOND_BLOCK("DIAMOND_BLOCK"),
    DIAMOND_ORE("DIAMOND_ORE"),
    DIORITE("DIORITE"),
    DIRT("DIRT"),
    DISPENSER("DISPENSER", "furnace_top", "furnace_top", "dispenser_front", "furnace_side"),
    DRIED_KELP_BLOCK("DRIED_KELP_BLOCK", "dried_kelp_top", "dried_kelp_bottom", "dried_kelp_side"),
    DROPPER("DROPPER", "furnace_top", "furnace_top", "dropper_front", "furnace_side"),
    EMERALD_BLOCK("EMERALD_BLOCK"),
    EMERALD_ORE("EMERALD_ORE"),
    END_STONE("END_STONE"),
    END_STONE_BRICKS("END_STONE_BRICKS"),
    FARMLAND("FARMLAND", "farmland_moist", "dirt", "dirt"),
    FIRE_CORAL_BLOCK("FIRE_CORAL_BLOCK"),
    FLETCHING_TABLE("FLETCHING_TABLE", "fletching_table_top", "birch_planks", "fletching_table_front", "fletching_table_front", "fletching_table_side", "fletching_table_side"),
    FURNACE("FURNACE", "furnace_top", "furnace_top", "furnace_front", "furnace_side"),
    GILDED_BLACKSTONE("GILDED_BLACKSTONE"),
    GLASS("GLASS"),
    GLOWSTONE("GLOWSTONE"),
    GOLD_BLOCK("GOLD_BLOCK"),
    GOLD_ORE("GOLD_ORE"),
    GRANITE("GRANITE"),
    GRASS_BLOCK("GRASS_BLOCK", "modded/grass_block_top", "dirt", "grass_block_side"),
    GRASS_PATH("GRASS_PATH", "grass_path_top", "dirt", "grass_path_side"),
    GRAY_CONCRETE("GRAY_CONCRETE"),
    GRAY_GLAZED_TERRACOTTA("GRAY_GLAZED_TERRACOTTA"),
    GRAY_SHULKER_BOX("GRAY_SHULKER_BOX"),
    GRAY_STAINED_GLASS("GRAY_STAINED_GLASS"),
    GRAY_TERRACOTTA("GRAY_TERRACOTTA"),
    GRAY_WOOL("GRAY_WOOL"),
    GREEN_CONCRETE("GREEN_CONCRETE"),
    GREEN_GLAZED_TERRACOTTA("GREEN_GLAZED_TERRACOTTA"),
    GREEN_SHULKER_BOX("GREEN_SHULKER_BOX"),
    GREEN_STAINED_GLASS("GREEN_STAINED_GLASS"),
    GREEN_TERRACOTTA("GREEN_TERRACOTTA"),
    GREEN_WOOL("GREEN_WOOL"),
    HAY_BLOCK("HAY_BLOCK", true, false, false, UP),
    HONEYCOMB_BLOCK("HONEYCOMB_BLOCK"),
    HONEY_BLOCK("HONEY_BLOCK"),
    HORN_CORAL_BLOCK("HORN_CORAL_BLOCK"),
    ICE("ICE"),
    IRON_BLOCK("IRON_BLOCK"),
    IRON_ORE("IRON_ORE"),
    JACK_O_LANTERN("JACK_O_LANTERN", "pumpkin_top", "pumpkin_top", "jack_o_lantern", "pumpkin_side"),
    JIGSAW_BLOCK("JIGSAW", true, true, false),
    JUKEBOX("JUKEBOX", "jukebox_top", "jukebox_side", "jukebox_side"),
    JUNGLE_LOG("JUNGLE_LOG", "jungle_log_top", "jungle_log", UP),
    JUNGLE_PLANKS("JUNGLE_PLANKS"),
    JUNGLE_WOOD("JUNGLE_WOOD", "jungle_log", UP),
    LAPIS_BLOCK("LAPIS_BLOCK"),
    LAPIS_ORE("LAPIS_ORE"),
    LIGHT_BLUE_CONCRETE("LIGHT_BLUE_CONCRETE"),
    LIGHT_BLUE_GLAZED_TERRACOTTA("LIGHT_BLUE_GLAZED_TERRACOTTA"),
    LIGHT_BLUE_SHULKER_BOX("LIGHT_BLUE_SHULKER_BOX"),
    LIGHT_BLUE_STAINED_GLASS("LIGHT_BLUE_STAINED_GLASS"),
    LIGHT_BLUE_TERRACOTTA("LIGHT_BLUE_TERRACOTTA"),
    LIGHT_BLUE_WOOL("LIGHT_BLUE_WOOL"),
    LIGHT_GRAY_CONCRETE("LIGHT_GRAY_CONCRETE"),
    LIGHT_GRAY_GLAZED_TERRACOTTA("LIGHT_GRAY_GLAZED_TERRACOTTA"),
    LIGHT_GRAY_SHULKER_BOX("LIGHT_GRAY_SHULKER_BOX"),
    LIGHT_GRAY_STAINED_GLASS("LIGHT_GRAY_STAINED_GLASS"),
    LIGHT_GRAY_TERRACOTTA("LIGHT_GRAY_TERRACOTTA"),
    LIGHT_GRAY_WOOL("LIGHT_GRAY_WOOL"),
    LIME_CONCRETE("LIME_CONCRETE"),
    LIME_GLAZED_TERRACOTTA("LIME_GLAZED_TERRACOTTA"),
    LIME_SHULKER_BOX("LIME_SHULKER_BOX"),
    LIME_STAINED_GLASS("LIME_STAINED_GLASS"),
    LIME_TERRACOTTA("LIME_TERRACOTTA"),
    LIME_WOOL("LIME_WOOL"),
    LODESTONE("LODESTONE", true, false, false),
    LOOM("LOOM", true, true, true),
    MAGENTA_CONCRETE("MAGENTA_CONCRETE"),
    MAGENTA_GLAZED_TERRACOTTA("MAGENTA_GLAZED_TERRACOTTA"),
    MAGENTA_SHULKER_BOX("MAGENTA_SHULKER_BOX"),
    MAGENTA_STAINED_GLASS("MAGENTA_STAINED_GLASS"),
    MAGENTA_TERRACOTTA("MAGENTA_TERRACOTTA"),
    MAGENTA_WOOL("MAGENTA_WOOL"),
    MAGMA_BLOCK("MAGMA_BLOCK"),
    MELON("MELON", true, false, true),
    MOSSY_COBBLESTONE("MOSSY_COBBLESTONE"),
    MOSSY_STONE_BRICKS("MOSSY_STONE_BRICKS"),
    MUSHROOM_STEM("MUSHROOM_STEM"),
    MYCELIUM("MYCELIUM", "mycelium_top", "dirt", "mycelium_side"),
    NETHERITE_BLOCK("NETHERITE_BLOCK"),
    NETHERRACK("NETHERRACK"),
    NETHER_BRICKS("NETHER_BRICKS"),
    NETHER_GOLD_ORE("NETHER_GOLD_ORE"),
    NETHER_QUARTZ_ORE("NETHER_QUARTZ_ORE"),
    NETHER_WART_BLOCK("NETHER_WART_BLOCK"),
    NOTE_BLOCK("NOTE_BLOCK"),
    OAK_LOG("OAK_LOG", "oak_log_top", "oak_log", UP),
    OAK_PLANKS("OAK_PLANKS"),
    OAK_WOOD("OAK_WOOD", "oak_log", UP),
    OBSIDIAN("OBSIDIAN"),
    ORANGE_CONCRETE("ORANGE_CONCRETE"),
    ORANGE_GLAZED_TERRACOTTA("ORANGE_GLAZED_TERRACOTTA"),
    ORANGE_SHULKER_BOX("ORANGE_SHULKER_BOX"),
    ORANGE_STAINED_GLASS("ORANGE_STAINED_GLASS"),
    ORANGE_TERRACOTTA("ORANGE_TERRACOTTA"),
    ORANGE_WOOL("ORANGE_WOOL"),
    PACKED_ICE("PACKED_ICE"),
    PINK_CONCRETE("PINK_CONCRETE"),
    PINK_GLAZED_TERRACOTTA("PINK_GLAZED_TERRACOTTA"),
    PINK_SHULKER_BOX("PINK_SHULKER_BOX"),
    PINK_STAINED_GLASS("PINK_STAINED_GLASS"),
    PINK_TERRACOTTA("PINK_TERRACOTTA"),
    PINK_WOOL("PINK_WOOL"),
    PISTON("PISTON", true, true, false, UP),
    PODZOL("PODZOL", "podzol_top", "dirt", "podzol_side"),
    POLISHED_ANDESITE("POLISHED_ANDESITE"),
    POLISHED_BASALT("POLISHED_BASALT", true, false, false, UP),
    POLISHED_BLACKSTONE("POLISHED_BLACKSTONE"),
    POLISHED_BLACKSTONE_BRICKS("POLISHED_BLACKSTONE_BRICKS"),
    POLISHED_DIORITE("POLISHED_DIORITE"),
    POLISHED_GRANITE("POLISHED_GRANITE"),
    PRISMARINE("PRISMARINE"),
    PRISMARINE_BRICKS("PRISMARINE_BRICKS"),
    PUMPKIN("PUMPKIN", true, false, false),
    PURPLE_CONCRETE("PURPLE_CONCRETE"),
    PURPLE_GLAZED_TERRACOTTA("PURPLE_GLAZED_TERRACOTTA"),
    PURPLE_SHULKER_BOX("PURPLE_SHULKER_BOX"),
    PURPLE_STAINED_GLASS("PURPLE_STAINED_GLASS"),
    PURPLE_TERRACOTTA("PURPLE_TERRACOTTA"),
    PURPLE_WOOL("PURPLE_WOOL"),
    PURPUR_BLOCK("PURPUR_BLOCK"),
    PURPUR_PILLAR("PURPUR_PILLAR", "purpur_pillar_top", "purpur_pillar", UP),
    QUARTZ_BLOCK("QUARTZ_BLOCK", "quartz_block_top"),
    QUARTZ_BRICKS("QUARTZ_BRICKS"),
    QUARTZ_PILLAR("QUARTZ_PILLAR", "quartz_pillar_top", "quartz_pillar", UP),
    REDSTONE_BLOCK("REDSTONE_BLOCK"),
    REDSTONE_LAMP("REDSTONE_LAMP"),
    REDSTONE_ORE("REDSTONE_ORE"),
    RED_CONCRETE("RED_CONCRETE"),
    RED_GLAZED_TERRACOTTA("RED_GLAZED_TERRACOTTA"),
    RED_MUSHROOM_BLOCK("RED_MUSHROOM_BLOCK"),
    RED_NETHER_BRICKS("RED_NETHER_BRICKS"),
    RED_SANDSTONE("RED_SANDSTONE", "red_sandstone_top", "red_sandstone_bottom", "red_sandstone"),
    RED_SHULKER_BOX("RED_SHULKER_BOX"),
    RED_STAINED_GLASS("RED_STAINED_GLASS"),
    RED_TERRACOTTA("RED_TERRACOTTA"),
    RED_WOOL("RED_WOOL"),
    RESPAWN_ANCHOR("RESPAWN_ANCHOR", "respawn_anchor_top_off", "respawn_anchor_bottom", "respawn_anchor_side0"),
    SANDSTONE("SANDSTONE", "sandstone_top", "red_sandstone_bottom", "sandstone"),
    SEA_LANTERN("SEA_LANTERN"),
    SHROOMLIGHT("SHROOMLIGHT"),
    SHULKER_BOX("SHULKER_BOX"),
    SLIME_BLOCK("SLIME_BLOCK"),
    SMITHING_TABLE("SMITHING_TABLE", "smithing_table_top", "smithing_table_bottom", "smithing_table_front", "smithing_table_front", "smithing_table_side", "smithing_table_side"),
    SMOKER("SMOKER", true, true, true),
    SMOOTH_QUARTZ("SMOOTH_QUARTZ", "quartz_block_bottom"),
    SMOOTH_RED_SANDSTONE("SMOOTH_RED_SANDSTONE", "red_sandstone_top"),
    SMOOTH_SANDSTONE("SMOOTH_SANDSTONE", "sandstone_top"),
    SMOOTH_STONE("SMOOTH_STONE"),
    SNOW_BLOCK("SNOW_BLOCK", "snow"),
    SOUL_SAND("SOUL_SAND"),
    SOUL_SOIL("SOUL_SOIL"),
    SPAWNER("SPAWNER"),
    SPONGE("SPONGE"),
    SPRUCE_LOG("SPRUCE_LOG", "spruce_log_top", "spruce_log", UP),
    SPRUCE_PLANKS("SPRUCE_PLANKS"),
    SPRUCE_WOOD("SPRUCE_WOOD", "spruce_log", UP),
    STICKY_PISTON("STICKY_PISTON", "piston_top_sticky", "piston_bottom", "piston_side", UP),
    STONE("STONE"),
    STONE_BRICKS("STONE_BRICKS"),
    STRIPPED_ACACIA_LOG("STRIPPED_ACACIA_LOG", "stripped_acacia_log_top", "stripped_acacia_log", UP),
    STRIPPED_ACACIA_WOOD("STRIPPED_ACACIA_WOOD", "stripped_acacia_log", UP),
    STRIPPED_BIRCH_LOG("STRIPPED_BIRCH_LOG", "stripped_birch_log_top", "stripped_birch_log", UP),
    STRIPPED_BIRCH_WOOD("STRIPPED_BIRCH_WOOD", "stripped_birch_log", UP),
    STRIPPED_CRIMSON_HYPHAE("STRIPPED_CRIMSON_HYPHAE", "stripped_crimson_stem", UP),
    STRIPPED_CRIMSON_STEM("STRIPPED_CRIMSON_STEM", "stripped_crimson_stem_top", "stripped_crimson_stem", UP),
    STRIPPED_DARK_OAK_LOG("STRIPPED_DARK_OAK_LOG", "stripped_dark_oak_log_top", "stripped_dark_oak_log", UP),
    STRIPPED_DARK_OAK_WOOD("STRIPPED_DARK_OAK_WOOD", "stripped_dark_oak_log", UP),
    STRIPPED_JUNGLE_LOG("STRIPPED_JUNGLE_LOG", "stripped_jungle_log_top", "stripped_jungle_log", UP),
    STRIPPED_JUNGLE_WOOD("STRIPPED_JUNGLE_WOOD", "stripped_jungle_log", UP),
    STRIPPED_OAK_LOG("STRIPPED_OAK_LOG", "stripped_oak_log_top", "stripped_oak_log", UP),
    STRIPPED_OAK_WOOD("STRIPPED_OAK_WOOD", "stripped_oak_log", UP),
    STRIPPED_SPRUCE_LOG("STRIPPED_SPRUCE_LOG", "stripped_spruce_log_top", "stripped_spruce_log", UP),
    STRIPPED_SPRUCE_WOOD("STRIPPED_SPRUCE_WOOD", "stripped_spruce_log", UP),
    STRIPPED_WARPED_HYPHAE("STRIPPED_WARPED_HYPHAE", "stripped_warped_stem", UP),
    STRIPPED_WARPED_STEM("STRIPPED_WARPED_STEM", "stripped_warped_stem_top", "stripped_warped_stem", UP),
    STRUCTURE_BLOCK("STRUCTURE_BLOCK"),
    TARGET("TARGET"),
    TERRACOTTA("TERRACOTTA"),
    TNT("TNT", true, true, false),
    DEAD_TUBE_CORAL_BLOCK("TUBE_CORAL_BLOCK"),
    WARPED_HYPHAE("WARPED_HYPHAE", UP),
    WARPED_NYLIUM("WARPED_NYLIUM", "warped_nylium", "netherrack", "warped_nylium_side"),
    WARPED_PLANKS("WARPED_PLANKS"),
    WARPED_STEM("WARPED_STEM", "warped_stem_top", "warped_stem", UP),
    WARPED_WART_BLOCK("WARPED_WART_BLOCK"),
    WATER("WATER"),
    WET_SPONGE("WET_SPONGE"),
    WHITE_CONCRETE("WHITE_CONCRETE"),
    WHITE_GLAZED_TERRACOTTA("WHITE_GLAZED_TERRACOTTA"),
    WHITE_SHULKER_BOX("WHITE_SHULKER_BOX"),
    WHITE_STAINED_GLASS("WHITE_STAINED_GLASS"),
    WHITE_TERRACOTTA("WHITE_TERRACOTTA"),
    WHITE_WOOL("WHITE_WOOL"),
    YELLOW_CONCRETE("YELLOW_CONCRETE"),
    YELLOW_GLAZED_TERRACOTTA("YELLOW_GLAZED_TERRACOTTA"),
    YELLOW_SHULKER_BOX("YELLOW_SHULKER_BOX"),
    YELLOW_STAINED_GLASS("YELLOW_STAINED_GLASS"),
    YELLOW_TERRACOTTA("YELLOW_TERRACOTTA"),
    YELLOW_WOOL("YELLOW_WOOL"),
    WHITE_CONCRETE_POWDER("WHITE_CONCRETE_POWDER"),
    ORANGE_CONCRETE_POWDER("ORANGE_CONCRETE_POWDER"),
    MAGENTA_CONCRETE_POWDER("MAGENTA_CONCRETE_POWDER"),
    LIGHT_BLUE_CONCRETE_POWDER("LIGHT_BLUE_CONCRETE_POWDER"),
    YELLOW_CONCRETE_POWDER("YELLOW_CONCRETE_POWDER"),
    LIME_CONCRETE_POWDER("LIME_CONCRETE_POWDER"),
    PINK_CONCRETE_POWDER("PINK_CONCRETE_POWDER"),
    GRAY_CONCRETE_POWDER("GRAY_CONCRETE_POWDER"),
    LIGHT_GRAY_CONCRETE_POWDER("LIGHT_GRAY_CONCRETE_POWDER"),
    CYAN_CONCRETE_POWDER("CYAN_CONCRETE_POWDER"),
    PURPLE_CONCRETE_POWDER("PURPLE_CONCRETE_POWDER"),
    BLUE_CONCRETE_POWDER("BLUE_CONCRETE_POWDER"),
    BROWN_CONCRETE_POWDER("BROWN_CONCRETE_POWDER"),
    GREEN_CONCRETE_POWDER("GREEN_CONCRETE_POWDER"),
    RED_CONCRETE_POWDER("RED_CONCRETE_POWDER"),
    BLACK_CONCRETE_POWDER("BLACK_CONCRETE_POWDER"),
    SAND("SAND"),
    GRAVEL("GRAVEL"),
    OAK_SLAB("OAK_SLAB", "oak_planks"),
    SPRUCE_SLAB("SPRUCE_SLAB", "spruce_planks"),
    BIRCH_SLAB("BIRCH_SLAB", "birch_planks"),
    JUNGLE_SLAB("JUNGLE_SLAB", "jungle_planks"),
    ACACIA_SLAB("ACACIA_SLAB", "acacia_planks"),
    DARK_OAK_SLAB("DARK_OAK_SLAB", "dark_oak_planks"),
    CRIMSON_SLAB("CRIMSON_SLAB", "crimson_planks"),
    WARPED_SLAB("WARPED_SLAB", "warped_planks"),
    STONE_SLAB("STONE_SLAB", "stone"),
    SMOOTH_STONE_SLAB("SMOOTH_STONE_SLAB", "smooth_stone", "smooth_stone_slab_side"),
    SANDSTONE_SLAB("SANDSTONE_SLAB", "sandstone_top", "sandstone_bottom", "sandstone"),
    CUT_SANDSTONE_SLAB("CUT_SANDSTONE_SLAB", "sandstone_top", "cut_sandstone"),
    SMOOTH_SANDSTONE_SLAB("SMOOTH_SANDSTONE_SLAB", "sandstone_top"),
    RED_SANDSTONE_SLAB("RED_SANDSTONE_SLAB", "red_sandstone_top", "red_sandstone_bottom", "red_sandstone"),
    CUT_RED_SANDSTONE_SLAB("CUT_RED_SANDSTONE_SLAB", "red_sandstone_top", "cut_red_sandstone"),
    SMOOTH_RED_SANDSTONE_SLAB("SMOOTH_RED_SANDSTONE_SLAB", "red_sandstone_top"),
    PETRIFIED_OAK_SLAB("PETRIFIED_OAK_SLAB", "oak_planks"),
    COBBLESTONE_SLAB("COBBLESTONE_SLAB", "cobblestone"),
    BRICK_SLAB("BRICK_SLAB", "bricks"),
    STONE_BRICK_SLAB("STONE_BRICK_SLAB", "stone_bricks"),
    NETHER_BRICK_SLAB("NETHER_BRICK_SLAB", "nether_bricks"),
    QUARTZ_SLAB("QUARTZ_SLAB", "quartz_block_top"),
    SMOOTH_QUARTZ_SLAB("SMOOTH_QUARTZ_SLAB", "quartz_block_bottom"),
    PURPUR_SLAB("PURPUR_SLAB", "purpur_block"),
    PRISMARINE_SLAB("PRISMARINE_SLAB", "prismarine"),
    PRISMARINE_BRICK_SLAB("PRISMARINE_BRICK_SLAB", "prismarine_bricks"),
    DARK_PRISMARINE_SLAB("DARK_PRISMARINE_SLAB", "dark_prismarine"),
    POLISHED_GRANITE_SLAB("POLISHED_GRANITE_SLAB", "polished_granite"),
    POLISHED_DIORITE_SLAB("POLISHED_DIORITE_SLAB", "polished_diorite"),
    POLISHED_ANDESITE_SLAB("POLISHED_ANDESITE_SLAB", "polished_andesite"),
    POLISHED_BLACKSTONE_SLAB("POLISHED_BLACKSTONE_SLAB", "polished_blackstone"),
    POLISHED_BLACKSTONE_BRICK_SLAB("POLISHED_BLACKSTONE_BRICK_SLAB", "polished_blackstone_bricks"),
    MOSSY_STONE_BRICK_SLAB("MOSSY_STONE_BRICK_SLAB", "mossy_stone_bricks"),
    MOSSY_COBBLESTONE_SLAB("MOSSY_COBBLESTONE_SLAB", "mossy_cobblestone"),
    END_STONE_BRICK_SLAB("END_STONE_BRICK_SLAB", "end_stone_bricks"),
    GRANITE_SLAB("GRANITE_SLAB", "granite"),
    ANDESITE_SLAB("ANDESITE_SLAB", "andesite"),
    RED_NETHER_BRICK_SLAB("RED_NETHER_BRICK_SLAB", "nether_bricks"),
    DIORITE_SLAB("DIORITE_SLAB", "diorite"),
    BLACKSTONE_SLAB("BLACKSTONE_SLAB", "blackstone_top", "blackstone");
    
    val material: Material?
    val textureTop: String
    val textureBottom: String
    val textureFront: String
    val textureBack: String
    val textureRight: String
    val textureLeft: String
    val defaultRotation: Direction
    
    constructor(material: String, textureTop: String, textureBottom: String, textureFront: String, textureBack: String,
                textureRight: String, textureLeft: String, defaultRotation: Direction = NORTH) {
        
        this.material = findMaterialByName(material)
        this.textureTop = "block/$textureTop"
        this.textureBottom = "block/$textureBottom"
        this.textureFront = "block/$textureFront"
        this.textureBack = "block/$textureBack"
        this.textureRight = "block/$textureRight"
        this.textureLeft = "block/$textureLeft"
        this.defaultRotation = defaultRotation
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
    
    constructor(material: String, top: Boolean, bottom: Boolean, front: Boolean, defaultRotation: Direction = NORTH) {
        this.material = findMaterialByName(material)
        
        val texture = "block/" + material.toLowerCase()
        
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
    
    private fun findMaterialByName(name: String): Material? {
        return Material.values().find { it.name == name }
    }
    
    companion object {
        
        fun hasMaterial(material: Material): Boolean {
            return values().any { it.material == material }
        }
        
        fun of(material: Material): BlockTexture {
            return values().first { it.material == material }
        }
        
        fun has(material: Material): Boolean = values().any { material == it.material }
        
    }
    
}

