package de.studiocode.miniatureblocks.menu.item.impl.pagedmenu

import de.studiocode.miniatureblocks.menu.inventory.PagedMenuInventory
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.utils.ItemBuilder
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class ForwardItem(private val pagedMenuInventory: PagedMenuInventory) : MenuItem() {

    override fun getItemStack(): ItemStack {
        return ItemBuilder(material = Material.GREEN_STAINED_GLASS_PANE, name = "§7Forward").also {
            if (pagedMenuInventory.hasNextPage()) {
                val nextPage = pagedMenuInventory.currentPage + 1
                val pages = pagedMenuInventory.getPageAmount()
                it.addLoreLine("§7Go to page §b$nextPage§7/§b$pages")
            } else it.addLoreLine("§7There are no more pages")
        }.build()
    }

    override fun handleClick(clickType: ClickType, event: InventoryClickEvent): Boolean {
        if (clickType == ClickType.LEFT) {
            pagedMenuInventory.goForward()
        }
        return false
    }


}