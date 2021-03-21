package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncBlockData
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.TexturedElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

// TODO: should be a model file

private const val PRESSURE_PLATE_HEIGHT = 1.0 / 16.0

class PressurePlatePart(data: AsyncBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    
    override val elements = listOf(createPressurePlateElement())
    
    private fun createPressurePlateElement(): Element {
        return TexturedElement(
            Point3D(PRESSURE_PLATE_HEIGHT, 0.0, PRESSURE_PLATE_HEIGHT),
            Point3D(1 - PRESSURE_PLATE_HEIGHT, PRESSURE_PLATE_HEIGHT, 1 - PRESSURE_PLATE_HEIGHT),
            blockTexture
        )
    }
    
}