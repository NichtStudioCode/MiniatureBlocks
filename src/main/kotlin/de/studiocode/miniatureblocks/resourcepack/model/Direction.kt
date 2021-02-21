package de.studiocode.miniatureblocks.resourcepack.model

import org.bukkit.Axis
import org.bukkit.block.BlockFace

enum class Direction(val xRot: Int, val yRot: Int, val blockFace: BlockFace, val axis: Axis?) {
    
    NORTH(0, 0, BlockFace.NORTH, Axis.Z),
    EAST(0, 1, BlockFace.EAST, Axis.X),
    SOUTH(0, 2, BlockFace.SOUTH, null),
    WEST(0, 3, BlockFace.WEST, null),
    UP(1, 0, BlockFace.UP, Axis.Y),
    DOWN(3, 0, BlockFace.DOWN, null);
    
    val modelDataName = name.toLowerCase()
    
    companion object {
        
        fun of(blockFace: BlockFace): Direction = values().first { it.blockFace == blockFace }
        fun of(axis: Axis): Direction = values().first { it.axis == axis }
        
    }
    
}