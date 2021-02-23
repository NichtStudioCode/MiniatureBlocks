package de.studiocode.miniatureblocks.build

import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.advance
import de.studiocode.miniatureblocks.util.isGlass
import de.studiocode.miniatureblocks.util.isSeeTrough
import org.bukkit.Location
import org.bukkit.block.Block

class BuildData {
    
    val data: List<BuildBlockData>
    val size: Int
    
    constructor(data: List<BuildBlockData>, size: Int = 16) {
        this.data = data
        this.size = size
    }
    
    constructor(min: Location, max: Location) {
        val data = ArrayList<BuildBlockData>()
        
        val locations = getAllLocations(min, max)
            .filter { BlockTexture.has(it.block.type) } // remove all blocks that have no miniature version
        
        locations.forEach { location ->
            val block = location.block
            val material = block.type
            val blockedSides = ArrayList<Direction>()
            if (!material.isSeeTrough() || material.isGlass()) { // only check for neighbors if it is a full opaque block or glass
                for (direction in Direction.values()) {
                    val clonedLocation = block.location.clone()
                    clonedLocation.advance(direction)
                    if (locations.contains(clonedLocation)) { // is it still in the building area
                        val neighborMaterial = clonedLocation.block.type
                        if (material.isGlass()) {
                            // don't render glass side if glass blocks of the same glass type are side by side
                            if (material == neighborMaterial) blockedSides.add(direction)
                        } else if (!neighborMaterial.isSeeTrough()) {
                            // mark side as blocked if it isn't visible
                            if (!neighborMaterial.isSeeTrough()) blockedSides.add(direction)
                        }
                    }
                }
                
                val x = location.blockX - min.blockX
                val y = location.blockY - min.blockY
                val z = location.blockZ - min.blockZ
                
                val buildBlockData = BuildBlockData(x, y, z, block, blockedSides)
                if (!buildBlockData.isSurroundedByBlocks()) data.add(buildBlockData)
            }
        }
        
        this.data = data
        this.size = max.blockX - min.blockX
    }
    
    private fun getAllLocations(min: Location, max: Location): List<Location> {
        val locations = ArrayList<Location>()
        for (x in min.blockX..max.blockX) {
            for (y in min.blockY..max.blockY) {
                for (z in min.blockZ..max.blockZ) {
                    locations += Location(min.world, x.toDouble(), y.toDouble(), z.toDouble())
                }
            }
        }
        
        return locations
    }
    
    class BuildBlockData(val x: Int, val y: Int, val z: Int, val block: Block, private val blockedSides: List<Direction>) {
        
        fun isSurroundedByBlocks(): Boolean = blockedSides.size == 6
        
        fun isSideVisible(side: Direction) = !blockedSides.contains(side)
        
    }
    
}