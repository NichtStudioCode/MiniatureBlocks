package de.studiocode.miniatureblocks.builderworld

import de.studiocode.miniatureblocks.resourcepack.model.parser.Direction
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.utils.advance
import de.studiocode.miniatureblocks.utils.isGlass
import de.studiocode.miniatureblocks.utils.isSeeTrough
import org.bukkit.Axis
import org.bukkit.Chunk
import org.bukkit.block.Block
import org.bukkit.block.BlockFace

class BuildData(chunk: Chunk) {
    
    val size = 16
    val data = ArrayList<BuildBlockData>()
    
    init {
        for (x in 0..15) {
            for (y in 1..16) {
                for (z in 0..15) {
                    val block = chunk.getBlock(x, y, z)
                    val material = block.type
                    if (BlockTexture.has(material)) { // remove all blocks that have no miniature version
                        val blockedSides = ArrayList<Direction>()
                        if (!material.isSeeTrough() || material.isGlass()) { // only check for neighbors if it is a full opaque block or glass
                            for (direction in Direction.values()) {
                                val location = block.location.clone()
                                location.advance(direction)
                                if (location.chunk == chunk && location.y > 0) { // is it still in the building area
                                    val neighborMaterial = location.block.type
                                    if (material.isGlass()) {
                                        // don't render glass side if glass blocks of the same glass type are side by side
                                        if (material == neighborMaterial) blockedSides.add(direction)
                                    } else if (!neighborMaterial.isSeeTrough()) {
                                        // mark side as blocked if it isn't visible
                                        if (!neighborMaterial.isSeeTrough()) blockedSides.add(direction)
                                    }
                                }
                            }
                        }
    
                        val buildBlockData = BuildBlockData(x, y, z, block, blockedSides)
                        if (!buildBlockData.isSurroundedByBlocks()) data.add(buildBlockData)
                    }
                }
            }
        }
    }
    
    class BuildBlockData(val x: Int, val y: Int, val z: Int, val block: Block, private val blockedSides: List<Direction>) {
        
        var facing: BlockFace? = null
        var axis: Axis? = null
        
        fun isSurroundedByBlocks(): Boolean = blockedSides.size == 6
        
        fun isSideVisible(side: Direction) = !blockedSides.contains(side)
        
    }
    
}