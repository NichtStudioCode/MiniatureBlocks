package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncMultipleFacingBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.TexturedElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

private const val HALF_FENCE_POST_WIDTH = 4.0 / 16.0 / 2
private const val HALF_FENCE_BEAM_WIDTH = 2.0 / 16.0 / 2

class FencePart(data: AsyncMultipleFacingBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    
    override val elements = ArrayList<Element>()
    
    init {
        elements += createFencePostElement()
        
        data.faces
            .map { Direction.of(it) }
            .forEach { direction ->
                val beams = createFenceBeamElements()
                beams.forEach { it.rotatePosAroundYAxis(direction.yRot) }
                elements += beams
            }
    }
    
    private fun createFencePostElement(): Element {
        return TexturedElement(
            Point3D(0.5 - HALF_FENCE_POST_WIDTH, 0.0, 0.5 - HALF_FENCE_POST_WIDTH),
            Point3D(0.5 + HALF_FENCE_POST_WIDTH, 1.0, 0.5 + HALF_FENCE_POST_WIDTH),
            blockTexture
        )
    }
    
    private fun createFenceBeamElements(): List<Element> {
        val pointA = Point3D(0.5 - HALF_FENCE_BEAM_WIDTH, 0.0, 0.0)
        val pointB = Point3D(0.5 + HALF_FENCE_BEAM_WIDTH, 0.0, 0.5 - HALF_FENCE_POST_WIDTH)
        return listOf(
            TexturedElement(
                pointA.copy().apply { y = 6.0 / 16.0 },
                pointB.copy().apply { y = 9.0 / 16.0 },
                blockTexture
            ),
            TexturedElement(
                pointA.copy().apply { y = 12.0 / 16.0 },
                pointB.copy().apply { y = 15.0 / 16.0 },
                blockTexture
            )
        )
    }
    
}