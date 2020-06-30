package de.studiocode.miniatureblocks.menu.item

import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

abstract class MenuItem {
    
    abstract fun getItemStack(): ItemStack
    
    abstract fun handleClick(clickType: ClickType, event: InventoryClickEvent): Boolean
    
}