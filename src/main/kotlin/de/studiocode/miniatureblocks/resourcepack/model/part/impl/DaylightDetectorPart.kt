package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.DaylightDetectorBlockData
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

class DaylightDetectorPart(data: DaylightDetectorBlockData) : Part() {
    
    private val textures = BlockTexture.of(data.material).textures
    
    override val elements: List<Element> = listOf(
        Element(
            Point3D(0.0, 0.0, 0.0), Point3D(1.0, 6.0 / 16.0, 1.0),
            Texture(textures[0]),
            Texture(textures[1]),
            Texture(textures[2]),
            Texture(textures[3]),
            if (data.inverted) Texture(textures[6]) else Texture(textures[4]),
            Texture(textures[5]),
        )
    )
    
}