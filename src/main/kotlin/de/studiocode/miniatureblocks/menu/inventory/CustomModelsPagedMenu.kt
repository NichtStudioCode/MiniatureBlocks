package de.studiocode.miniatureblocks.menu.inventory

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.menu.item.impl.BackgroundItem
import de.studiocode.miniatureblocks.menu.item.impl.RefreshItem
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData.CustomModel

abstract class CustomModelsPagedMenu(title: String) : PagedMenuInventory(title) {
    
    init {
        fill(45 until inventory.size, BackgroundItem)
        setItem(49, RefreshItem(this))
    }

    override fun refresh() {
        loadPageContent()
        updatePageButtons()
    }

    override fun getItems(fromIndex: Int, toIndex: Int): List<MenuItem> {
        val models = MiniatureBlocks.INSTANCE.resourcePack.getModels()
        val cutModels = models.subList(fromIndex, if (toIndex > models.size) models.size else toIndex)

        return convertToMenuItems(cutModels)
    }
    
    abstract fun convertToMenuItems(cutModels: List<CustomModel>): List<MenuItem>

    override fun getContentSize(): Int {
        return MiniatureBlocks.INSTANCE.resourcePack.getModels().size
    }
    
}