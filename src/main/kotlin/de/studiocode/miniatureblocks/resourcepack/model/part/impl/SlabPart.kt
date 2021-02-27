package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.SlabBlockData
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.SlabElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

class SlabPart(data: SlabBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    private val top = data.top
    
    override val elements = listOf(SlabElement(blockTexture))
    override val rotatable = true
    
    init {
        if (top) addPosRotation(2, 0)
        applyRotation()
    }
    
}