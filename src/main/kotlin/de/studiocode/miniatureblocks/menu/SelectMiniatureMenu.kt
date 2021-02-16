package de.studiocode.miniatureblocks.menu

import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.GUIType
import de.studiocode.invui.gui.impl.SimpleGUI
import de.studiocode.invui.gui.impl.SimplePagedItemsGUI
import de.studiocode.invui.item.Item
import de.studiocode.invui.item.ItemBuilder
import de.studiocode.invui.item.impl.BaseItem
import de.studiocode.invui.item.impl.SimpleItem
import de.studiocode.invui.resourcepack.Icon
import de.studiocode.invui.window.impl.merged.split.AnvilSplitWindow
import de.studiocode.invui.window.impl.single.SimpleWindow
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData.CustomModel
import de.studiocode.miniatureblocks.utils.runTaskLater
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.scheduler.BukkitTask

open class SelectMiniatureMenu(private val player: Player, private val itemProvider: (CustomModel, Boolean) -> Item) {
    
    private val listGUI: SimplePagedItemsGUI = GUIBuilder(GUIType.PAGED_ITEMS, 9, 6)
        .setStructure("" +
            "1 - - - - - - - s" +
            "| x x x x x x x |" +
            "| x x x x x x x |" +
            "| x x x x x x x |" +
            "| x x x x x x x |" +
            "3 r - < - > - - 4")
        .addIngredient('s', SearchItem(true))
        .addIngredient('r', RefreshItem())
        .setItems(getMiniatureItems(true))
        .build() as SimplePagedItemsGUI
    
    private val previewGUI: SimplePagedItemsGUI = GUIBuilder(GUIType.PAGED_ITEMS, 9, 4)
        .setStructure("" +
            "x x x x x x x x x" +
            "x x x x x x x x x" +
            "x x x x x x x x x" +
            "# r # < # > # # s")
        .addIngredient('s', SearchItem(false))
        .addIngredient('r', RefreshItem())
        .setItems(getMiniatureItems(false))
        .build() as SimplePagedItemsGUI
    
    private val searchGUI = SimpleGUI(3, 1)
        .apply {
            setItem(0, Icon.ANVIL_OVERLAY_PLUS.item)
            setItem(1, Icon.ANVIL_OVERLAY_ARROW.item)
            setItem(2, ClearSearchItem())
        }
    
    private var searchMode = false
    private var lastFilter: String = ""
    
    fun openWindow() {
        val window = if (searchMode) AnvilSplitWindow(player, "Search...", searchGUI, previewGUI) { refreshItems(it) }
        else SimpleWindow(player, "Miniatures" + if (lastFilter != "") " §8(§7$lastFilter...§8)" else "", listGUI)
        
        window.show()
    }
    
    private fun switchSearchMode() {
        searchMode = !searchMode
        openWindow()
    }
    
    private fun refreshItems(filter: String = "") {
        listGUI.setItems(getMiniatureItems(true, filter))
        previewGUI.setItems(getMiniatureItems(false, filter))
    }
    
    private fun getMiniatureItems(obtainable: Boolean, filter: String = ""): List<Item> {
        this.lastFilter = filter
        return MiniatureBlocks.INSTANCE.resourcePack.getModels()
            .filter { it.name.toLowerCase().startsWith(filter.toLowerCase()) }
            .map { itemProvider.invoke(it, obtainable) }
    }
    
    private inner class SearchItem(state: Boolean) : SimpleItem(
        if (state) Icon.LENS.itemBuilder.setDisplayName("§7Search...")
        else Icon.ARROW_1_UP.itemBuilder.setDisplayName("§7Go back")) {
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            if (clickType == ClickType.LEFT) switchSearchMode()
        }
        
    }
    
    private inner class RefreshItem : BaseItem() {
        
        private val builder = Icon.REFRESH.itemBuilder.setDisplayName("§7Refresh miniatures")
        private val animatedBuilder = Icon.REFRESH_ANIMATED.itemBuilder.setDisplayName("§7Refresh miniatures")
        
        private var task: BukkitTask? = null
            set(value) {
                if (task != null && value != null) task!!.cancel()
                field = value
            }
        
        private var animated = false
            set(value) {
                field = value
                notifyWindows()
                if (value) task = runTaskLater(40) { animated = false }
            }
        
        override fun getItemBuilder(): ItemBuilder = if (animated) animatedBuilder else builder
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            if (clickType == ClickType.LEFT) {
                refreshItems(lastFilter)
                animated = true
            }
        }
        
    }
    
    private inner class ClearSearchItem : SimpleItem(Icon.X.itemBuilder.setDisplayName("§7Clear search")) {
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            if (clickType == ClickType.LEFT) refreshItems()
        }
        
    }
    
}