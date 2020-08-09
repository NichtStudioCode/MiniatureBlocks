package de.studiocode.miniatureblocks.menu.inventory.impl

import de.studiocode.miniatureblocks.menu.inventory.PagedMenuInventory
import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.menu.item.impl.BackgroundItem
import de.studiocode.miniatureblocks.menu.item.impl.animationmenu.AnimationFrameItem
import de.studiocode.miniatureblocks.menu.item.impl.animationmenu.CreateAnimationItem
import de.studiocode.miniatureblocks.menu.item.impl.animationmenu.TickDelayItem
import de.studiocode.miniatureblocks.miniature.data.impl.AnimatedMiniatureData
import de.studiocode.miniatureblocks.miniature.item.impl.AnimatedMiniatureItem
import de.studiocode.miniatureblocks.utils.sendPrefixedMessage

class AnimationMenu(data: AnimatedMiniatureData? = null) : PagedMenuInventory("Create Animation", infinitePages = true) {

    private val tickDelayItem: TickDelayItem
    private val frameMap = HashMap<Int, AnimationFrameItem>()

    init {
        fill(45 until inventory.size, BackgroundItem)

        tickDelayItem = TickDelayItem()

        if (data != null && data.isValid()) {
            tickDelayItem.tickDelay = data.tickDelay
            for ((index, model) in data.models!!.withIndex()) {
                frameMap[index] = AnimationFrameItem(this, index, model)
            }
        }

        setItem(49, tickDelayItem)
        setItem(53, CreateAnimationItem(this))

    }

    override fun getItems(fromIndex: Int, toIndex: Int): List<MenuItem> {
        val items = ArrayList<MenuItem>()
        for (index in fromIndex..toIndex) {
            AnimationFrameItem(this, index, null)
            if (!frameMap.containsKey(index)) frameMap[index] = AnimationFrameItem(this, index, null)
            val animationFrameItem = frameMap[index]!!
            items.add(animationFrameItem)
        }
        return items
    }

    override fun getContentSize(): Int {
        return (currentPage + 1) * scrollableSlots.size
    }

    fun createAnimation() {
        val models = (0..(frameMap.keys.max() ?: 0)).mapNotNull { frameMap[it]?.model }.toTypedArray()
        val animationData = AnimatedMiniatureData(tickDelayItem.tickDelay, models)
        val animationItem = AnimatedMiniatureItem.create(animationData)

        val player = viewer!!
        player.inventory.addItem(animationItem.itemStack)
        player.closeInventory()
        player.sendPrefixedMessage("§7Animated item added to inventory")
    }

}