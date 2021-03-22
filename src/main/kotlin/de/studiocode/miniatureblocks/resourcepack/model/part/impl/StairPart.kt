package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncStairs
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.SlabElement
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.TexturedElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.block.data.type.Stairs.Shape.*

class StairPart(data: AsyncStairs) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    override val elements = ArrayList<Element>()
    
    init {
        elements += SlabElement(blockTexture)
        
        val top = data.top
        val shape = data.shape
        val facing = data.facing
        
        val yRot =
            if (shape == STRAIGHT) {
                elements += createStraightStairElement()
                Direction.of(facing).yRot
            } else {
                val right =
                    if (shape == OUTER_RIGHT || shape == OUTER_LEFT) {
                        elements += createOuterStairElement()
                        shape == OUTER_RIGHT
                    } else {
                        elements += createInnerStairElements()
                        shape == INNER_RIGHT
                    }
                //                                    right and bottom -> 1 OR   left and top -> -1
                Direction.of(facing).yRot + if (right && !top) 1 else if (!right && top) -1 else 0
            }
        
        addPosRotation(0, yRot)
        if (top) addPosRotation(2, 2)
        
        applyModifications()
    }
    
    private fun createStraightStairElement(): Element {
        val fromPos = Point3D(0.0, 0.5, 0.0)
        val toPos = Point3D(1.0, 1.0, 0.5)
        
        return TexturedElement(fromPos, toPos, blockTexture)
    }
    
    private fun createOuterStairElement(): Element {
        val fromPos = Point3D(0.0, 0.5, 0.0)
        val toPos = Point3D(0.5, 1.0, 0.5)
        
        return TexturedElement(fromPos, toPos, blockTexture)
    }
    
    private fun createInnerStairElements() =
        listOf(createStraightStairElement(), createOuterStairElement().apply { rotatePosAroundYAxis(3) })
    
    
}