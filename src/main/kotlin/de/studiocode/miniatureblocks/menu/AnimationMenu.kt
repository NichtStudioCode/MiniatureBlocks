package de.studiocode.miniatureblocks.menu

import de.studiocode.invui.gui.SlotElement
import de.studiocode.invui.gui.SlotElement.ItemSlotElement
import de.studiocode.invui.gui.impl.PagedGUI
import de.studiocode.invui.gui.structure.Structure
import de.studiocode.invui.item.ItemBuilder
import de.studiocode.invui.item.impl.BaseItem
import de.studiocode.invui.item.impl.SimpleItem
import de.studiocode.invui.resourcepack.Icon
import de.studiocode.invui.window.impl.single.SimpleWindow
import de.studiocode.miniatureblocks.miniature.data.impl.AnimatedMiniatureData
import de.studiocode.miniatureblocks.miniature.item.impl.AnimatedMiniatureItem
import de.studiocode.miniatureblocks.resourcepack.file.ModelFile.CustomModel
import de.studiocode.miniatureblocks.util.playBurpSound
import de.studiocode.miniatureblocks.util.playClickSound
import de.studiocode.miniatureblocks.util.sendPrefixedMessage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent

class AnimationMenu(val player: Player, data: AnimatedMiniatureData? = null) :
    PagedGUI(9, 6, true, STRUCTURE) {
    
    companion object {
        private val STRUCTURE = Structure("" +
            "x x x x x x x x x" +
            "x x x x x x x x x" +
            "x x x x x x x x x" +
            "x x x x x x x x x" +
            "x x x x x x x x x" +
            "# # # < . > # # .")
    }
    
    private val tickDelayItem = TickDelayItem()
    private val frameMap = HashMap<Int, AnimationFrameItem>()
    private var currentFrame = 0
    
    private val selectMenu = SelectMiniatureMenu(player) { _, model, _ ->
        SelectMiniatureItem(model) {
            val item = frameMap[currentFrame]!!
            item.model = model
            player.playBurpSound()
            openWindow()
        }
    }
    
    init {
        if (data != null && data.isValid()) {
            tickDelayItem.tickDelay = data.tickDelay
            for ((index, model) in data.models!!.withIndex()) {
                frameMap[index] = AnimationFrameItem(index, model)
            }
        }
        
        setItem(49, tickDelayItem)
        setItem(53, CreateAnimationItem())
        
        update()
    }
    
    fun openWindow() {
        SimpleWindow(player, "Create Animation", this).show()
    }
    
    override fun getPageElements(page: Int): MutableList<SlotElement> {
        val from = page * itemListSlots.size
        val to = from + itemListSlots.size
        
        val elements = ArrayList<SlotElement>()
        for (index in from until to) {
            if (!frameMap.containsKey(index)) frameMap[index] = AnimationFrameItem(index, null)
            val item = frameMap[index]!!
            elements.add(ItemSlotElement(item))
        }
        
        return elements
    }
    
    override fun getPageAmount() = -1
    
    fun createAnimation() {
        if (frameMap.isNotEmpty()) {
            val models = (0..(frameMap.keys.maxOrNull() ?: 0)).mapNotNull { frameMap[it]?.model }.toTypedArray()
            val animationData = AnimatedMiniatureData(tickDelayItem.tickDelay, models)
            val animationItem = AnimatedMiniatureItem.create(animationData)
            
            player.inventory.addItem(animationItem.itemStack)
            player.closeInventory()
            player.sendPrefixedMessage("§7Animated item added to inventory.")
        } else {
            player.sendPrefixedMessage("§cCan't create animation because it is empty.")
        }
    }
    
    private inner class CreateAnimationItem : SimpleItem(Icon.GREEN_CHECKMARK.itemBuilder
        .setDisplayName("§7Create Animation")) {
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            if (clickType == ClickType.LEFT) {
                createAnimation()
                player.playClickSound()
            }
        }
        
    }
    
    private inner class TickDelayItem : BaseItem() {
        
        var tickDelay: Int = 1
            set(value) {
                field = value
                if (field < 1) field = 1
            }
        
        override fun getItemProvider(): ItemBuilder {
            return Icon.HORIZONTAL_DOTS.itemBuilder
                .setDisplayName("§7Tick delay: §b$tickDelay")
                .addLoreLines("§7Left-click to increase", "§7Right-click to decrease")
        }
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            if (clickType == ClickType.LEFT || clickType == ClickType.RIGHT) {
                if (clickType == ClickType.LEFT) tickDelay++ else tickDelay--
                
                player.playBurpSound()
                notifyWindows()
            }
        }
        
    }
    
    private inner class AnimationFrameItem(private val frame: Int, var model: CustomModel?) : BaseItem() {
        
        override fun getItemProvider(): ItemBuilder {
            return (model?.createItemBuilder()
                ?.also { it.addLoreLines("§7Miniature: §b${model!!.name}") }
                ?: ItemBuilder(Material.WHITE_STAINED_GLASS_PANE))
                .setDisplayName("§7Frame §8#§b${frame + 1}")
        }
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            if (clickType == ClickType.LEFT) {
                selectMenu.openWindow()
                currentFrame = frame
            } else if (clickType == ClickType.RIGHT) {
                model = null
                player.playBurpSound()
                notifyWindows()
            }
        }
        
    }
    
    private class SelectMiniatureItem(model: CustomModel, private val selectHandler: () -> Unit) :
        SimpleItem(model.createItemBuilder().addLoreLines("§7Use this miniature as the frame")) {
        
        override fun handleClick(clickType: ClickType?, player: Player?, event: InventoryClickEvent?) {
            if (clickType == ClickType.LEFT) selectHandler.invoke()
        }
        
    }
    
}
