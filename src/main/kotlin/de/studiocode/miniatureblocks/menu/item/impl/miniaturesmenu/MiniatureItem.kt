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

    private val itemBuilder = ItemBuilder(Material.JACK_O_LANTERN, name = "§f$name", customModelData = customModelData)
    private val receivableItem = itemBuilder.build()
    private val menuItemStack = itemBuilder.also {
        it.addLoreLine("§7Left-click to obtain miniature")
        it.addLoreLine("§7Right-click to delete miniature")
    }.build()

    override fun getItemStack(): ItemStack {
        return menuItemStack
    }

    override fun handleClick(clickType: ClickType, event: InventoryClickEvent): Boolean {
        if (clickType == ClickType.LEFT) {
            (event.whoClicked as Player).inventory.addItem(receivableItem)
        } else if (clickType == ClickType.RIGHT) {
            MiniatureBlocks.INSTANCE.resourcePack.removeModel(name)
            MiniatureBlocks.INSTANCE.miniatureManager.removeMiniatureArmorStands(receivableItem)
        }
        return false
    }

}