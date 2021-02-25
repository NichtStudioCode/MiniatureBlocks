package de.studiocode.miniatureblocks.build

import de.studiocode.miniatureblocks.build.BuildData.BuildBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.createPart
import de.studiocode.miniatureblocks.util.isGlass
import de.studiocode.miniatureblocks.util.isSeeTrough
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class BuildDataCreator(min: Location, max: Location) {
    
    private val executorService = Executors.newFixedThreadPool(20)
    private val data = ConcurrentHashMap<BuildBlockData, Part>()
    
    private val world = min.world!!
    private val minX = min.blockX
    private val minY = min.blockY
    private val minZ = min.blockZ
    private val maxX = max.blockX
    private val maxY = max.blockY
    private val maxZ = max.blockZ
    
    fun createData(): BuildData {
        val points = ArrayList<Point3D>(100)
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    points.add(Point3D(x, y, z))
                    if (points.size == 100) {
                        processPoints(ArrayList(points))
                        points.clear()
                    }
                }
            }
        }
        processPoints(points)
        
        executorService.shutdown()
        executorService.awaitTermination(5, TimeUnit.MINUTES)
        
        return BuildData(data, maxX - minX + 1)
    }
    
    private fun processPoints(points: ArrayList<Point3D>) {
        if (points.isEmpty()) return
        
        executorService.submit {
            points.forEach {
                val block = it.getBlock(world)
                val material = block.type
                
                if (BlockTexture.has(material)) {
                    val blockedSides = ArrayList<Direction>()
                    if (!material.isSeeTrough() || material.isGlass()) { // only check for neighbors if it is a full opaque block or glass
                        for (direction in Direction.values()) {
                            val neighborPoint = it.advance(direction, 1)
                            if (isSideBlocked(material, neighborPoint)) blockedSides.add(direction)
                        }
                    }
                    
                    if (blockedSides.size != 6) {
                        val x = it.x - minX
                        val y = it.y - minY
                        val z = it.z - minZ
                        data[BuildBlockData(x, y, z, block, blockedSides)] = block.createPart()
                    }
                }
            }
        }
    }
    
    private fun isSideBlocked(material: Material, point: Point3D): Boolean {
        if (isInRange(point)) {
            val neighbor = point.getBlock(world)
            val neighborMaterial = neighbor.type
            if (BlockTexture.has(neighborMaterial)) {
                if (material.isGlass()) {
                    // don't render glass side if glass blocks of the same glass type are side by side
                    return material == neighborMaterial
                } else if (!neighborMaterial.isSeeTrough()) {
                    // mark side as blocked if it isn't visible 
                    return !neighborMaterial.isSeeTrough()
                }
            }
        }
        
        return false
    }
    
    private fun isInRange(point: Point3D): Boolean {
        return point.x in minX..maxX
            && point.y in minY..maxY
            && point.z in minZ..maxZ
    }
    
    class Point3D(val x: Int, val y: Int, val z: Int) {
        
        fun getBlock(world: World) = world.getBlockAt(x, y, z)
        
        fun advance(direction: Direction, step: Int): Point3D {
            return when (direction) {
                Direction.NORTH -> Point3D(x, y, z - step)
                Direction.EAST -> Point3D(x + step, y, z)
                Direction.SOUTH -> Point3D(x, y, z + step)
                Direction.WEST -> Point3D(x - step, y, z)
                Direction.UP -> Point3D(x, y + step, z)
                Direction.DOWN -> Point3D(x, y - step, z)
            }
        }
        
        override fun equals(other: Any?): Boolean {
            return if (other is Point3D) x == other.x && y == other.y && z == other.z else this === other
        }
        
        override fun hashCode(): Int {
            var result = 3
            result = 31 * result + x xor (x ushr 32)
            result = 31 * result + y xor (y ushr 32)
            result = 31 * result + z xor (z ushr 32)
            return result
        }
        
    }
    
}