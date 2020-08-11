package de.studiocode.miniatureblocks.utils

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
