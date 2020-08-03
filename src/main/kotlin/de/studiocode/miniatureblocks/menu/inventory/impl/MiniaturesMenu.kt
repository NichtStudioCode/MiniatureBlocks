package de.studiocode.miniatureblocks.menu.inventory.impl

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.menu.inventory.PagedMenuInventory
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.menu.item.impl.BackgroundItem
import de.studiocode.miniatureblocks.menu.item.impl.miniaturesmenu.MiniatureItem
import de.studiocode.miniatureblocks.menu.item.impl.miniaturesmenu.RefreshItem

class MiniaturesMenu : PagedMenuInventory("Miniatures") {

    init {
        fill(45 until inventory.size, BackgroundItem())
        setItem(49, RefreshItem(this))
    }

    fun refreshModels() {
        loadPageContent()
        updatePageButtons()
    }

    override fun getItems(fromIndex: Int, toIndex: Int): List<MenuItem> {
        val models = MiniatureBlocks.INSTANCE.resourcePack.getModels()
        val cutModels = models.subList(fromIndex, if (toIndex > models.size) models.size else toIndex)

        val items = ArrayList<MenuItem>()
        cutModels.forEach { items.add(MiniatureItem(it)) }
        return items
    }

    override fun getContentSize(): Int {
        return MiniatureBlocks.INSTANCE.resourcePack.getModels().size
    }
}