package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.MultipleFacingBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.TexturedElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.Axis

private const val GLASS_PANE_WIDTH = 2.0 / 16.0
private const val HALF_GLASS_PANE_WIDTH = GLASS_PANE_WIDTH / 2

class GlassPanePart(data: MultipleFacingBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    override val elements = ArrayList<Element>()
    
    init {
        elements += createCenterElement()
        
        data.faces
            .map { Direction.of(it) }
            .forEach { direction ->
                val element = createSideElement()
                element.freezeUV(Direction.UP, Direction.DOWN)
                element.rotatePosAroundYAxis(direction.yRot)
                if (direction.axis == Axis.X) {
                    element.textures[Direction.DOWN]!!.rotation = 3
                    element.textures[Direction.UP]!!.rotation = 1
                }
                elements += element
            }
    }
    
    private fun createCenterElement(): Element {
        return TexturedElement(
            Point3D(0.5 - HALF_GLASS_PANE_WIDTH, 0.0, 0.5 - HALF_GLASS_PANE_WIDTH),
            Point3D(0.5 + HALF_GLASS_PANE_WIDTH, 1.0, 0.5 + HALF_GLASS_PANE_WIDTH),
            blockTexture
        )
    }
    
    private fun createSideElement(): Element {
        return TexturedElement(
            Point3D(0.5 - HALF_GLASS_PANE_WIDTH, 0.0, 0.0),
            Point3D(0.5 + HALF_GLASS_PANE_WIDTH, 1.0, 0.5 - HALF_GLASS_PANE_WIDTH),
            blockTexture
        )
    }
    
}