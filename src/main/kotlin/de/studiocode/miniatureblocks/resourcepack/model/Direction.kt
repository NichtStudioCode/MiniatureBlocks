package de.studiocode.miniatureblocks.resourcepack.model

import org.bukkit.Axis
import org.bukkit.block.BlockFace

enum class Direction(
    opposite: Lazy<Direction>,
    val xRot: Int, val yRot: Int,
    val stepX: Int, val stepY: Int, val stepZ: Int,
    val blockFace: BlockFace, val axis: Axis
) {
    
    NORTH(
        lazy { SOUTH },
        xRot = 0, yRot = 0,
        stepX = 0, stepY = 0, stepZ = -1,
        BlockFace.NORTH, Axis.Z
    ),
    
    EAST(
        lazy { WEST },
        xRot = 0, yRot = 1,
        stepX = 1, stepY = 0, stepZ = 0,
        BlockFace.EAST, Axis.X
    ),
    
    SOUTH(
        lazy { NORTH },
        xRot = 0, yRot = 2,
        stepX = 0, stepY = 0, stepZ = 1,
        BlockFace.SOUTH, Axis.Z
    ),
    
    WEST(
        lazy { EAST },
        xRot = 0, yRot = 3,
        stepX = -1, stepY = 0, stepZ = 0,
        BlockFace.WEST, Axis.X
    ),
    
    UP(
        lazy { DOWN },
        xRot = 1, yRot = 0,
        stepX = 0, stepY = 1, stepZ = 0,
        BlockFace.UP, Axis.Y
    ),
    
    DOWN(
        lazy { UP },
        xRot = 3, yRot = 0,
        stepX = 0, stepY = -1, stepZ = 0,
        BlockFace.DOWN, Axis.Y
    );
    
    val modelDataName = name.toLowerCase()
    val opposite by opposite
    
    companion object {
        
        fun of(blockFace: BlockFace): Direction = values().first { it.blockFace == blockFace }
        fun of(axis: Axis): Direction = values().first { it.axis == axis }
        
    }
    
}