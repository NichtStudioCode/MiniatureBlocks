package de.studiocode.miniatureblocks.menu

import de.studiocode.invui.gui.structure.Marker
import de.studiocode.invui.gui.structure.Structure
import de.studiocode.invui.item.impl.controlitem.PageItem
import de.studiocode.invui.resourcepack.Icon

object Menus {
    
    fun registerGlobalIngredients() {
        Structure.addGlobalIngredient('x', Marker.ITEM_LIST_SLOT)
        Structure.addGlobalIngredient('<', ::PageBackItem)
        Structure.addGlobalIngredient('>', ::PageForwardItem)
        Structure.addGlobalIngredient('#', Icon.BACKGROUND.item)
        Structure.addGlobalIngredient('-', Icon.LIGHT_HORIZONTAL_LINE.item)
        Structure.addGlobalIngredient('|', Icon.LIGHT_VERTICAL_LINE.item)
        Structure.addGlobalIngredient('1', Icon.LIGHT_CORNER_TOP_LEFT.item)
        Structure.addGlobalIngredient('2', Icon.LIGHT_CORNER_TOP_RIGHT.item)
        Structure.addGlobalIngredient('3', Icon.LIGHT_CORNER_BOTTOM_LEFT.item)
        Structure.addGlobalIngredient('4', Icon.LIGHT_CORNER_BOTTOM_RIGHT.item)
    }
    
    class PageBackItem : PageItem(false, {
        (if (it.hasPageBefore()) Icon.ARROW_1_LEFT else Icon.LIGHT_ARROW_1_LEFT).itemBuilder
            .setDisplayName("§7Go back")
            .addLoreLines(if (it.hasInfinitePages()) {
                if (it.currentPageIndex == 0) "§cYou can't go further back"
                else "§8Go to page ${it.currentPageIndex}"
            } else {
                if (it.hasPageBefore()) "§8Go to page ${it.currentPageIndex}/${it.pageAmount}"
                else "§8You can't go further back"
            })
    })
    
    class PageForwardItem : PageItem(true, {
        (if (it.hasNextPage()) Icon.ARROW_1_RIGHT else Icon.LIGHT_ARROW_1_RIGHT).itemBuilder
            .setDisplayName("§7Next page")
            .addLoreLines(if (it.hasInfinitePages()) {
                "§8Go to page ${it.currentPageIndex + 2}"
            } else {
                if (it.hasNextPage()) "§8Go to page ${it.currentPageIndex + 2}/${it.pageAmount}"
                else "§8There are no more pages"
            })
    })
    
}