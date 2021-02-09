package de.studiocode.miniatureblocks.miniature.armorstand.impl

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStand
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStandManager.MiniatureType.NORMAL
import de.studiocode.miniatureblocks.miniature.data.types.NormalMiniatureDataType
import de.studiocode.miniatureblocks.miniature.item.impl.NormalMiniatureItem
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData.CustomModel
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.ArmorStand

class NormalMiniatureArmorStand(armorStand: ArmorStand) : MiniatureArmorStand(armorStand) {
    
    companion object {
        
        private val PLUGIN = MiniatureBlocks.INSTANCE
        val DATA_KEY = NamespacedKey(PLUGIN, "normalMiniatureData")
        
        fun spawn(location: Location, item: NormalMiniatureItem): NormalMiniatureArmorStand {
            if (item.isValid()) {
                val armorStand = spawnArmorStandSilently(location, item.itemStack, NORMAL)
                { it.set(DATA_KEY, NormalMiniatureDataType, item.data!!) }
                
                return NormalMiniatureArmorStand(armorStand)
            } else throw IllegalArgumentException("Invalid miniature data")
        }
        
    }
    
    var model: CustomModel? = null
    
    init {
        val data = dataContainer.get(DATA_KEY, NormalMiniatureDataType)
        if (data != null) model = data.model
    }
    
    override fun isValid() = super.isValid() && model != null
    
    override fun containsModel(model: CustomModel) = this.model == model
    
    
}