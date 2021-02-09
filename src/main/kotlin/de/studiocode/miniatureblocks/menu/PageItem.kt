package de.studiocode.miniatureblocks.menu

import de.studiocode.invui.item.impl.controlitem.PageItem
import de.studiocode.invui.resourcepack.Icon

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