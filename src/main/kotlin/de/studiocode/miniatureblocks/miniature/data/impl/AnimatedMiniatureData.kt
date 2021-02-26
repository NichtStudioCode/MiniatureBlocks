package de.studiocode.miniatureblocks.miniature.data.impl

import de.studiocode.miniatureblocks.miniature.armorstand.impl.AnimatedMiniatureArmorStand
import de.studiocode.miniatureblocks.miniature.data.MiniatureData
import de.studiocode.miniatureblocks.resourcepack.model.ModelData

class AnimatedMiniatureData(val tickDelay: Int, val models: Array<out ModelData.CustomModel>?) : MiniatureData {
    
    constructor(armorStand: AnimatedMiniatureArmorStand) : this(armorStand.tickDelay, armorStand.models)
    
    override fun isValid() = tickDelay > 0 && models != null
    
}