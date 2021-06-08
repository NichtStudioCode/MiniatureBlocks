package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncAgeable
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

class AgeablePart(data: AsyncAgeable) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    override val elements = SerializedPart.getModelElements(blockTexture.models!![0], arrayOf(blockTexture.textures[data.age]))
    
}