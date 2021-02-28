package de.studiocode.miniatureblocks.util.point

import java.lang.Double.doubleToLongBits

class Point2D(var x: Double, var y: Double) {
    
    fun rotateClockwise() {
        if (x == 0.0 && y == 0.0) return
        
        if (x > 0 && y > 0) { // top right
            val temp = x
            x = y
            y = -temp
        } else if (x > 0 && y < 0) { // bottom right
            val temp = y
            y = -x
            x = temp
        } else if (x < 0 && y < 0) { // bottom left
            val temp = y
            y = -x
            x = temp
        } else if (x < 0 && y > 0) { // top left
            val temp = y
            y = -x
            x = temp
        } else if (y == 0.0) { // on x axis
            y = -x
            x = 0.0
        } else if (x == 0.0) { // on y axis
            x = y
            y = 0.0
        }
    }
    
    override fun toString() = "Point2D ($x | $y)"
    
    override fun equals(other: Any?): Boolean {
        return if (other is Point2D) x == other.x && y == other.y else this === other
    }
    
    override fun hashCode(): Int {
        var result = 3
        result = (31 * result + doubleToLongBits(x) xor doubleToLongBits(x) ushr 32).toInt()
        result = (31 * result + doubleToLongBits(y) xor doubleToLongBits(y) ushr 32).toInt()
        return result
    }
    
}