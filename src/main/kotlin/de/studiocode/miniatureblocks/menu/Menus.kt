package de.studiocode.miniatureblocks.menu

import de.studiocode.invui.gui.impl.PagedGUI
import de.studiocode.invui.gui.structure.Markers
import de.studiocode.invui.gui.structure.Structure
import de.studiocode.invui.item.ItemBuilder
import de.studiocode.invui.item.impl.controlitem.PageItem
import de.studiocode.invui.resourcepack.Icon

object Menus {
    
    fun registerGlobalIngredients() {
        Structure.addGlobalIngredient('x', Markers.ITEM_LIST_SLOT)
        Structure.addGlobalIngredient('<', ::PageBackItem)
        Structure.addGlobalIngredient('>', ::PageForwardItem)
        Structure.addGlobalIngredient('#', Icon.BACKGROUND.item)
        Structure.addGlobalIngredient('-', Icon.LIGHT_HORIZONTAL_LINE.item)
        Structure.addGlobalIngredient('|', Icon.LIGHT_VERTICAL_LINE.item)
        Structure.addGlobalIngredient('1', Icon.LIGHT_CORNER_TOP_LEFT.item)
        Structure.addGlobalIngredient('2', Icon.LIGHT_CORNER_TOP_RIGHT.item)
        Structure.addGlobalIngredient('3', Icon.LIGHT_CORNER_BOTTOM_LEFT.item)
        Structure.addGlobalIngredient('4', Icon.LIGHT_CORNER_BOTTOM_RIGHT.item)
        Structure.addGlobalIngredient('5', Icon.LIGHT_VERTICAL_RIGHT.item)
        Structure.addGlobalIngredient('6', Icon.LIGHT_VERTICAL_LEFT.item)
        Structure.addGlobalIngredient('7', Icon.LIGHT_HORIZONTAL_UP.item)
        Structure.addGlobalIngredient('8', Icon.LIGHT_HORIZONTAL_DOWN.item)
    }
    
    class PageBackItem : PageItem(false) {
        
        override fun getItemProvider(gui: PagedGUI): ItemBuilder {
            val itemBuilder = (if (gui.hasPageBefore()) Icon.ARROW_1_LEFT else Icon.LIGHT_ARROW_1_LEFT).itemBuilder
            itemBuilder.setDisplayName("§7Go back")
            itemBuilder.addLoreLines(
                if (gui.hasInfinitePages()) {
                    if (gui.currentPageIndex == 0) "§cYou can't go further back"
                    else "§8Go to page ${gui.currentPageIndex}"
                } else {
                    if (gui.hasPageBefore()) "§8Go to page ${gui.currentPageIndex}/${gui.pageAmount}"
                    else "§8You can't go further back"
                }
            )
            return itemBuilder
        }
        
    }
    
    class PageForwardItem : PageItem(true) {
        
        override fun getItemProvider(gui: PagedGUI): ItemBuilder {
            val itemBuilder = (if (gui.hasNextPage()) Icon.ARROW_1_RIGHT else Icon.LIGHT_ARROW_1_RIGHT).itemBuilder
            itemBuilder.setDisplayName("§7Next page")
            itemBuilder.addLoreLines(
                if (gui.hasInfinitePages()) {
                    "§8Go to page ${gui.currentPageIndex + 2}"
                } else {
                    if (gui.hasNextPage()) "§8Go to page ${gui.currentPageIndex + 2}/${gui.pageAmount}"
                    else "§8There are no more pages"
                }
            )
            return itemBuilder
        }
        
    }
    
}