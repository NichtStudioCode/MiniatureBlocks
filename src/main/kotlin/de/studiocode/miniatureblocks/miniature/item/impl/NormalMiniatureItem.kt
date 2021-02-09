package de.studiocode.miniatureblocks.miniature.item.impl

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStandManager.MiniatureType.NORMAL
import de.studiocode.miniatureblocks.miniature.data.impl.NormalMiniatureData
import de.studiocode.miniatureblocks.miniature.data.types.NormalMiniatureDataType
import de.studiocode.miniatureblocks.miniature.item.MiniatureItem
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData.CustomModel
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

class NormalMiniatureItem(itemStack: ItemStack) : MiniatureItem(itemStack) {
    
    companion object {
        
        private val PLUGIN = MiniatureBlocks.INSTANCE
        private val DATA_KEY = NamespacedKey(PLUGIN, "normalMiniatureData")
        
        fun create(model: CustomModel) = create(NormalMiniatureData(model))
        
        fun create(data: NormalMiniatureData): NormalMiniatureItem {
            if (data.isValid()) {
                val itemStack = createItemStack(data.model!!.createItemBuilder(), NORMAL)
                { it.set(DATA_KEY, NormalMiniatureDataType, data) }
                return NormalMiniatureItem(itemStack)
            } else throw IllegalArgumentException("Invalid miniature data")
        }
        
    }
    
    val data: NormalMiniatureData? = dataContainer.get(DATA_KEY, NormalMiniatureDataType)
    
    override fun isValid() = data != null && data.isValid()
    
}