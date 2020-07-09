package de.studiocode.miniatureblocks.menu.item.impl

import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.utils.ItemBuilder
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class BackgroundItem : MenuItem() {

    private val itemStack: ItemStack = ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, name = "§f").build()

    override fun getItemStack(): ItemStack {
        return itemStack
    }

    override fun handleClick(clickType: ClickType, event: InventoryClickEvent): Boolean {
        return false
    }
}