package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncSwitch
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.TexturedElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.block.data.FaceAttachable.AttachedFace

private const val HALF_BUTTON_LENGTH = 6.0 / 16.0 / 2
private const val HALF_BUTTON_WIDTH = 4.0 / 16.0 / 2
private const val BUTTON_HEIGHT = 2.0 / 16.0

// TODO: levers
class SwitchPart(data: AsyncSwitch) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    override val elements = ArrayList<Element>()
    
    init {
        val direction = Direction.of(data.facing)
        val element = createButtonElement()
        
        if (data.attachedFace == AttachedFace.WALL) {
            element.rotatePosAroundXAxis(3)
        } else if (data.attachedFace == AttachedFace.CEILING) {
            element.rotatePosAroundXAxis(2)
        }
        
        element.rotatePosAroundYAxis(direction.yRot)
        
        elements += element
    }
    
    private fun createButtonElement(): Element {
        return TexturedElement(
            Point3D(0.5 - HALF_BUTTON_LENGTH, 0.0, 0.5 - HALF_BUTTON_WIDTH),
            Point3D(0.5 + HALF_BUTTON_LENGTH, BUTTON_HEIGHT, 0.5 + HALF_BUTTON_WIDTH),
            blockTexture
        )
    }
    
}