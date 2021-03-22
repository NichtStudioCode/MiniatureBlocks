package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncSnow
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.TexturedElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

class SnowPart(data: AsyncSnow) : Part() {
    
    override val elements = listOf(
        TexturedElement(
            Point3D(0.0, 0.0, 0.0),
            Point3D(1.0, data.layers.toDouble() / data.maximumLayers.toDouble(), 1.0),
            BlockTexture.of(data.material)
        )
    )
    
}