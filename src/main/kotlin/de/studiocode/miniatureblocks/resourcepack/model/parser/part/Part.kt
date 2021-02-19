package de.studiocode.miniatureblocks.resourcepack.model.parser.part

import com.google.common.base.Preconditions
import de.studiocode.miniatureblocks.resourcepack.model.parser.Direction
import de.studiocode.miniatureblocks.resourcepack.model.parser.Direction.*
import de.studiocode.miniatureblocks.resourcepack.model.parser.RotationValue
import de.studiocode.miniatureblocks.utils.shift
import java.util.*
import kotlin.math.max
import kotlin.math.min

abstract class Part {
    
    abstract val elements: List<Element>
    abstract val rotatable: Boolean
    
    private var rotateX by RotationValue()
    private var rotateY by RotationValue()
    
    fun addRotation(direction: Direction) {
        rotateX += direction.xRot
        rotateY += direction.yRot
    }
    
    fun addRotation(x: Int, y: Int) {
        rotateX += x
        rotateY += y
    }
    
    fun applyRotation() {
        if (rotateX == 0 && rotateY == 0) return
        
        rotateAroundXAxis(rotateX)
        rotateAroundYAxis(rotateY)
        
        rotateX = 0
        rotateY = 0
    }
    
    private fun rotateAroundYAxis(rotation: Int) {
        if (rotation < 1) return
        elements.forEach { it.rotateTexturesAroundYAxis(rotation) }
        if (!rotatable) return
        for (element in elements) {
            val start = Point2D(element.fromPos[0], element.fromPos[2])
            val end = Point2D(element.toPos[0], element.toPos[2])
            
            repeat(rotation) {
                start.rotateClockwise()
                end.rotateClockwise()
            }
            
            element.fromPos[0] = min(start.x, end.x)
            element.fromPos[2] = min(start.y, end.y)
            element.toPos[0] = max(start.x, end.x)
            element.toPos[2] = max(start.y, end.y)
        }
    }
    
    private fun rotateAroundXAxis(rotation: Int) {
        if (rotation < 1) return
        elements.forEach { it.rotateTexturesAroundXAxis(rotation) }
        if (!rotatable) return
        for (element in elements) {
            val start = Point2D(element.fromPos[2], element.fromPos[1])
            val end = Point2D(element.toPos[2], element.toPos[1])
    
            repeat(rotation) {
                start.rotateClockwise()
                end.rotateClockwise()
            }
    
            element.fromPos[2] = min(start.x, end.x)
            element.fromPos[1] = min(start.y, end.y)
            element.toPos[2] = max(start.x, end.x)
            element.toPos[1] = max(start.y, end.y)
        }
    }
    
    class Point2D(var x: Double, var y: Double) {
    
        fun rotateClockwise() {
            if (x == 0.0 && y == 0.0) return
            
            if (x > 0 && y > 0) { // top right
                y = -y
            } else if (x > 0 && y < 0) { // bottom right
                x = -x
            } else if (x < 0 && y < 0) { // bottom left
                y = -y
            } else if (x < 0 && y > 0) { // top left
                x = -x
            } else if (y == 0.0 ) { // on x axis
                y = -x
                x = 0.0
            } else if (x == 0.0) { // on y axis
                x = y
                y = 0.0
            }
        }
    
        override fun toString() = "Point2D ($x | $y)"
    }
    
    open class Element(fromPos: DoubleArray, toPos: DoubleArray, vararg textures: Texture) {
        
        // center
        var fromPos = fromPos.map { it - 0.5 }.toDoubleArray()
        var toPos = toPos.map { it - 0.5 }.toDoubleArray()
        
        val textures: EnumMap<Direction, Texture> = EnumMap(Direction::class.java)
        
        init {
            Preconditions.checkArgument(fromPos.size == 3, "fromPos size has to be 3")
            Preconditions.checkArgument(toPos.size == 3, "toPos size has to be 3")
            Preconditions.checkArgument(textures.size == 6, "textures size has to be 6")
            
            this.textures[NORTH] = textures[0]
            this.textures[EAST] = textures[1]
            this.textures[SOUTH] = textures[2]
            this.textures[WEST] = textures[3]
            this.textures[UP] = textures[4]
            this.textures[DOWN] = textures[5]
        }
        
        open fun rotateTexturesAroundYAxis(rotation: Int) {
            if (rotation < 1) return
            
            // shift all sides that aren't on the Y axis
            shiftSides(rotation, NORTH, EAST, SOUTH, WEST)
            
            // rotate top and down texture accordingly
            textures[UP]!!.rotation += rotation
            textures[DOWN]!!.rotation -= rotation // down texture rotates in the opposite direction
        }
        
        open fun rotateTexturesAroundXAxis(rotation: Int) {
            if (rotation < 1) return
            
            // rotate current front texture -180° (I don't understand why but that's right I guess)
            textures[NORTH]!!.rotation -= 2
            
            // shift all sides that aren't on the X axis
            shiftSides(rotation, NORTH, UP, SOUTH, DOWN)
            
            // set rotations of X axis textures accordingly
            textures[WEST]!!.rotation += rotation
            textures[EAST]!!.rotation -= rotation // east texture rotates in the opposite direction
            
            // rotate new front texture +180° (I don't understand why but that's right I guess)
            textures[NORTH]!!.rotation += 2
        }
        
        fun getFromPosInMiniature(x: Int, y: Int, z: Int, stepSize: Int) = getPosInMiniature(fromPos, x, y, z, stepSize)
        
        fun getToPosInMiniature(x: Int, y: Int, z: Int, stepSize: Int) = getPosInMiniature(toPos, x, y, z, stepSize)
        
        private fun getPosInMiniature(pos: DoubleArray, x: Int, y: Int, z: Int, stepSize: Int): DoubleArray {
            val posInMiniature = DoubleArray(3)
            posInMiniature[0] = (pos[0] + 0.5) * stepSize + x
            posInMiniature[1] = (pos[1] + 0.5) * stepSize + y
            posInMiniature[2] = (pos[2] + 0.5) * stepSize + z
            return posInMiniature
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
        
    }
    
    class Texture(val uv: DoubleArray, val textureLocation: String, rotation: Int = 0) {
        
        var rotation by RotationValue()
        
        init {
            Preconditions.checkArgument(uv.size == 4, "uv size has to be 4")
        }
        
    }
    
}