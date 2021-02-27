package de.studiocode.miniatureblocks.build

import de.studiocode.miniatureblocks.build.BuildData.BuildBlockData
import de.studiocode.miniatureblocks.build.concurrent.SyncTaskExecutor
import de.studiocode.miniatureblocks.build.concurrent.ThreadSafeBlockData
import de.studiocode.miniatureblocks.build.concurrent.toThreadSafeBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.isGlass
import de.studiocode.miniatureblocks.util.isSeeTrough
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import java.util.concurrent.*

class BuildDataCreator(min: Location, max: Location) {
    
    private val world = min.world!!
    private val minX = min.blockX
    private val minY = min.blockY
    private val minZ = min.blockZ
    private val maxX = max.blockX
    private val maxY = max.blockY
    private val maxZ = max.blockZ
    
    private val partsExecutorService = Executors.newFixedThreadPool(20)
    private val dataExecutorService = Executors.newFixedThreadPool(20)
    private val syncExecutor = SyncTaskExecutor()
    
    private val batches = ConcurrentLinkedQueue<Set<Point3D>>()
    private val processedWorldData = ConcurrentHashMap<Point3D, Pair<ThreadSafeBlockData, Part>>()
    private val data = ConcurrentHashMap<BuildBlockData, Part>()
    
    fun createData(): BuildData {
        val points = ArrayList<Point3D>(200)
        
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    points.add(Point3D(x, y, z))
                    if (points.size == 200) {
                        val pointsCopy = ArrayList(points) // create a copy of the points
                        loadWorldData(pointsCopy, this::processWorldData)
                        points.clear()
                    }
                }
            }
        }
        loadWorldData(points, this::processWorldData)
        
        syncExecutor.awaitCompletion()
        partsExecutorService.shutdownAndWait()
        
        createBuildBlockData()
        dataExecutorService.shutdownAndWait()
        
        return BuildData(data, maxX - minX + 1)
    }
    
    private fun ExecutorService.shutdownAndWait(time: Long = 1, timeUnit: TimeUnit = TimeUnit.MINUTES) {
        shutdown()
        awaitTermination(time, timeUnit)
    }
    
    private fun loadWorldData(points: List<Point3D>, after: (HashMap<Point3D, ThreadSafeBlockData>) -> Unit) {
        syncExecutor.submit { // run sync as it accesses Bukkit
            val worldData = HashMap<Point3D, ThreadSafeBlockData>()
            points.forEach {
                val block = it.getBlock(world)
                val material = block.type
                if (BlockTexture.has(material)) {
                    val data = block.blockData.toThreadSafeBlockData(material)
                    worldData[it] = data // save data (block and material) of this point
                }
            }
            after(worldData) // continue processing async
        }
    }
    
    private fun processWorldData(worldInfo: HashMap<Point3D, ThreadSafeBlockData>) {
        partsExecutorService.submit {
            try {
                worldInfo.forEach { (point, data) -> processedWorldData[point] = data to Part.createPart(data) }
                batches.add(worldInfo.keys) // add points batch for later processing (createBuildData)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun createBuildBlockData() {
        batches.forEach { points ->
            dataExecutorService.submit {
                try {
                    points.forEach { point ->
                        val pair = processedWorldData[point]
                        if (pair != null) {
                            val data = pair.first
                            val part = pair.second
                            val material = data.material
                            
                            val blockedSides = ArrayList<Direction>()
                            if (!material.isSeeTrough() || material.isGlass()) { // only check for neighbors if it is a full opaque block or glass
                                blockedSides.addAll(Direction.values()
                                    .filter { isSideBlocked(material, point.advance(it, 1)) })
                            }
                            
                            if (blockedSides.size != 6) {
                                val x = point.x - minX
                                val y = point.y - minY
                                val z = point.z - minZ
                                this.data[BuildBlockData(x, y, z, blockedSides)] = part
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    private fun isSideBlocked(material: Material, point: Point3D): Boolean {
        if (isInRange(point)) {
            val neighborMaterial = processedWorldData[point]?.first?.material
            if (neighborMaterial != null) {
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
        
        override fun toString(): String {
            return "Point3D($x | $y | $z)"
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