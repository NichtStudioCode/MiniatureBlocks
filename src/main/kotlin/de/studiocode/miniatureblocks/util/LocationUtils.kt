package de.studiocode.miniatureblocks.util

import com.google.common.base.Preconditions
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStand
import de.studiocode.miniatureblocks.miniature.armorstand.getMiniature
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.util.BoundingBox
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect
import java.awt.Color
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

val Location.blockLocation: Location
    get() = Location(world, blockX.toDouble(), blockY.toDouble(), blockZ.toDouble())

fun Location.toPoint() = Point3D(x, y, z)

fun Location.getMiniatureLookingAt(maxDistance: Double, stepSize: Double = 0.25): MiniatureArmorStand? {
    val location = this.clone()
    val vector = location.direction.multiply(stepSize)
    
    var hitboxCache: Map<ArmorStand, Pair<Location, Location>>? = null
    var lastChunk: Chunk? = null
    
    var distance = 0.0
    while (distance <= maxDistance) {
        location.add(vector)
        
        val chunk = location.chunk
        if (chunk != lastChunk) {
            hitboxCache = chunk.getMiniatures().associateWith {
                it.location.clone().subtract(0.5, 0.0, 0.5) to it.location.clone().add(0.5, 1.0, 0.5)
            }
            lastChunk = chunk
        }
        
        val block = location.block
        if (block.type.isTraversable() || !block.boundingBox.contains(location)) {
            for ((armorStand, hitbox) in hitboxCache!!) {
                if (location.isBetween(hitbox.first, hitbox.second))
                    return armorStand.getMiniature()
            }
        } else return null
        
        distance += stepSize
    }
    
    return null
}

fun BoundingBox.contains(location: Location) =
    contains(location.x, location.y, location.z)

fun Chunk.getMiniatures(): List<ArmorStand> {
    return entities
        .filterIsInstance<ArmorStand>()
        .filter { it.getMiniature() != null }
}

fun Location.isBetween(start: Location, end: Location) =
    x in start.x.rangeTo(end.x)
        && y in start.y.rangeTo(end.y)
        && z in start.z.rangeTo(end.z)

fun Location.getBoxOutline(other: Location, correct: Boolean, stepSize: Double = 0.5): List<Location> {
    val locations = ArrayList<Location>()
    
    val minX = min(x, other.x)
    val minY = min(y, other.y)
    val minZ = min(z, other.z)
    val maxX = max(x, other.x) + if (correct) 1 else 0
    val maxY = max(y, other.y) + if (correct) 1 else 0
    val maxZ = max(z, other.z) + if (correct) 1 else 0
    
    // lines in x direction
    var x = minX
    while (x < maxX) {
        x += stepSize
        locations.add(Location(world, x, minY, minZ))
        locations.add(Location(world, x, maxY, minZ))
        locations.add(Location(world, x, minY, maxZ))
        locations.add(Location(world, x, maxY, maxZ))
    }
    
    // lines in z direction
    var z = minZ
    while (z < maxZ) {
        z += stepSize
        locations.add(Location(world, minX, minY, z))
        locations.add(Location(world, maxX, minY, z))
        locations.add(Location(world, minX, maxY, z))
        locations.add(Location(world, maxX, maxY, z))
    }
    
    // lines in y direction
    var y = minY
    while (y < maxY) {
        y += stepSize
        locations.add(Location(world, minX, y, minZ))
        locations.add(Location(world, maxX, y, minZ))
        locations.add(Location(world, minX, y, maxZ))
        locations.add(Location(world, maxX, y, maxZ))
    }
    
    return locations
}

fun Location.createColoredParticle(color: Color): Any = ParticleBuilder(ParticleEffect.REDSTONE, this).setColor(color).toPacket()

fun Location.advance(direction: Direction, stepSize: Double = 1.0) =
    add(direction.stepX * stepSize, direction.stepY * stepSize, direction.stepZ * stepSize)

operator fun Location.rangeTo(end: Location): List<Location> {
    Preconditions.checkArgument(world == end.world, "Locations are not in the same world")
    
    val locations = ArrayList<Location>()
    for (x in blockX..end.blockX) {
        for (y in blockY..end.blockY) {
            for (z in blockZ..end.blockZ) {
                locations += Location(world, x.toDouble(), y.toDouble(), z.toDouble())
            }
        }
    }
    
    return locations
}

object LocationUtils {
    
    fun getChunksBetween(min: Location, max: Location): List<Chunk> {
        Preconditions.checkArgument(min.world != null && min.world == max.world)
        
        val startX = floor(min.blockX / 16.0).toInt()
        val startZ = floor(min.blockZ / 16.0).toInt()
        val endX = floor(max.blockX / 16.0).toInt()
        val endZ = floor(max.blockZ / 16.0).toInt()
        
        val chunks = ArrayList<Chunk>()
        val world = min.world!!
        
        for (x in startX..endX) {
            for (z in startZ..endZ) {
                chunks.add(world.getChunkAt(x, z))
            }
        }
        
        return chunks
    }
    
}
