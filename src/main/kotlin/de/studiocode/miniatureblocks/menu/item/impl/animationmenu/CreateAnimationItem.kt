package de.studiocode.miniatureblocks.menu.item.impl.animationmenu

import de.studiocode.miniatureblocks.menu.inventory.impl.AnimationMenu
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.utils.ItemBuilder
import de.studiocode.miniatureblocks.utils.playClickSound
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent

class CreateAnimationItem(private val parent: AnimationMenu) : MenuItem() {

    private val itemStack = ItemBuilder(Material.ANVIL, displayName = "§7Create Animation").build()
    
    override fun getItemStack() = itemStack

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent): Boolean {
        if (clickType == ClickType.LEFT) {
            parent.createAnimation()
            player.playClickSound()
        }
        return false
    }

}