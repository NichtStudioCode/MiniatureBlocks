package de.studiocode.miniatureblocks.util.point

import de.studiocode.miniatureblocks.resourcepack.model.Direction
import org.bukkit.Axis
import org.bukkit.Axis.*
import org.bukkit.World
import kotlin.math.max
import kotlin.math.min

fun List<Double>.toPoint3D() = Point3D(this[0], this[1], this[2])

data class Point3D(var x: Double, var y: Double, var z: Double) {
    
    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())
    
    fun getBlock(world: World) = world.getBlockAt(x.toInt(), y.toInt(), z.toInt())
    
    fun advance(direction: Direction, step: Int = 1) = advance(direction, step.toDouble())
    
    fun rotateAroundYAxis(rotation: Int, origin: Point3D) {
        subtract(origin)
        val point2D = to2D(Y)
        repeat(rotation) { point2D.rotateClockwise() }
        setTo(point2D.to3D(Y, y))
        add(origin)
    }
    
    fun rotateAroundXAxis(rotation: Int, origin: Point3D) {
        subtract(origin)
        val point2D = to2D(Z)
        repeat(rotation) { point2D.rotateClockwise() }
        setTo(point2D.to3D(Z, z))
        add(origin)
    }
    
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
    
    private fun subtract(point: Point3D) {
        x -= point.x
        y -= point.y
        z -= point.z
    }
    
    private fun add(point: Point3D) {
        x += point.x
        y += point.y
        z += point.z
    }
    
    private fun setTo(point: Point3D) {
        x = point.x
        y = point.y
        z = point.z
    }
    
    companion object {
        
        fun sort(first: Point3D, second: Point3D): Pair<Point3D, Point3D> {
            val minX = min(first.x, second.x)
            val minY = min(first.y, second.y)
            val minZ = min(first.z, second.z)
            val maxX = max(first.x, second.x)
            val maxY = max(first.y, second.y)
            val maxZ = max(first.z, second.z)
            
            return Point3D(minX, minY, minZ) to Point3D(maxX, maxY, maxZ)
        }
        
    }
    
}