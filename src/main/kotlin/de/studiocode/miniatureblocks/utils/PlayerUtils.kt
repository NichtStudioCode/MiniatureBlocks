package de.studiocode.miniatureblocks.utils

import de.studiocode.miniatureblocks.menu.inventory.MenuInventory
import org.bukkit.entity.Player

fun Player.openInventory(menuInventory: MenuInventory) {
    menuInventory.viewer = this
}