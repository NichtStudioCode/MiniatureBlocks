package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.DirectionalBlockData
import de.studiocode.miniatureblocks.build.concurrent.MultipleFacingBlockData
import de.studiocode.miniatureblocks.build.concurrent.ThreadSafeBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

private const val SIZE = 1.0 / 16.0

class FlatPart(data: ThreadSafeBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    private val textures = blockTexture.textures
    
    override val elements = ArrayList<Element>()
    
    init {
        if (data is MultipleFacingBlockData) {
            data.faces
                .map { Direction.of(it) }
                .forEach { 
                    val element = createFlatElement(false)
                    element.rotatePosAroundYAxis(it.yRot)
                    element.rotateTexturesAroundYAxis(it.yRot)
                    elements += element
                }
        } else {
            elements += createFlatElement(true)
            if (data is DirectionalBlockData) addRotation(Direction.of(data.facing))
        }
        
        addRotation(blockTexture.defaultRotation)
        applyModifications()
    }
    
    private fun createFlatElement(inverted: Boolean) =
        Element(
            Point3D(0.0, 0.0, if (inverted) 1.0 - SIZE else SIZE), Point3D(1.0, 1.0, if (inverted) 1.0 - SIZE else SIZE),
            *Array(6) { Texture(if (it == 0 || it == 2) textures[0] else "") }
        )
    
}