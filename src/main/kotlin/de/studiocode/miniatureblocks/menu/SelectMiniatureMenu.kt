package de.studiocode.miniatureblocks.menu

import de.studiocode.invui.item.Item
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.resourcepack.file.ModelFile.CustomModel
import de.studiocode.miniatureblocks.util.searchFor
import org.bukkit.entity.Player

open class SelectMiniatureMenu(player: Player, private val itemProvider: (SelectMiniatureMenu, CustomModel, Boolean) -> Item) :
    SearchMenu(player, "Miniatures", true) {
    
    override fun getItems(preview: Boolean, filter: String): List<Item> {
        return MiniatureBlocks.INSTANCE.resourcePack.mainModelData.customModels
            .searchFor(filter) { it.name }
            .map { itemProvider.invoke(this, it, !preview) }
    }
    
}