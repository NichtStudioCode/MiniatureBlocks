package de.studiocode.miniatureblocks.menu.item.impl.pagedmenu

import de.studiocode.miniatureblocks.menu.inventory.PagedMenuInventory
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.utils.ItemBuilder
import de.studiocode.miniatureblocks.utils.playClickSound
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class BackItem(private val pagedMenuInventory: PagedMenuInventory) : MenuItem() {

    override fun getItemStack(): ItemStack {
        return ItemBuilder(material = Material.RED_STAINED_GLASS_PANE, displayName = "§7Back").also {
            if (pagedMenuInventory.hasPageBefore()) {
                val pageBefore = pagedMenuInventory.currentPage - 1
                val pages = pagedMenuInventory.getPageAmount()
                it.addLoreLine("§7Go to page §b${pageBefore + 1}" + if (pagedMenuInventory.infinitePages) "" else "§7/§b${pages + 1}")
            } else it.addLoreLine("§7You can't go further back")
        }.build()
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent): Boolean {
        if (clickType == ClickType.LEFT) {
            pagedMenuInventory.goBack()
            player.playClickSound()
        }
        return false
    }

}