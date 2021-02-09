package de.studiocode.miniatureblocks.miniature.item

import de.studiocode.invui.item.itembuilder.ItemBuilder
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.miniature.Miniature
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStandManager.MiniatureType
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType.BYTE

abstract class MiniatureItem(val itemStack: ItemStack) : Miniature(itemStack.itemMeta!!) {
    
    companion object {
        
        private val PLUGIN = MiniatureBlocks.INSTANCE
        private val TYPE_ID_KEY = NamespacedKey(PLUGIN, "miniatureTypeID")
        
        fun createItemStack(
            itemBuilder: ItemBuilder,
            miniatureType: MiniatureType,
            setData: (PersistentDataContainer) -> Unit
        ): ItemStack {
            val itemStack = itemBuilder.build()
            val itemMeta = itemStack.itemMeta!!
            val dataContainer = itemMeta.persistentDataContainer
            
            dataContainer.set(TYPE_ID_KEY, BYTE, miniatureType.id)
            setData.invoke(dataContainer)
            
            itemStack.itemMeta = itemMeta
            return itemStack
        }
        
    }
    
}