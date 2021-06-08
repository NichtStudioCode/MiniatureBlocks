package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncLevelled
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

class CauldronPart(data: AsyncLevelled) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    override val elements = ArrayList<Element>()
    
    init {
        elements += SerializedPart.getModelElements(blockTexture.models!![0], blockTexture.textures)
        (setOf(1, 2, 3) - data.level).forEach { level -> elements.removeIf { it.name?.toIntOrNull() == level } }
    }
    
}