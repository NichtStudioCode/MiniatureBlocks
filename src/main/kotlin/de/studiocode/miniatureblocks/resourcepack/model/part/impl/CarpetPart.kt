package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.ThreadSafeBlockData
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.TexturedElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

private const val CARPET_HEIGHT = 1.0 / 16.0

class CarpetPart(data: ThreadSafeBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    
    override val elements = listOf(createCarpetElement())
    override val rotatable = false
    
    private fun createCarpetElement(): Element {
        return TexturedElement(
            Point3D(0.0, 0.0, 0.0), Point3D(1.0, CARPET_HEIGHT, 1.0),
            blockTexture
        )
    }
    
}