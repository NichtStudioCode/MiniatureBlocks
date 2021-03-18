package de.studiocode.miniatureblocks.util.point

import de.studiocode.miniatureblocks.resourcepack.model.Direction
import org.bukkit.Axis
import org.bukkit.Axis.*
import org.bukkit.World

fun List<Double>.toPoint3D() = Point3D(this[0], this[1], this[2])

data class Point3D(var x: Double, var y: Double, var z: Double) {
    
    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())
    
    fun getBlock(world: World) = world.getBlockAt(x.toInt(), y.toInt(), z.toInt())
    
    fun advance(direction: Direction, step: Int = 1) = advance(direction, step.toDouble())
    
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
    
}