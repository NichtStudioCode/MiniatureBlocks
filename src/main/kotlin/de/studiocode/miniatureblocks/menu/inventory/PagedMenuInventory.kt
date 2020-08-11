package de.studiocode.miniatureblocks.menu.inventory

import de.studiocode.miniatureblocks.menu.item.MenuItem
import de.studiocode.miniatureblocks.utils.ItemBuilder
import de.studiocode.miniatureblocks.utils.playClickSound
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.stream.IntStream
import kotlin.math.roundToInt

abstract class PagedMenuInventory(title: String, lines: Int = 6, private val bBtn: Int = 48, private val fBtn: Int = 50,
                                  val scrollableSlots: IntArray = IntStream.range(0, 45).toArray(),
                                  val infinitePages: Boolean = false) : MenuInventory(title, lines) {

    var currentPage = 0

    init {
        setPageButtons()
    }

    override fun handleInvOpen() {
        loadPageContent()
    }

    private fun setPageButtons() {
        setItem(bBtn, BackItem())
        setItem(fBtn, ForwardItem())
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
        return infinitePages || getContentSize() > (currentPage + 1) * scrollableSlots.size
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

    inner class ForwardItem : MenuItem() {

        override fun getItemStack(): ItemStack {
            return ItemBuilder(material = Material.GREEN_STAINED_GLASS_PANE, displayName = "§7Forward").also {
                if (hasNextPage()) {
                    val nextPage = currentPage + 1
                    val pages = getPageAmount()
                    it.addLoreLine("§7Go to page §b${nextPage + 1}" + if (infinitePages) "" else "§7/§b${pages + 1}")
                } else it.addLoreLine("§7There are no more pages")
            }.build()
        }

        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent): Boolean {
            if (clickType == ClickType.LEFT) {
                goForward()
                player.playClickSound()
            }
            return false
        }

    }

    inner class BackItem : MenuItem() {

        override fun getItemStack(): ItemStack {
            return ItemBuilder(material = Material.RED_STAINED_GLASS_PANE, displayName = "§7Back").also {
                if (hasPageBefore()) {
                    val pageBefore = currentPage - 1
                    val pages = getPageAmount()
                    it.addLoreLine("§7Go to page §b${pageBefore + 1}" + if (infinitePages) "" else "§7/§b${pages + 1}")
                } else it.addLoreLine("§7You can't go further back")
            }.build()
        }

        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent): Boolean {
            if (clickType == ClickType.LEFT) {
                goBack()
                player.playClickSound()
            }
            return false
        }

    }

}

