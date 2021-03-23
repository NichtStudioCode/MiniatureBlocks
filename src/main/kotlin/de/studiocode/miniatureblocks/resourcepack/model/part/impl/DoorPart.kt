package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncDoor
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.block.data.type.Door.Hinge

private const val DOOR_WIDTH = 3.0 / 16.0
private const val HALF_DOOR_WIDTH = DOOR_WIDTH / 2

class DoorPart(data: AsyncDoor) : Part() {
    
    private val textureLocations = BlockTexture.of(data.material).textures
    private val textureLocation = textureLocations[if (data.top) 1 else 0]
    
    private val frontUV = Texture.UV(0.0, 0.0, 1.0, 1.0)
    private val backUV = frontUV.copy()
    private val rightUV = Texture.UV(0.0, 0.0, DOOR_WIDTH, 1.0)
    private val leftUV = rightUV.copy()
    private val topBotUV = Texture.UV(1 - DOOR_WIDTH, 0.0, 1.0, 1.0)
    
    override val elements = ArrayList<Element>()
    
    init {
        val element = Element(
            Point3D(0.0, 0.0, 1 - DOOR_WIDTH), Point3D(1.0, 1.0, 1.0),
            Texture(backUV, textureLocation),
            Texture(rightUV, textureLocation),
            Texture(frontUV, textureLocation),
            Texture(leftUV, textureLocation),
            Texture(topBotUV, if (data.top) textureLocations[0] else "", 3),
            Texture(topBotUV, if (data.top) "" else textureLocations[0], 1)
        )
        elements += element
        
        if (data.hinge == Hinge.RIGHT) {
            frontUV.flip(true)
        } else {
            backUV.flip(true)
        }
    
        if (data.open) {
            rightUV.flip(true)
            
            element.textures[Direction.UP]!!.rotation += 2
            element.textures[Direction.DOWN]!!.rotation +=2
            
            if (data.hinge == Hinge.RIGHT) {
                element.rotatePosAroundYAxis(1, Point3D(1 - HALF_DOOR_WIDTH, 0.0, 1 - HALF_DOOR_WIDTH))
                element.rotateTexturesAroundYAxis(1)
            } else {
                element.rotatePosAroundYAxis(3, Point3D(HALF_DOOR_WIDTH, 0.0, 1 - HALF_DOOR_WIDTH))
                element.rotateTexturesAroundYAxis(3)
            }
        } else {
            leftUV.flip(true)
        }
        
        val direction = Direction.of(data.facing)
        addRotation(direction)
        applyModifications()
    }
    
}