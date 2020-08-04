package de.studiocode.miniatureblocks.utils

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.menu.inventory.MenuInventory
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

const val prefix = "§8[§bMiniatureBlocks§8]§r "

fun Player.openInventory(menuInventory: MenuInventory) {
    menuInventory.viewer = this
}

fun Player.getTargetEntity(maxDistance: Double, stepSize: Double = 0.25): Entity? {
    return eyeLocation.getEntityLookingAt(maxDistance, this, stepSize = stepSize)
}

fun Player.getTargetMiniature(): ArmorStand? {
    val entity = getTargetEntity(8.0)
    if (entity is ArmorStand && MiniatureBlocks.INSTANCE.miniatureManager.isArmorStandMiniature(entity))
        return entity
    return null
}

fun Player.sendPrefixedMessage(message: String) {
    sendMessage(prefix + message)
}

fun Player.kickPlayerPrefix(message: String) {
    kickPlayer(prefix + message)
}