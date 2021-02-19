package de.studiocode.miniatureblocks.utils

import de.studiocode.miniatureblocks.resourcepack.model.parser.Direction
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity

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
