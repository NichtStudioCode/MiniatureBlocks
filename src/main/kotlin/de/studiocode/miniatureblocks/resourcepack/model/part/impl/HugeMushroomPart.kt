package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncHugeMushroom
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.intValue
import de.studiocode.miniatureblocks.util.point.Point3D

class HugeMushroomPart(data: AsyncHugeMushroom) : Part() {
    
    private val textures = BlockTexture.of(data.material).textures
    override val elements = ArrayList<Element>()
    
    init {
        val faces = data.faces.map(Direction::of)
        val textures = Direction.values().map { Texture(textures[faces.contains(it).intValue]) }.toTypedArray()
        elements += Element(Point3D(0, 0, 0), Point3D(1, 1, 1), *textures)
    }
    
}