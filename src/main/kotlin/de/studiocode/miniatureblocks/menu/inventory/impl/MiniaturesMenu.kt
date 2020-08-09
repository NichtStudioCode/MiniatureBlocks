package de.studiocode.miniatureblocks.menu.inventory.impl

import de.studiocode.miniatureblocks.menu.inventory.CustomModelsPagedMenu
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.menu.item.impl.miniaturesmenu.MiniatureItem
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData

class MiniaturesMenu : CustomModelsPagedMenu("Miniatures") {

    override fun convertToMenuItems(cutModels: List<MainModelData.CustomModel>): List<MenuItem> {
        return cutModels.map { MiniatureItem(it) }
    }

}