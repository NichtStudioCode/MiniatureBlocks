package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.FluidBlockData
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import kotlin.math.min

class FluidPart(data: FluidBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    private val textures = blockTexture.textures
    override val elements = ArrayList<Element>()
    
    init {
        val texture = Texture(textures[0])
        val topTexture = if (data.direction == null) Texture(textures[1])
        else Texture(Texture.UV(0.0, 0.0, 0.5, 0.5), textures[2], (2 + data.direction.yRot) % 4)
        elements += Element(
            Point3D(0.0, 0.0, 0.0),
            Point3D(1.0, min((7 - data.level) / 7.0, 14.0 / 16.0), 1.0),
            texture,
            texture,
            texture,
            texture,
            topTexture,
            texture,
        )
    }
    
}