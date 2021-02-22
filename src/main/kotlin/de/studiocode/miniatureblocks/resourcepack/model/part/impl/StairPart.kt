package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.SlabElement
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.TexturedElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bukkit.block.Block
import org.bukkit.block.data.Bisected.Half.TOP
import org.bukkit.block.data.type.Stairs
import org.bukkit.block.data.type.Stairs.Shape.*

class StairPart(block: Block) : Part() {
    
    private val blockTexture = BlockTexture.of(block.type)
    
    override val rotatable = true
    override val elements = ArrayList<Element>()
    
    init {
        elements += SlabElement(blockTexture)
        
        val stairs = block.blockData as Stairs
        val top = stairs.half == TOP
        val shape = stairs.shape
        
        val yRot =
            if (shape == STRAIGHT) {
                elements += createStraightStairElement()
                Direction.of(stairs.facing).yRot
            } else {
                val right =
                    if (shape == OUTER_RIGHT || shape == OUTER_LEFT) {
                        elements += createOuterStairElement()
                        stairs.shape == OUTER_RIGHT
                    } else {
                        elements += createInnerStairElements()
                        stairs.shape == INNER_RIGHT
                    }
                //                                    right and bottom -> 1 OR   left and top -> -1
                Direction.of(stairs.facing).yRot + if (right && !top) 1 else if (!right && top) -1 else 0
            }
        
        addPosRotation(0, yRot)
        if (top) addPosRotation(2, 2)
        
        applyRotation()
    }
    
    private fun createStraightStairElement(): Element {
        val fromPos = doubleArrayOf(0.0, 0.5, 0.0)
        val toPos = doubleArrayOf(1.0, 1.0, 0.5)
        
        return TexturedElement(fromPos, toPos, blockTexture)
    }
    
    private fun createOuterStairElement(): Element {
        val fromPos = doubleArrayOf(0.0, 0.5, 0.0)
        val toPos = doubleArrayOf(0.5, 1.0, 0.5)
        
        return TexturedElement(fromPos, toPos, blockTexture)
    }
    
    private fun createInnerStairElements() =
        listOf(createStraightStairElement(), createOuterStairElement().apply { rotatePosAroundYAxis(3) })
    
    
}