package de.studiocode.miniatureblocks.menu.inventory.impl

import de.studiocode.miniatureblocks.menu.inventory.CustomModelsPagedMenu
import de.studiocode.miniatureblocks.menu.inventory.impl.AnimationMenu.AnimationFrameItem
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData
import de.studiocode.miniatureblocks.utils.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class PickMiniatureMenu(val item: AnimationFrameItem) : CustomModelsPagedMenu("Pick a miniature") {
    
    init {
        setItem(53, RemoveFrameItem())
    }
    
    override fun convertToMenuItems(cutModels: List<MainModelData.CustomModel>): List<MenuItem> {
        return cutModels.map { PickMiniatureItem(it) }
    }

    inner class PickMiniatureItem(val customModel: MainModelData.CustomModel) : MenuItem() {

        private val menuItemStack = customModel.createItemBuilder().also {
            it.addLoreLine("§7Left-click to pick this miniature")
        }.build()

        override fun getItemStack(): ItemStack {
            return menuItemStack
        }

        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent): Boolean {
            if (clickType == ClickType.LEFT) {
                item.handleModelPick(player, customModel)
            }
            return false
        }
        
    }
    
    inner class RemoveFrameItem : MenuItem() {
        
        private val itemStack = ItemBuilder(Material.WHITE_STAINED_GLASS_PANE, displayName = "§7Remove frame").build()
        
        override fun getItemStack() = itemStack

        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent): Boolean {
            if (clickType == ClickType.LEFT) {
                item.handleModelPick(player, null)
            }
            return false
        }
        
    }

}