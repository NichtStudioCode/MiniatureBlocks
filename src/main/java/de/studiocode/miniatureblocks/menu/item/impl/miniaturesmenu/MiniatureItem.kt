package de.studiocode.miniatureblocks.menu.item.impl.miniaturesmenu

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.utils.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class MiniatureItem(private val name: String, customModelData: Int) : MenuItem() {
    
    private val itemStack = ItemBuilder(Material.JACK_O_LANTERN, name = "§f$name", customModelData = customModelData).also { 
        it.addLoreLine("§7Left-click to obtain miniature")
        it.addLoreLine("§7Right-click to delete miniature")
    }.build()
    
    override fun getItemStack(): ItemStack {
        return itemStack
    }

    override fun handleClick(clickType: ClickType, event: InventoryClickEvent): Boolean {
        if (clickType == ClickType.LEFT) {
            (event.whoClicked as Player).inventory.addItem(itemStack)
        } else if (clickType == ClickType.RIGHT) {
            MiniatureBlocks.INSTANCE.resourcePack.removeModel(name)
            //TODO: remove all armor stands with the deleted model
        }
        return false
    }

}