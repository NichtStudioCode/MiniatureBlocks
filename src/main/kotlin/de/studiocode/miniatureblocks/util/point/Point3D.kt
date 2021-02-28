package de.studiocode.miniatureblocks.util.point

import de.studiocode.miniatureblocks.resourcepack.model.Direction
import org.bukkit.Axis
import org.bukkit.Axis.*
import org.bukkit.World

class Point3D(var x: Double, var y: Double, var z: Double) {
    
    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())
    
    fun getBlock(world: World) = world.getBlockAt(x.toInt(), y.toInt(), z.toInt())
    
    fun advance(direction: Direction, step: Int) = advance(direction, step.toDouble())
    
    fun advance(direction: Direction, step: Double): Point3D {
        return when (direction) {
            Direction.NORTH -> Point3D(x, y, z - step)
            Direction.EAST -> Point3D(x + step, y, z)
            Direction.SOUTH -> Point3D(x, y, z + step)
            Direction.WEST -> Point3D(x - step, y, z)
            Direction.UP -> Point3D(x, y + step, z)
            Direction.DOWN -> Point3D(x, y - step, z)
        }
    }
    
    fun to2D(ignore: Axis) =
        when (ignore) {
            X -> Point2D(z, y)
            Y -> Point2D(z, x)
            Z -> Point2D(x, y)
        }
    
    fun toDoubleArray() = doubleArrayOf(x, y, z)
    
    override fun toString(): String {
        return "Point3D($x | $y | $z)"
    }
    
    override fun equals(other: Any?): Boolean {
        return if (other is Point3D) x == other.x && y == other.y && z == other.z else this === other
    }
    
    override fun hashCode(): Int {
        var result = 3
        result = (31 * result + java.lang.Double.doubleToLongBits(x) xor java.lang.Double.doubleToLongBits(x) ushr 32).toInt()
        result = (31 * result + java.lang.Double.doubleToLongBits(y) xor java.lang.Double.doubleToLongBits(y) ushr 32).toInt()
        result = (31 * result + java.lang.Double.doubleToLongBits(z) xor java.lang.Double.doubleToLongBits(z) ushr 32).toInt()
        return result
    }
    
}