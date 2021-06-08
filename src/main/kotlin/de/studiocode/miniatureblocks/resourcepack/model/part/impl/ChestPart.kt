package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncChest
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

class ChestPart(val data: AsyncChest) : Part() {
    
    private val texture = BlockTexture.of(data.material)
    override val elements = ArrayList<Element>()
    
    init {
        elements += SerializedPart.getModelElements(texture.models!![data.type.ordinal], arrayOf(texture.textures[data.type.ordinal]))
        rotate(Direction.of(data.facing))
    }
    
}