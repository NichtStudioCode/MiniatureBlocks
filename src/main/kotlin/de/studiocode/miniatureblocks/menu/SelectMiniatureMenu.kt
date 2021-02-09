package de.studiocode.miniatureblocks.menu

import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.GUIType
import de.studiocode.invui.gui.builder.Marker
import de.studiocode.invui.gui.impl.SimpleGUI
import de.studiocode.invui.gui.impl.SimplePagedItemsGUI
import de.studiocode.invui.item.Item
import de.studiocode.invui.item.impl.SimpleItem
import de.studiocode.invui.resourcepack.Icon
import de.studiocode.invui.window.impl.merged.split.AnvilSplitWindow
import de.studiocode.invui.window.impl.single.SimpleWindow
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData.CustomModel
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent

open class SelectMiniatureMenu(private val player: Player, private val itemProvider: (CustomModel, Boolean) -> Item) {
    
    private val listGUI: SimplePagedItemsGUI = GUIBuilder(GUIType.PAGED_ITEMS, 9, 6)
        .setStructure("" +
            "1 - - - - - - - s" +
            "| x x x x x x x |" +
            "| x x x x x x x |" +
            "| x x x x x x x |" +
            "| x x x x x x x |" +
            "3 r - < - > - - 4")
        .setMarker('x', Marker.ITEM_LIST_SLOT)
        .setIngredient('1', Icon.LIGHT_CORNER_TOP_RIGHT.item)
        .setIngredient('3', Icon.LIGHT_CORNER_BOTTOM_LEFT.item)
        .setIngredient('4', Icon.LIGHT_CORNER_BOTTOM_RIGHT.item)
        .setIngredient('-', Icon.LIGHT_HORIZONTAL_LINE.item)
        .setIngredient('|', Icon.LIGHT_VERTICAL_LINE.item)
        .setIngredient('<', PageBackItem())
        .setIngredient('>', PageForwardItem())
        .setIngredient('s', SearchItem(true))
        .setIngredient('r', RefreshItem())
        .setItems(getMiniatureItems(true))
        .build() as SimplePagedItemsGUI
    
    private val previewGUI: SimplePagedItemsGUI = GUIBuilder(GUIType.PAGED_ITEMS, 9, 4)
        .setStructure("" +
            "x x x x x x x x x" +
            "x x x x x x x x x" +
            "x x x x x x x x x" +
            "# r # < # > # # s")
        .setMarker('x', Marker.ITEM_LIST_SLOT)
        .setIngredient('#', Icon.BACKGROUND.item)
        .setIngredient('<', PageBackItem())
        .setIngredient('>', PageForwardItem())
        .setIngredient('s', SearchItem(false))
        .setIngredient('r', RefreshItem())
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
    
    private inner class RefreshItem : SimpleItem(Icon.MaterialIcon.NORMAL.getItemBuilder(Material.TOTEM_OF_UNDYING)
        .setDisplayName("§7Refresh miniatures")) {
        
        override fun handleClick(clickType: ClickType?, player: Player?, event: InventoryClickEvent?) {
            if (clickType == ClickType.LEFT) refreshItems(lastFilter)
        }
        
    }
    
    private inner class ClearSearchItem : SimpleItem(Icon.X.itemBuilder.setDisplayName("§7Clear search")) {
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            if (clickType == ClickType.LEFT) refreshItems()
        }
        
    }
    
}