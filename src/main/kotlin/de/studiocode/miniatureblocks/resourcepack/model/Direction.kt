package de.studiocode.miniatureblocks.resourcepack.model

import org.bukkit.Axis
import org.bukkit.Axis.*
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
        BlockFace.NORTH, Z
    ),
    
    EAST(
        lazy { WEST },
        xRot = 0, yRot = 1,
        stepX = 1, stepY = 0, stepZ = 0,
        BlockFace.EAST, X
    ),
    
    SOUTH(
        lazy { NORTH },
        xRot = 0, yRot = 2,
        stepX = 0, stepY = 0, stepZ = 1,
        BlockFace.SOUTH, Z
    ),
    
    WEST(
        lazy { EAST },
        xRot = 0, yRot = 3,
        stepX = -1, stepY = 0, stepZ = 0,
        BlockFace.WEST, X
    ),
    
    UP(
        lazy { DOWN },
        xRot = 1, yRot = 0,
        stepX = 0, stepY = 1, stepZ = 0,
        BlockFace.UP, Y
    ),
    
    DOWN(
        lazy { UP },
        xRot = 3, yRot = 0,
        stepX = 0, stepY = -1, stepZ = 0,
        BlockFace.DOWN, Y
    );
    
    val modelDataName = name.lowercase()
    val opposite by opposite
    
    companion object {
        
        val cardinalPoints = values().filter { it.stepY == 0 }
        
        fun of(blockFace: BlockFace): Direction = values().first { it.blockFace == blockFace }
        
        fun of(axis: Axis) =
            when (axis) {
                X -> EAST
                Y -> UP
                Z -> NORTH
            }
        
        fun ofRotation(blockFace: BlockFace) =
            when (blockFace) {
                BlockFace.NORTH -> NORTH to 0.0
                BlockFace.EAST -> EAST to 0.0
                BlockFace.SOUTH -> SOUTH to 0.0
                BlockFace.WEST -> WEST to 0.0
                BlockFace.UP -> UP to 0.0
                BlockFace.DOWN -> DOWN to 0.0
                
                BlockFace.NORTH_NORTH_EAST -> NORTH to -22.5
                BlockFace.NORTH_EAST -> NORTH to -45.0
                BlockFace.EAST_NORTH_EAST -> EAST to 22.5
                BlockFace.EAST_SOUTH_EAST -> EAST to -22.5
                BlockFace.SOUTH_EAST -> EAST to -45.0
                BlockFace.SOUTH_SOUTH_EAST -> SOUTH to 22.5
                BlockFace.SOUTH_SOUTH_WEST -> SOUTH to -22.5
                BlockFace.SOUTH_WEST -> SOUTH to -45.0
                BlockFace.WEST_SOUTH_WEST -> WEST to 22.5
                BlockFace.WEST_NORTH_WEST -> WEST to -22.5
                BlockFace.NORTH_WEST -> WEST to -45.0
                BlockFace.NORTH_NORTH_WEST -> NORTH to 22.5
                
                else -> NORTH to 0.0
            }
        
    }
    
}