package de.studiocode.miniatureblocks.resourcepack.model

import org.bukkit.Axis
import org.bukkit.block.BlockFace

enum class Direction(
    val xRot: Int, val yRot: Int,
    val stepX: Int, val stepY: Int, val stepZ: Int,
    val blockFace: BlockFace, val axis: Axis
) {
    
    NORTH(
        xRot = 0, yRot = 0,
        stepX = 0, stepY = 0, stepZ = -1,
        BlockFace.NORTH, Axis.Z
    ),
    
    EAST(
        xRot = 0, yRot = 1,
        stepX = 1, stepY = 0, stepZ = 0,
        BlockFace.EAST, Axis.X
    ),
    
    SOUTH(
        xRot = 0, yRot = 2,
        stepX = 0, stepY = 0, stepZ = 1,
        BlockFace.SOUTH, Axis.Z
    ),
    
    WEST(
        xRot = 0, yRot = 3,
        stepX = -1, stepY = 0, stepZ = 0,
        BlockFace.WEST, Axis.X
    ),
    
    UP(
        xRot = 1, yRot = 0,
        stepX = 0, stepY = 1, stepZ = 0,
        BlockFace.UP, Axis.Y
    ),
    
    DOWN(
        xRot = 3, yRot = 0,
        stepX = 0, stepY = -1, stepZ = 0,
        BlockFace.DOWN, Axis.Y
    );
    
    val modelDataName = name.toLowerCase()
    
    companion object {
        
        fun of(blockFace: BlockFace): Direction = values().first { it.blockFace == blockFace }
        fun of(axis: Axis): Direction = values().first { it.axis == axis }
        
    }
    
}