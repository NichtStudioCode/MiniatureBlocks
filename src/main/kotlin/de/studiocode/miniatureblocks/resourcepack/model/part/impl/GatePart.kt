package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.GateBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.TexturedElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

private const val GATE_WIDTH = 2.0 / 16.0
private const val HALF_GATE_WIDTH = GATE_WIDTH / 2
private const val BOTTOM_SPACE = 5.0 / 16.0

private const val HORIZONTAL_BEAM_HEIGHT = 3.0 / 16.0
private const val HORIZONTAL_BEAM_LENGTH = 4.0 / 16.0

private val LEFT_ROT_ORIGIN = doubleArrayOf(HALF_GATE_WIDTH, 0.0, 0.5)
private val RIGHT_ROT_ORIGIN = doubleArrayOf(1 - HALF_GATE_WIDTH, 0.0, 0.5)

class GatePart(data: GateBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    override val elements = ArrayList<Element>()
    
    init {
        val leftBeams = createLeftBeams()
        val rightBeams = createLeftBeams().onEach { it.rotatePosAroundYAxis(2) }
        
        elements += createGatePosts() + leftBeams + rightBeams
        
        if (data.open) {
            leftBeams.forEach { it.rotatePosAroundYAxis(3, LEFT_ROT_ORIGIN) }
            rightBeams.forEach { it.rotatePosAroundYAxis(1, RIGHT_ROT_ORIGIN) }
        }
        
        addPosRotation(Direction.of(data.facing))
        applyModifications()
        
        elements.forEach(Element::freezeUV)
        
        if (data.inWall) {
            addMove(0.0, -3.0 / 16.0, 0.0)
            applyModifications()
        }
    }
    
    private fun createGatePosts(): List<Element> {
        return listOf(
            TexturedElement(
                Point3D(0.0, BOTTOM_SPACE, 0.5 - HALF_GATE_WIDTH),
                Point3D(GATE_WIDTH, 1.0, 0.5 + HALF_GATE_WIDTH),
                blockTexture
            ),
            
            TexturedElement(
                Point3D(1 - GATE_WIDTH, BOTTOM_SPACE, 0.5 - HALF_GATE_WIDTH),
                Point3D(1.0, 1.0, 0.5 + HALF_GATE_WIDTH),
                blockTexture
            )
        )
    }
    
    private fun createLeftBeams(): List<Element> {
        val horizontalBeams = ArrayList<Element>()
        repeat(2) {
            val yStart = BOTTOM_SPACE + if (it == 0) (1.0 / 16.0) else (7.0 / 16.0)
            val yEnd = yStart + HORIZONTAL_BEAM_HEIGHT
            horizontalBeams += TexturedElement(
                Point3D(GATE_WIDTH, yStart, 0.5 - HALF_GATE_WIDTH),
                Point3D(GATE_WIDTH + HORIZONTAL_BEAM_LENGTH, yEnd, 0.5 + HALF_GATE_WIDTH),
                blockTexture
            )
        }
        
        val verticalBeam = TexturedElement(
            Point3D(0.5 - GATE_WIDTH, BOTTOM_SPACE + (1.0 / 16.0), 0.5 - HALF_GATE_WIDTH),
            Point3D(0.5, 1.0 - (1.0 / 16.0), 0.5 + HALF_GATE_WIDTH),
            blockTexture
        )
        
        return horizontalBeams + verticalBeam
    }
    
}