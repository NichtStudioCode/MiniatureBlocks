package de.studiocode.miniatureblocks.build

import de.studiocode.miniatureblocks.build.concurrent.SyncTaskExecutor
import de.studiocode.miniatureblocks.build.concurrent.ThreadSafeBlockData
import de.studiocode.miniatureblocks.build.concurrent.toThreadSafeBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.Direction.*
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.isGlass
import de.studiocode.miniatureblocks.util.isTranslucent
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.Location
import org.bukkit.Material
import java.util.concurrent.*

typealias ElementData = Triple<Element, Point3D, Material>

private val EMPTY_TEXTURE = Texture(Texture.UV(0.0, 0.0, 0.0, 0.0), "")

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
    private val data = ConcurrentHashMap<Point3D, Part>()
    
    fun createData(): BuildData {
        val points = ArrayList<Point3D>(200)
        
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    points.add(Point3D(x, y, z))
                    if (points.size == 200) {
                        val pointsCopy = ArrayList(points)
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
                            removeObstructedFaces(point)
                            
                            val x = point.x - minX
                            val y = point.y - minY
                            val z = point.z - minZ
                            this.data[Point3D(x, y, z)] = pair.second
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    private fun removeObstructedFaces(point: Point3D) {
        val info = processedWorldData[point]!!
        val material = info.first.material
        val part = info.second
        
        Direction.values().forEach { direction ->
            val neighborPoint = point.advance(direction)
            var neighborElements: List<ElementData> = emptyList()
            val neighborInfo = processedWorldData[neighborPoint]
            if (neighborInfo != null) {
                val neighborMaterial = neighborInfo.first.material
                neighborElements = neighborInfo.second.elements
                    .map {
                        ElementData(
                            it,
                            Point3D(direction.stepX, direction.stepY, direction.stepZ),
                            neighborMaterial
                        )
                    }
            }
            
            part.elements
                .filterNot(Element::hasRotation) // ignore elements with rotation
                .forEach { element ->
                    val surroundingElements = neighborElements +
                        part.elements
                            .filter { !it.hasRotation() && it != element } // ignore elements with rotation and the same element
                            .map { ElementData(it, Point3D(0, 0, 0), material) }
                    
                    if (isSideBlocked(element, material, direction, surroundingElements)) {
                        element.textures[direction] = EMPTY_TEXTURE
                    }
                }
        }
    }
    
    private fun isSideBlocked(element: Element, material: Material, direction: Direction, surroundingElements: List<ElementData>): Boolean {
        val from = element.fromPos
        val to = element.toPos
        
        surroundingElements.forEach { (neighbor, offset, neighborMaterial) ->
            val neighborFrom = neighbor.fromPos.mapToOffset(offset)
            val neighborTo = neighbor.toPos.mapToOffset(offset)
            
            if (isTouching(direction, from, to, neighborFrom, neighborTo)) {
                val from2D = from.to2D(direction.axis)
                val to2D = to.to2D(direction.axis)
                val neighborFrom2D = neighborFrom.to2D(direction.axis)
                val neighborTo2D = neighborTo.to2D(direction.axis)
                
                val xRange = neighborFrom2D.x.rangeTo(neighborTo2D.x)
                val yRange = neighborFrom2D.y.rangeTo(neighborTo2D.y)
                
                val blocked = from2D.x in xRange && to2D.x in xRange && from2D.y in yRange && to2D.y in yRange
                if (blocked) {
                    if (neighborMaterial.isTranslucent()) {
                        if (material.isGlass() && material != neighborMaterial) return false // if it is not the same glass type, show the textures
                        else if (!material.isGlass()) return false // if the neighbor is translucent (but not glass), show the textures
                    }
                    return true // side is blocked
                }
            }
        }
        
        return false
    }
    
    private fun Point3D.mapToOffset(offset: Point3D) = Point3D(x + offset.x, y + offset.y, z + offset.z)
    
    private fun isTouching(direction: Direction, from: Point3D, to: Point3D, neighborFrom: Point3D, neighborTo: Point3D) =
        when (direction) {
            NORTH -> from.z == neighborTo.z
            EAST -> to.x == neighborFrom.x
            SOUTH -> to.z == neighborFrom.z
            WEST -> from.x == neighborTo.x
            UP -> to.y == neighborFrom.y
            DOWN -> from.y == neighborTo.y
        }
    
}