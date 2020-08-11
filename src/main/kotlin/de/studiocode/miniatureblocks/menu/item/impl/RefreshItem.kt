package de.studiocode.miniatureblocks.menu.item.impl

import de.studiocode.miniatureblocks.menu.inventory.MenuInventory
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.utils.ItemBuilder
import de.studiocode.miniatureblocks.utils.playBurpSound
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class RefreshItem(private val menu: MenuInventory) : MenuItem() {

    private val itemStack = ItemBuilder(Material.BLUE_STAINED_GLASS_PANE, displayName = "§7Refresh miniatures").build()

    override fun getItemStack(): ItemStack {
        return itemStack
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent): Boolean {
        if (clickType == ClickType.LEFT) {
            menu.refresh()
            player.playBurpSound()
        }
        return false
    }

}