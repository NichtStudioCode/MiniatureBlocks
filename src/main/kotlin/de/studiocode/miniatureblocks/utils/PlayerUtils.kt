package de.studiocode.miniatureblocks.utils

import de.studiocode.miniatureblocks.menu.inventory.MenuInventory
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

fun Player.openInventory(menuInventory: MenuInventory) {
    menuInventory.viewer = this
}

fun Player.getTargetEntity(maxDistance: Double, stepSize: Double = 0.25): Entity? {
    return eyeLocation.getEntityLookingAt(maxDistance, this, stepSize = stepSize)
}
