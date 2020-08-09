package de.studiocode.miniatureblocks.miniature.item.impl

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStandManager.MiniatureType.ANIMATED
import de.studiocode.miniatureblocks.miniature.data.impl.AnimatedMiniatureData
import de.studiocode.miniatureblocks.miniature.data.types.AnimatedMiniatureDataType
import de.studiocode.miniatureblocks.miniature.item.MiniatureItem
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

class AnimatedMiniatureItem(itemStack: ItemStack) : MiniatureItem(itemStack) {

    companion object {

        private val PLUGIN = MiniatureBlocks.INSTANCE
        private val DATA_KEY = NamespacedKey(PLUGIN, "animatedMiniatureData")
        
        fun create(data: AnimatedMiniatureData): AnimatedMiniatureItem {
            if (data.isValid()) {
                val itemBuilder = data.models!![0].createItemBuilder().also {
                    it.displayName = "§7[Animated] ${it.displayName}"
                }

                val itemStack = createItemStack(itemBuilder, ANIMATED)
                { it.set(DATA_KEY, AnimatedMiniatureDataType, data) }

                return AnimatedMiniatureItem(itemStack)
            } else throw IllegalArgumentException("Invalid miniature data")
        }

    }

    val data: AnimatedMiniatureData?

    init {
        data = dataContainer.get(DATA_KEY, AnimatedMiniatureDataType)
    }

    override fun isValid() = data != null && data.isValid()

}