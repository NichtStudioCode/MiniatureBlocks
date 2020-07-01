package de.studiocode.miniatureblocks.menu.inventory

import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.menu.item.impl.pagedmenu.BackItem
import de.studiocode.miniatureblocks.menu.item.impl.pagedmenu.ForwardItem
import java.util.stream.IntStream
import kotlin.math.roundToInt

abstract class PagedMenuInventory(title: String, lines: Int = 6, private val bBtn: Int = 48, private val fBtn: Int = 50,
                                  private val scrollableSlots: IntArray = IntStream.range(0, 45).toArray()) : MenuInventory(title, lines) {
    
    var currentPage = 0
    
    init {
        setPageButtons()
        loadPageContent()
    }
    
    private fun setPageButtons() {
        setItem(bBtn, BackItem(this))
        setItem(fBtn, ForwardItem(this))
    }
    
    fun updatePageButtons() {
        resetItems(bBtn, fBtn)
    }
    
    abstract fun getContentSize(): Int
    
    abstract fun getItems(fromIndex: Int, toIndex: Int): List<MenuItem>
    
    fun loadPageContent() {
        val fromIndex = currentPage * scrollableSlots.size
        val items = getItems(fromIndex, fromIndex + scrollableSlots.size)
        for ((i, slot) in scrollableSlots.withIndex()) {
            if (i < items.size) {
                setItem(slot, items[i])
            } else removeItem(slot)
        }
    }
    
    fun getPageAmount(): Int {
        return ((getContentSize().toDouble()) / (scrollableSlots.size.toDouble())).roundToInt()
    }
    
    fun hasNextPage(): Boolean {
        return getContentSize() > (currentPage + 1) * scrollableSlots.size
    }
    
    fun hasPageBefore(): Boolean {
        return currentPage > 0
    }
    
    fun goForward() {
        if (hasNextPage()) {
            currentPage++
            loadPageContent()
            updatePageButtons()
        }
    }
    
    fun goBack() {
        if (hasPageBefore()) {
            currentPage--
            loadPageContent()
            updatePageButtons()
        }
    }
    
}

