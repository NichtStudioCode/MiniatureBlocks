package de.studiocode.miniatureblocks.menu.item.impl

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.miniature.item.impl.NormalMiniatureItem
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData.CustomModel
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class MiniatureItem(private val customModel: CustomModel) : MenuItem() {

    private val receivableItem = NormalMiniatureItem.create(customModel)
    private val menuItemStack = customModel.createItemBuilder().also {
        it.addLoreLine("§7Left-click to obtain miniature")
        it.addLoreLine("§7Right-click to delete miniature")
    }.build()

    override fun getItemStack(): ItemStack {
        return menuItemStack
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent): Boolean {
        if (clickType == ClickType.LEFT) {
            (event.whoClicked as Player).inventory.addItem(receivableItem.itemStack)
        } else if (clickType == ClickType.RIGHT) {
            val miniatureBlocks = MiniatureBlocks.INSTANCE
            val resourcePack = miniatureBlocks.resourcePack
            miniatureBlocks.miniatureManager.removeMiniatureArmorStands(customModel)
            resourcePack.removeModel(customModel.name)
        }
        return false
    }

}