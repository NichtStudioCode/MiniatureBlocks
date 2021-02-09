package de.studiocode.miniatureblocks.menu.item

import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

abstract class MenuItem {
    
    abstract fun getItemStack(): ItemStack
    
    abstract fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent): Boolean
    
}