package de.studiocode.miniatureblocks.resourcepack.model

import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.utils.shift
import org.bukkit.Axis
import org.bukkit.block.BlockFace

open class Cube(private val blockTexture: BlockTexture) {
    
    val textures = createDefaultTextures()
    private var rotateX = 0
    private var rotateY = 0
    
    init {
        val defaultRotation = blockTexture.defaultRotation
        rotateX -= defaultRotation.xRot
        rotateY -= defaultRotation.yRot
    }
    
    private fun createDefaultTextures(): HashMap<Direction, SideTexture> {
        val defaultTextures = HashMap<Direction, SideTexture>()
        
        for (direction in Direction.values()) {
            defaultTextures[direction] = SideTexture(
                when (direction) {
                    
                    Direction.NORTH -> blockTexture.textureFront
                    Direction.EAST -> blockTexture.textureRight
                    Direction.SOUTH -> blockTexture.textureBack
                    Direction.WEST -> blockTexture.textureLeft
                    Direction.UP -> blockTexture.textureTop
                    Direction.DOWN -> blockTexture.textureBottom
                    
                }
            )
        }
        
        return defaultTextures
    }
    
    fun addRotation(direction: Direction) {
        rotateX += direction.xRot
        rotateY += direction.yRot
    }
    
    fun rotate() {
        rotateAroundXAxis(rotateX)
        rotateAroundYAxis(rotateY)
        
        rotateX = 0
        rotateY = 0
    }
    
    private fun rotateAroundXAxis(rotation: Int) {
        if (rotation == 0) return
        
        // rotate current front texture - 180° (I don't understand why but that's right I guess)
        textures[Direction.NORTH]!!.rotation -= 2
        
        // shift sides
        shiftSides(Direction.xRotShiftSides, rotation)
        
        // set rotations for textures
        textures[Direction.WEST]!!.rotation += rotation
        textures[Direction.EAST]!!.rotation -= rotation // east texture has to rotate in the opposite direction
        
        // rotate new front texture + 180° (I don't understand why but that's right I guess)
        textures[Direction.NORTH]!!.rotation += 2
    }
    
    private fun rotateAroundYAxis(rotation: Int) {
        if (rotation == 0) return
        
        // shift sides
        shiftSides(Direction.yRotShiftSides, rotation)
        
        // set rotation for up and down textures
        textures[Direction.UP]!!.rotation += rotation
        textures[Direction.DOWN]!!.rotation -= rotation // down texture has to rotate in the opposite direction
    }
    
    private fun shiftSides(directions: Array<Direction>, amount: Int) {
        // shifting sides
        val sides: ArrayList<Direction> = directions.toCollection(ArrayList())
        val sideTextures = sides.map { textures[it] }.toCollection(ArrayList())
        sideTextures.shift(amount)
        
        // put new values in textures map
        for ((index, side) in sides.withIndex()) {
            textures[side] = sideTextures[index]!!
        }
    }
    
    class SideTexture(val texture: String) {
        
        var rotation: Int = 0
            set(value) {
                field = value
                field %= 4
                if (field < 0) field += 4
            }
        
    }
    
    enum class Direction(val xRot: Int, val yRot: Int, val blockFace: BlockFace, val axis: Axis?) {
        
        NORTH(0, 0, BlockFace.NORTH, Axis.Z),
        EAST(0, 1, BlockFace.EAST, Axis.X),
        SOUTH(0, 2, BlockFace.SOUTH, null),
        WEST(0, 3, BlockFace.WEST, null),
        UP(1, 0, BlockFace.UP, Axis.Y),
        DOWN(3, 0, BlockFace.DOWN, null);
        
        val modelDataName = name.toLowerCase()
        
        companion object {
            
            fun fromBlockFace(blockFace: BlockFace): Direction = values().first { it.blockFace == blockFace }
            fun fromAxis(axis: Axis): Direction = values().first { it.axis == axis }
            val yRotShiftSides = arrayOf(NORTH, EAST, SOUTH, WEST)
            val xRotShiftSides = arrayOf(NORTH, UP, SOUTH, DOWN)
            
        }
        
    }
}
