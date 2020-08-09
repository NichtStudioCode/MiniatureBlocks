package de.studiocode.miniatureblocks.menu.item.impl.animationmenu

import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData.CustomModel
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class PickMiniatureItem(val customModel: CustomModel, private val animationFrameItem: AnimationFrameItem) : MenuItem() {

    private val menuItemStack = customModel.createItemBuilder().also {
        it.addLoreLine("§7Left-click to pick this miniature")
    }.build()

    override fun getItemStack(): ItemStack {
        return menuItemStack
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent): Boolean {
        if (clickType == ClickType.LEFT) {
            animationFrameItem.handleModelPick(player, customModel)
        }
        return false
    }

}