package de.studiocode.miniatureblocks.menu.inventory

import de.studiocode.miniatureblocks.MiniatureBlocks
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MenuInventoryManager private constructor() : Listener {

    companion object {
        val INSTANCE = MenuInventoryManager()
    }

    private val menuInventories = ArrayList<MenuInventory>()

    init {
        val plugin = MiniatureBlocks.INSTANCE
        if (plugin.isEnabled) Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    fun addMenuInventory(menuInventory: MenuInventory) {
        menuInventories.add(menuInventory)
    }

    fun removeMenuInventory(menuInventory: MenuInventory) {
        menuInventories.remove(menuInventory)
    }

    fun closeAllMenuInventories() {
        menuInventories.forEach(MenuInventory::close)
    }

    @EventHandler
    fun handleInventoryClick(event: InventoryClickEvent) {
        val inventory = event.clickedInventory
        if (inventory != null) {
            menuInventories.filter { inventory == it.inventory }
                    .forEach { it.handleInventoryClick(event) }
        }
    }

    @EventHandler
    fun handleInventoryClose(event: InventoryCloseEvent) {
        menuInventories.filter { event.inventory == it.inventory }
                .forEach(MenuInventory::handleInventoryClose)
    }

}