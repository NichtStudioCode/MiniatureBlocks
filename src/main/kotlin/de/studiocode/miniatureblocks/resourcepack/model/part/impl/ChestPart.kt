package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncChest
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

private val models = arrayOf("model/chest", "model/chest_left", "model/chest_right")
 
class ChestPart(val data: AsyncChest) : Part() {
    
    private val textures = BlockTexture.of(data.material).textures
    override val elements = ArrayList<Element>()
    
    init {
        elements += SerializedPart.getModelElements(models[data.type.ordinal], arrayOf(textures[data.type.ordinal]))
        
        rotate(Direction.of(data.facing))
    }
    
}