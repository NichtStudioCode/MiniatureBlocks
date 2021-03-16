package de.studiocode.miniatureblocks.resourcepack.model.element

import com.google.common.base.Preconditions
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.RotationValue
import org.bukkit.Axis

class Texture : Cloneable {
    
    private var uv: UV?
    var textureLocation: String
    var rotation by RotationValue()
    
    constructor(uv: UV, textureLocation: String, rotation: Int = 0) {
        Preconditions.checkArgument(rotation > -4 && rotation < 4, "Illegal rotation")
        this.uv = uv
        this.textureLocation = textureLocation
        this.rotation = rotation
    }
    
    constructor(textureLocation: String, rotation: Int = 0) {
        this.textureLocation = textureLocation
        this.rotation = rotation
        uv = null
    }
    
    fun freezeDynamicUV(element: Element, direction: Direction) {
        val uv = getDynamicUV(element, direction)
        this.uv = UV(uv[0], uv[1], uv[2], uv[3])
    }
    
    fun getUvInMiniature(element: Element, direction: Direction): DoubleArray {
        val uv: DoubleArray = if (this.uv == null) getDynamicUV(element, direction)
        else this.uv!!.doubleArray
        
        return normalizeUv(uv)
    }
    
    private fun getDynamicUV(element: Element, direction: Direction): DoubleArray {
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
        
        return doubleArrayOf(x[0], y[0], x[1], y[1]).map { it + 0.5 }.toDoubleArray()
    }
    
    private fun normalizeUv(uv: DoubleArray): DoubleArray {
        val normalizedUv = DoubleArray(4)
        normalizedUv[0] = uv[0] * 16
        normalizedUv[1] = uv[1] * 16
        normalizedUv[2] = uv[2] * 16
        normalizedUv[3] = uv[3] * 16
        return normalizedUv
    }
    
    public override fun clone(): Texture {
        return (super.clone() as Texture).apply {
            uv = uv?.copy()
        }
    }
    
    data class UV(var fromX: Double, var fromY: Double, var toX: Double, var toY: Double) {
        
        val doubleArray: DoubleArray
            get() = doubleArrayOf(fromX, fromY, toX, toY)
        
        fun flip(x: Boolean) {
            if (x) {
                val temp = fromX
                fromX = toX
                toX = temp
            } else {
                val temp = fromY
                fromY = toY
                toY = temp
            }
        }
        
    }
    
}