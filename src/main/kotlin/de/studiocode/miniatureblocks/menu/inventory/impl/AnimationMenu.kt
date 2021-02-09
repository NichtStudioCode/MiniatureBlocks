package de.studiocode.miniatureblocks.menu.inventory.impl

import de.studiocode.invui.item.itembuilder.ItemBuilder
import de.studiocode.miniatureblocks.menu.inventory.PagedMenuInventory
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.menu.item.impl.BackgroundItem
import de.studiocode.miniatureblocks.miniature.data.impl.AnimatedMiniatureData
import de.studiocode.miniatureblocks.miniature.item.impl.AnimatedMiniatureItem
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData
import de.studiocode.miniatureblocks.utils.*
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class AnimationMenu(data: AnimatedMiniatureData? = null) :
    PagedMenuInventory("Create Animation", infinitePages = true) {
    
    private val tickDelayItem = TickDelayItem()
    private val frameMap = HashMap<Int, AnimationFrameItem>()
    
    init {
        fill(45 until inventory.size, BackgroundItem)
        
        if (data != null && data.isValid()) {
            tickDelayItem.tickDelay = data.tickDelay
            for ((index, model) in data.models!!.withIndex()) {
                frameMap[index] = AnimationFrameItem(index, model)
            }
        }
        
        setItem(49, tickDelayItem)
        setItem(53, CreateAnimationItem())
    }
    
    override fun getItems(fromIndex: Int, toIndex: Int): List<MenuItem> {
        val items = ArrayList<MenuItem>()
        for (index in fromIndex..toIndex) {
            AnimationFrameItem(index, null)
            if (!frameMap.containsKey(index)) frameMap[index] = AnimationFrameItem(index, null)
            val animationFrameItem = frameMap[index]!!
            items.add(animationFrameItem)
        }
        return items
    }
    
    override fun getContentSize(): Int {
        return (currentPage + 1) * scrollableSlots.size
    }
    
    fun createAnimation() {
        val player = viewer!!
        if (frameMap.isNotEmpty()) {
            val models = (0..(frameMap.keys.max() ?: 0)).mapNotNull { frameMap[it]?.model }.toTypedArray()
            val animationData = AnimatedMiniatureData(tickDelayItem.tickDelay, models)
            val animationItem = AnimatedMiniatureItem.create(animationData)
            
            player.inventory.addItem(animationItem.itemStack)
            player.closeInventory()
            player.sendPrefixedMessage("§7Animated item added to inventory.")
        } else {
            player.sendPrefixedMessage("§cCan't create animation because it is empty.")
        }
    }
    
    inner class TickDelayItem : MenuItem() {
        
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
    
    inner class AnimationFrameItem(private val frame: Int, var model: MainModelData.CustomModel?) : MenuItem() {
        
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
            
            player.openInventory(this@AnimationMenu)
            player.playBurpSound()
        }
        
    }
    
    inner class CreateAnimationItem : MenuItem() {
        
        private val itemStack = ItemBuilder(Material.ANVIL, displayName = "§7Create Animation").build()
        
        override fun getItemStack() = itemStack
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent): Boolean {
            if (clickType == ClickType.LEFT) {
                createAnimation()
                player.playClickSound()
            }
            return false
        }
        
    }
    
}