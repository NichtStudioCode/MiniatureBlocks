package de.studiocode.miniatureblocks.menu.item.impl.animationmenu

import de.studiocode.miniatureblocks.menu.inventory.impl.AnimationMenu
import de.studiocode.miniatureblocks.menu.inventory.impl.PickMiniatureMenu
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData
import de.studiocode.miniatureblocks.utils.ItemBuilder
import de.studiocode.miniatureblocks.utils.openInventory
import de.studiocode.miniatureblocks.utils.playBurpSound
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class AnimationFrameItem(private val parent: AnimationMenu, private val frame: Int, var model: MainModelData.CustomModel?) : MenuItem() {

    override fun getItemStack(): ItemStack {
        val itemBuilder = model?.createItemBuilder()?.also { it.addLoreLine("§7Miniature: §b${model!!.name}") }
                ?: ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
        itemBuilder.displayName = "§7Frame §8#§b${frame + 1}"
        return itemBuilder.build()
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent): Boolean {
        if (clickType == ClickType.LEFT) {
            player.openInventory(PickMiniatureMenu(this))
        } else if (clickType == ClickType.RIGHT) {
            model = null
            player.playBurpSound()
            return true
        }
        return false
    }

    fun handleModelPick(player: Player, model: MainModelData.CustomModel?) {
        this.model = model

        player.openInventory(parent)
        player.playBurpSound()
    }

}