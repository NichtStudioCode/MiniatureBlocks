package de.studiocode.miniatureblocks.menu.inventory

import de.studiocode.miniatureblocks.menu.item.MenuItem
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

abstract class MenuInventory(title: String, lines: Int) {
    
    private val menuItems = HashMap<Int, MenuItem>()
    var inventory: Inventory = Bukkit.createInventory(null, 9 * lines, title)
    var viewer: Player? = null
    set(viewer) {
        field = viewer
        
        if (viewer != null) {
            inventory.viewers.forEach { it.closeInventory() }
            viewer.openInventory(inventory)
            MenuInventoryManager.INSTANCE.addMenuInventory(this)
            handleInvOpen()
        }
    }


    fun handleInvOpen() {
        //empty
    }
    
    fun setItem(slot: Int, menuItem: MenuItem) {
        menuItems[slot] = menuItem
        
        inventory.setItem(slot, menuItem.getItemStack())
    }
    
    fun removeItem(slot: Int) {
        menuItems.remove(slot)
        
        inventory.setItem(slot, null)
    }
    
    fun resetItems(vararg slots: Int) {
        for (slot in slots) resetItem(slot)
    }
    
    fun resetItem(slot: Int) {
        inventory.setItem(slot, menuItems[slot]?.getItemStack())
    }
    
    fun hasItem(slot: Int): Boolean {
        return menuItems.containsKey(slot)
    }
    
    fun fill(range: IntRange, menuItem: MenuItem) {
        for (slot in range) {
            if (!hasItem(slot)) setItem(slot, menuItem)
        }
    }
    
    fun fill(menuItem: MenuItem) {
        fill(0 until inventory.size, menuItem)
    }
    
    fun handleInventoryClick(event: InventoryClickEvent) {
        event.isCancelled = true
        
        val slot = event.slot
        if (menuItems.containsKey(slot)) {
            val menuItem = menuItems[slot]
            if (menuItem!!.handleClick(event.click, event)) resetItem(slot)
        }
    }
    
    fun handleInventoryClose() {
        viewer = null
        MenuInventoryManager.INSTANCE.removeMenuInventory(this)
    }
    
    fun close() {
        viewer?.closeInventory()
    }

}