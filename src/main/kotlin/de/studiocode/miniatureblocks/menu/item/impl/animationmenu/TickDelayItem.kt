package de.studiocode.miniatureblocks.menu.item.impl.animationmenu

import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.utils.ItemBuilder
import de.studiocode.miniatureblocks.utils.playBurpSound
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class TickDelayItem : MenuItem() {
    
    var tickDelay: Int = 1
    set(value) {
        field = value
        if (field < 1) field = 1
    }
    
    private val base = ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).also { 
        it.addLoreLine("§7Left-click to increase")
        it.addLoreLine("§7Right-click to decrease")
    }
    
    override fun getItemStack(): ItemStack {
        return base.also { it.displayName = "§7Tick delay: §b$tickDelay" }.build()
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent): Boolean {
        if (clickType == ClickType.LEFT || clickType == ClickType.RIGHT) {
            if (clickType == ClickType.LEFT) tickDelay++ else tickDelay--
            
            player.playBurpSound()
            return true
        }
        return false
    }
    
}