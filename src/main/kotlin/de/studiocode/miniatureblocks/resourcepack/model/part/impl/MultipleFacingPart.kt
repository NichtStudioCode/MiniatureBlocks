package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncMultipleFacing
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

open class MultipleFacingPart(data: AsyncMultipleFacing) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    final override val elements = ArrayList<Element>()
    
    init {
        elements += SerializedPart.getModelElements(blockTexture.models!![0], blockTexture.textures)
        val directions = (Direction.cardinalPoints - data.faces.map(Direction::of))
        elements.removeIf { element -> directions.any { direction -> element.name.equals(direction.name, true) } }
    }
    
}