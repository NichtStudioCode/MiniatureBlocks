package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.WallBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.TexturedElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

const val TALL_HEIGHT = 1.0
const val LOW_HEIGHT = 14.0 / 16.0
const val HALF_SIDE_WIDTH = 6.0 / 16.0 / 2.0

class WallPart(val data: WallBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    private val halfCenterSize = (if (data.up) 8.0 / 16.0 else 6.0 / 16.0) / 2
    private val centerHeight = if (data.up || data.faces.all { (_, tall) -> tall }) TALL_HEIGHT else LOW_HEIGHT
    private val sideLength = if (data.up) 4.0 / 16.0 else 5.0 / 16.0
    override val elements = ArrayList<Element>()
    
    init {
        elements += createCenterElement()
        data.faces.forEach { (blockFace, _) -> elements += createSideElement(Direction.of(blockFace)) }
    }
    
    private fun createCenterElement(): Element {
        return TexturedElement(
            Point3D(0.5 - halfCenterSize, 0.0, 0.5 - halfCenterSize),
            Point3D(0.5 + halfCenterSize, centerHeight, 0.5 + halfCenterSize),
            blockTexture
        )
    }
    
    private fun createSideElement(direction: Direction): Element {
        val element = TexturedElement(
            Point3D(0.5 - HALF_SIDE_WIDTH, 0.0, 0.0),
            Point3D(0.5 + HALF_SIDE_WIDTH, getHeight(direction), sideLength),
            blockTexture
        )
        
        element.rotatePosAroundYAxis(direction.yRot)
        return element
    }
    
    private fun getHeight(direction: Direction) = if (data.faces[direction.blockFace]!!) TALL_HEIGHT else LOW_HEIGHT
    
}