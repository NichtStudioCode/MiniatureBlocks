package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncSlab
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.SlabElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

class SlabPart(data: AsyncSlab) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    private val top = data.top
    
    override val elements = listOf(SlabElement(blockTexture))
    
    init {
        if (top) rotatePos(2, 0)
    }
    
}