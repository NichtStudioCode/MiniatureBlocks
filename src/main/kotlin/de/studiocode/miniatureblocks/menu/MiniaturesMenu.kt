package de.studiocode.miniatureblocks.menu

import de.studiocode.invui.item.impl.SimpleItem
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.miniature.item.impl.NormalMiniatureItem
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent

class MiniaturesMenu(player: Player) : SelectMiniatureMenu(player, { model, obtainable -> TakeMiniatureItem(model, obtainable) }) {
    
    private class TakeMiniatureItem(val customModel: MainModelData.CustomModel, private val obtainable: Boolean = false) : SimpleItem(
        customModel.createItemBuilder().apply {
            if (obtainable) addLoreLines("§7Left-click to obtain this miniature", "§cRight-click to delete this miniature")
        }) {
        
        private val item = NormalMiniatureItem.create(customModel)
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            if (obtainable) {
                if (clickType == ClickType.LEFT) {
                    player.inventory.addItem(item.itemStack)
                } else if (clickType == ClickType.RIGHT) {
                    val miniatureBlocks = MiniatureBlocks.INSTANCE
                    val resourcePack = miniatureBlocks.resourcePack
                    miniatureBlocks.miniatureManager.removeMiniatureArmorStands(customModel)
                    resourcePack.removeModel(customModel.name)
                }
            }
        }
        
    }
    
}