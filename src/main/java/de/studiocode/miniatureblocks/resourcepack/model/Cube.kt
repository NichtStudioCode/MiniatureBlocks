package de.studiocode.miniatureblocks.resourcepack.model

import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.utils.shiftRight
import org.bukkit.block.BlockFace

//TODO: add support for blocks like pistons or logs
class Cube(private val blockTexture: BlockTexture) {

    private var direction: Direction = Direction.NORTH
    private var xRot: Int = 0 // "pitch" 1 / 90°
    private var yRot: Int = 0 // "yaw" 1 / 90°

    fun setFacing(direction: Direction) {
        this.direction = direction
        xRot = direction.xRot
        yRot = direction.yRot
    }

    private fun createDefaultTextures(): HashMap<Direction, SideTexture> {
        val defaultTextures = HashMap<Direction, SideTexture>()

        for (side in Direction.values()) {
            defaultTextures[side] = SideTexture(when (side) {

                Direction.NORTH -> blockTexture.textureFront
                Direction.EAST -> blockTexture.textureRight
                Direction.SOUTH -> blockTexture.textureBack
                Direction.WEST -> blockTexture.textureLeft
                Direction.UP -> blockTexture.textureTop
                Direction.DOWN -> blockTexture.textureBottom

            }, 0)
        }



        return defaultTextures
    }

    // should probably be cleaned up
    fun calculateTextures(): HashMap<Direction, SideTexture> {
        val defaultTextures = createDefaultTextures()
        val textures = HashMap<Direction, SideTexture>()

        if (yRot != 0) {
            // shift textures
            val yRotShiftTextures = ArrayList<SideTexture>()
            for (yRotShiftSide in Direction.yRotShiftSides) {
                yRotShiftTextures.add(defaultTextures[yRotShiftSide]!!)
            }
            yRotShiftTextures.shiftRight(yRot)

            for ((index, side) in Direction.yRotShiftSides.withIndex()) {
                textures[side] = yRotShiftTextures[index]
            }

            // rotation for non-shifted textures
            for (side in Direction.yRotRotateSides) {
                val sideTexture = defaultTextures[side]!!
                sideTexture.rotation = if (side == Direction.UP) yRot else 4 - yRot // down texture has always opposite rotation
                textures[side] = sideTexture
            }
        } else if (xRot != 0) {
            // shift textures
            val xRotShiftTextures = ArrayList<SideTexture>()
            for (xRotShiftSide in Direction.xRotShiftSides) {
                xRotShiftTextures.add(defaultTextures[xRotShiftSide]!!)
            }
            xRotShiftTextures.shiftRight(xRot)

            for ((index, side) in Direction.xRotShiftSides.withIndex()) {
                textures[side] = xRotShiftTextures[index]
            }

            // rotation for non-shifted textures
            for (side in Direction.xRotRotateSides) {
                val sideTexture = defaultTextures[side]!!
                sideTexture.rotation = if (side == Direction.WEST) xRot else 4 - xRot // east texture has opposite rotation
                textures[side] = sideTexture
            }

            // rotation for shifted textures
            textures[Direction.NORTH]!!.rotation = 2
            textures[direction]!!.rotation = 2
        }

        return textures.ifEmpty { defaultTextures }
    }


    class SideTexture(val texture: String, var rotation: Int)

    enum class Direction(val xRot: Int, val yRot: Int, val blockFace: BlockFace) {

        NORTH(0, 0, BlockFace.NORTH),
        EAST(0, 1, BlockFace.EAST),
        SOUTH(0, 2, BlockFace.SOUTH),
        WEST(0, 3, BlockFace.WEST),
        UP(1, 0, BlockFace.UP),
        DOWN(3, 0, BlockFace.DOWN);

        val modelDataName = name.toLowerCase()
        
        companion object {

            fun fromBlockFace(blockFace: BlockFace): Direction = values().first { it.blockFace == blockFace }
            val yRotShiftSides = arrayOf(NORTH, EAST, SOUTH, WEST)
            val yRotRotateSides = arrayOf(UP, DOWN)
            val xRotShiftSides = arrayOf(NORTH, UP, SOUTH, DOWN)
            val xRotRotateSides = arrayOf(WEST, EAST)
            
        }

    }
}
