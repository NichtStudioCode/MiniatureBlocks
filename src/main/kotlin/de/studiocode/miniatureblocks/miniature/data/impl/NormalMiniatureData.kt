package de.studiocode.miniatureblocks.miniature.data.impl

import de.studiocode.miniatureblocks.miniature.armorstand.impl.NormalMiniatureArmorStand
import de.studiocode.miniatureblocks.miniature.data.MiniatureData
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData.CustomModel

class NormalMiniatureData(val model: CustomModel?) : MiniatureData {
    
    constructor(armorStand: NormalMiniatureArmorStand) : this(armorStand.model)
    
    override fun isValid() = model != null
    
}