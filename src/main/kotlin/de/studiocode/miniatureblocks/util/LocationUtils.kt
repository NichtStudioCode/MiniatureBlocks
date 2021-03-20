package de.studiocode.miniatureblocks.util

import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStand
import de.studiocode.miniatureblocks.miniature.armorstand.getMiniature
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

fun Location.getMiniatureLookingAt(maxDistance: Double, stepSize: Double = 0.25): MiniatureArmorStand? {
    val location = this.clone()
    val vector = location.direction.multiply(stepSize)
    
    val miniatures = HashMap<Chunk, List<ArmorStand>>()
    
    var distance = 0.0
    while (distance <= maxDistance) {
        location.add(vector)
        
        val chunk = location.chunk
        if (!miniatures.containsKey(chunk)) miniatures[chunk] = chunk.getMiniatures()
        
        if (location.block.type.isTraversable()) {
            for (miniature in miniatures[chunk]!!) {
                val start = miniature.location.clone().subtract(0.5, 0.0, 0.5)
                val end = miniature.location.clone().add(0.5, 1.0, 0.5)
                
                if (location.isBetween(start, end)) return miniature.getMiniature()
            }
        } else return null
        
        distance += stepSize
    }
    
    return null
}

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
