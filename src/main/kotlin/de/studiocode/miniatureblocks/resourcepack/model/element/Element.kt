package de.studiocode.miniatureblocks.resourcepack.model.element

import com.google.common.base.Preconditions
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.Point2D
import de.studiocode.miniatureblocks.util.shift
import java.util.*
import kotlin.math.max
import kotlin.math.min

open class Element(fromPos: DoubleArray, toPos: DoubleArray, vararg textures: Texture) {
    
    // centered positions
    var fromPos = fromPos.map { it - 0.5 }.toDoubleArray()
    var toPos = toPos.map { it - 0.5 }.toDoubleArray()
    
    val textures: EnumMap<Direction, Texture> = EnumMap(Direction::class.java)
    
    init {
        Preconditions.checkArgument(fromPos.size == 3, "fromPos size has to be 3")
        Preconditions.checkArgument(toPos.size == 3, "toPos size has to be 3")
        Preconditions.checkArgument(textures.size == 6, "textures size has to be 6")
        
        this.textures[Direction.NORTH] = textures[0]
        this.textures[Direction.EAST] = textures[1]
        this.textures[Direction.SOUTH] = textures[2]
        this.textures[Direction.WEST] = textures[3]
        this.textures[Direction.UP] = textures[4]
        this.textures[Direction.DOWN] = textures[5]
    }
    
    fun rotatePosAroundYAxis(rotation: Int) {
        if (rotation < 1) return
        val start = Point2D(fromPos[2], fromPos[0])
        val end = Point2D(toPos[2], toPos[0])
        
        repeat(rotation) {
            start.rotateClockwise()
            end.rotateClockwise()
        }
        
        fromPos[2] = min(start.x, end.x)
        fromPos[0] = min(start.y, end.y)
        toPos[2] = max(start.x, end.x)
        toPos[0] = max(start.y, end.y)
    }
    
    fun rotatePosAroundXAxis(rotation: Int) {
        if (rotation < 1) return
        val start = Point2D(fromPos[2], fromPos[1])
        val end = Point2D(toPos[2], toPos[1])
        
        repeat(rotation) {
            start.rotateClockwise()
            end.rotateClockwise()
        }
        
        fromPos[2] = min(start.x, end.x)
        fromPos[1] = min(start.y, end.y)
        toPos[2] = max(start.x, end.x)
        toPos[1] = max(start.y, end.y)
    }
    
    open fun rotateTexturesAroundYAxis(rotation: Int) {
        if (rotation < 1) return
        
        // shift all sides that aren't on the Y axis
        shiftSides(rotation, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
        
        // rotate top and down texture accordingly
        textures[Direction.UP]!!.rotation += rotation
        textures[Direction.DOWN]!!.rotation -= rotation // down texture rotates in the opposite direction
    }
    
    open fun rotateTexturesAroundXAxis(rotation: Int) {
        if (rotation < 1) return
        
        // rotate current front texture -180° (I don't understand why but that's right I guess)
        textures[Direction.NORTH]!!.rotation -= 2
        
        // shift all sides that aren't on the X axis
        shiftSides(rotation, Direction.NORTH, Direction.UP, Direction.SOUTH, Direction.DOWN)
        
        // set rotations of X axis textures accordingly
        textures[Direction.WEST]!!.rotation += rotation
        textures[Direction.EAST]!!.rotation -= rotation // east texture rotates in the opposite direction
        
        // rotate new front texture +180° (I don't understand why but that's right I guess)
        textures[Direction.NORTH]!!.rotation += 2
    }
    
    private fun shiftSides(shiftAmount: Int, vararg directions: Direction) {
        // create textures copy and shift sides
        val sideTextures = directions.map { textures[it] }.toMutableList()
        sideTextures.shift(shiftAmount)
        
        // put new shifted values in textures map
        for ((index, side) in directions.withIndex()) {
            textures[side] = sideTextures[index]!!
        }
    }
    
    fun getFromPosInMiniature(x: Int, y: Int, z: Int, stepSize: Double) = getPosInMiniature(fromPos, x, y, z, stepSize)
    
    fun getToPosInMiniature(x: Int, y: Int, z: Int, stepSize: Double) = getPosInMiniature(toPos, x, y, z, stepSize)
    
    private fun getPosInMiniature(pos: DoubleArray, x: Int, y: Int, z: Int, stepSize: Double): DoubleArray {
        val posInMiniature = DoubleArray(3)
        posInMiniature[0] = (pos[0] + 0.5) * stepSize + x * stepSize
        posInMiniature[1] = (pos[1] + 0.5) * stepSize + y * stepSize
        posInMiniature[2] = (pos[2] + 0.5) * stepSize + z * stepSize
        return posInMiniature
    }
    
}