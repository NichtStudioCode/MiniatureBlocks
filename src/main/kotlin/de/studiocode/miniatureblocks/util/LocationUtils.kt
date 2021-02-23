package de.studiocode.miniatureblocks.util

import de.studiocode.miniatureblocks.resourcepack.model.Direction
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import kotlin.math.max
import kotlin.math.min

fun Location.getEntityLookingAt(maxDistance: Double, vararg exclusions: Entity, stepSize: Double = 0.25): Entity? {
    val entities = (world?.entities ?: emptyList())
        .filter { !exclusions.contains(it) }
    
    val location = this.clone()
    val vector = location.direction.multiply(stepSize)
    
    var distance = 0.0
    while (distance <= maxDistance) {
        
        location.add(vector)
        
        if (location.block.type == Material.AIR) {
            val entity = location.filterInBoundingBox(entities).firstOrNull()
            if (entity != null) return entity
        } else return null
        
        distance += stepSize
    }
    
    return null
}

fun <T : Entity> Location.filterInBoundingBox(entities: List<T>): List<T> {
    return entities.filter { it.boundingBox.contains(x, y, z) }
}

fun Location.roundCoordinates(digits: Int) {
    x = MathUtils.roundToDecimalDigits(x, digits)
    y = MathUtils.roundToDecimalDigits(y, digits)
    z = MathUtils.roundToDecimalDigits(z, digits)
}

fun Location.advance(direction: Direction, step: Double = 1.0) {
    when (direction) {
        Direction.NORTH -> add(0.0, 0.0, -step)
        Direction.EAST -> add(step, 0.0, 0.0)
        Direction.SOUTH -> add(0.0, 0.0, step)
        Direction.WEST -> add(-step, 0.0, 0.0)
        Direction.UP -> add(0.0, step, 0.0)
        Direction.DOWN -> add(0.0, -step, 0.0)
    }
}

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
