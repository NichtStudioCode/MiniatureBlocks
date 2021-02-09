package de.studiocode.miniatureblocks.builderworld

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.ChunkGenerator
import java.util.*

class BuilderWorldGenerator : ChunkGenerator() {
    
    override fun generateChunkData(world: World, random: Random, x: Int, z: Int, biome: BiomeGrid): ChunkData {
        val chunk = createChunkData(world)
        
        val isBuildArea: Boolean =
            x and 1 == 0 && z and 1 == 0 // If the least significant bit is set the number is not even.
        
        chunk.setRegion(0, 0, 0, 16, 1, 16, if (isBuildArea) Material.WHITE_CONCRETE else Material.BLUE_STAINED_GLASS)
        
        if (isBuildArea) {
            chunk.setRegion(7, 0, 0, 9, 1, 16, Material.LIGHT_BLUE_CONCRETE)
            chunk.setRegion(0, 0, 7, 16, 1, 9, Material.LIGHT_BLUE_CONCRETE)
            chunk.setRegion(7, 0, 7, 9, 1, 9, Material.LIGHT_BLUE_CONCRETE)
        }
        
        return chunk
    }
    
}