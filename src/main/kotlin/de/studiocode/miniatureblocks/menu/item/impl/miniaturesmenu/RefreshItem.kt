package de.studiocode.miniatureblocks.menu.item.impl.miniaturesmenu

import de.studiocode.miniatureblocks.menu.inventory.impl.MiniaturesMenu
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.utils.ItemBuilder
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class RefreshItem(private val miniaturesMenu: MiniaturesMenu) : MenuItem() {

    private val itemStack = ItemBuilder(Material.BLUE_STAINED_GLASS_PANE, name = "§7Refresh miniatures").build()

    override fun getItemStack(): ItemStack {
        return itemStack
    }

    override fun handleClick(clickType: ClickType, event: InventoryClickEvent): Boolean {
        if (clickType == ClickType.LEFT) {
            miniaturesMenu.refreshModels()
        }
        return false
    }

}