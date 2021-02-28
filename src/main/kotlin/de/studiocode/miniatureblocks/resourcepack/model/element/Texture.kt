package de.studiocode.miniatureblocks.resourcepack.model.element

import com.google.common.base.Preconditions
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.RotationValue
import org.bukkit.Axis

class Texture {
    
    private val uv: DoubleArray?
    val textureLocation: String
    var rotation by RotationValue()
    
    constructor(uv: DoubleArray, textureLocation: String, rotation: Int = 0) {
        Preconditions.checkArgument(uv.size == 4, "uv size has to be 4")
        this.uv = uv
        this.textureLocation = textureLocation
        this.rotation = rotation
    }
    
    constructor(textureLocation: String, rotation: Int = 0) {
        this.textureLocation = textureLocation
        this.rotation = rotation
        uv = null
    }
    
    fun getUvInMiniature(element: Element, direction: Direction): DoubleArray {
        val uv: DoubleArray
        if (this.uv == null) {
            val fromPos = element.fromPos.toDoubleArray().map { it - 0.5 }
            val toPos = element.toPos.toDoubleArray().map { it - 0.5 }
            
            val axisHor: Int // horizontal axis
            val axisVert: Int // vertical axis
            
            when (direction.axis) {
                Axis.X -> { // west or east
                    axisHor = 2 // z
                    axisVert = 1 // y
                }
                Axis.Y -> { // up or down
                    axisHor = 0 // x
                    axisVert = 2 // z
                }
                else -> { // north or south
                    axisHor = 0 // x
                    axisVert = 1 // y
                }
            }
            
            var nx = -1
            var ny = -1
            if (direction == Direction.WEST || direction == Direction.SOUTH) {
                nx = 1
            } else if (direction == Direction.UP) {
                ny = 1
                nx = 1
            }
            
            val x = doubleArrayOf(fromPos[axisHor] * nx, toPos[axisHor] * nx).sorted()
            val y = doubleArrayOf(fromPos[axisVert] * ny, toPos[axisVert] * ny).sorted()
            
            uv = doubleArrayOf(x[0], y[0], x[1], y[1]).map { it + 0.5 }.toDoubleArray()
        } else uv = this.uv
        
        return normalizeUv(uv)
    }
    
    private fun normalizeUv(uv: DoubleArray): DoubleArray {
        val normalizedUv = DoubleArray(4)
        normalizedUv[0] = uv[0] * 16
        normalizedUv[1] = uv[1] * 16
        normalizedUv[2] = uv[2] * 16
        normalizedUv[3] = uv[3] * 16
        return normalizedUv
    }
    
}