package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncBlockData
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.TexturedElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.Material

private val ELEMENT_CREATORS = hashMapOf(
    Material.GRASS_PATH to ::createGrassPathElement,
    Material.CACTUS to ::createCactusElement
)

val MISC_MATERIALS = ArrayList(ELEMENT_CREATORS.keys)

private fun createElement(material: Material) =
    ELEMENT_CREATORS[material]!!(BlockTexture.of(material))

private fun createGrassPathElement(texture: BlockTexture): Element {
    return TexturedElement(
        Point3D(0.0, 0.0, 0.0), Point3D(1.0, 15.0 / 16.0, 1.0),
        texture
    )
}

private fun createCactusElement(texture: BlockTexture): Element {
    val space = 1.0 / 16.0
    return TexturedElement(
        Point3D(space, 0.0, space), Point3D(1 - space, 1.0, 1 - space),
        texture
    )
}

class MiscPart(data: AsyncBlockData) : Part() {
    
    override val elements = ArrayList<Element>()
    
    init {
        elements += createElement(data.material)
    }
    
}