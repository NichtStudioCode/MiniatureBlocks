package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncBrewingStand
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

class BrewingStandPart(data: AsyncBrewingStand) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    override val elements = ArrayList<Element>()
    
    init {
        elements += SerializedPart.getModelElements(blockTexture.models!![0], blockTexture.textures)
        data.bottles.forEach { index -> elements.removeIf { it.name == "!$index" } }
        (setOf(0, 1, 2) - data.bottles).forEach { index -> elements.removeIf { it.name == index.toString() } }
    }
    
}