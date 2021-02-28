package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.TrapdoorBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

class TrapdoorPart(data: TrapdoorBlockData) : Part() {
    
    private val trapdoorHeight = 0.1875
    private val halfTrapdoorHeight = trapdoorHeight / 2
    
    private val blockTexture = BlockTexture.of(data.material)
    
    override val elements: List<Element>
    override val rotatable = true
    
    init {
        val element = createTrapdoorElement()
        elements = listOf(element)
        
        val facing = Direction.of(data.facing)
    
        if (data.open) {
            if (data.top) element.rotateTexturesAroundYAxis(2)
            // rotation is always the same because direction rotation hasn't been applied yet
            val origin = doubleArrayOf(0.0, halfTrapdoorHeight, 1.0 - halfTrapdoorHeight)
            element.rotatePosAroundXAxis(1, origin)
            element.rotateTexturesAroundXAxis(1)
        } else if (data.top) {
            element.rotatePosAroundXAxis(2)
        }
        
        addRotation(facing)
        applyRotation()
    }
    
    private fun createTrapdoorElement(): Element {
        val fromPos = Point3D(0.0, 0.0, 0.0)
        val toPos = Point3D(1.0, trapdoorHeight, 1.0)
        
        return Element(fromPos, toPos,
            Texture(doubleArrayOf(0.0, 0.0, 1.0, trapdoorHeight), blockTexture.getTexture(Direction.NORTH)),
            Texture(doubleArrayOf(0.0, 0.0, 1.0, trapdoorHeight), blockTexture.getTexture(Direction.EAST)),
            Texture(doubleArrayOf(0.0, 0.0, 1.0, trapdoorHeight), blockTexture.getTexture(Direction.SOUTH)),
            Texture(doubleArrayOf(0.0, 0.0, 1.0, trapdoorHeight), blockTexture.getTexture(Direction.WEST)),
            Texture(doubleArrayOf(0.0, 0.0, 1.0, 1.0), blockTexture.getTexture(Direction.UP), 2),
            Texture(doubleArrayOf(0.0, 0.0, 1.0, 1.0), blockTexture.getTexture(Direction.DOWN), 4)
        )
    }
    
}