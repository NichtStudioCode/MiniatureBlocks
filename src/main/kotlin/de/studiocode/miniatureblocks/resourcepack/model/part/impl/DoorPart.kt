package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.DoorBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.block.data.type.Door.Hinge

class DoorPart(data: DoorBlockData) : Part() {
    
    private val textureLocations = BlockTexture.of(data.material).textures
    private val textureLocation = textureLocations[if (data.top) 1 else 0]
    
    private val doorWidth = 0.1875
    private val halfDoorWidth = doorWidth / 2
    
    private val frontUV = Texture.UV(0.0, 0.0, 1.0, 1.0)
    private val backUV = frontUV.clone()
    private val rightUV = Texture.UV(0.0, 0.0, doorWidth, 1.0)
    private val leftUV = rightUV.clone()
    private val topBotUV = Texture.UV(1 - doorWidth, 0.0, 1.0, 1.0)
    
    override val elements = ArrayList<Element>()
    override val rotatable = true
    
    init {
        val element = Element(
            Point3D(0.0, 0.0, 1 - doorWidth), Point3D(1.0, 1.0, 1.0),
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
                element.rotatePosAroundYAxis(1, doubleArrayOf(1 - halfDoorWidth, 0.0, 1 - halfDoorWidth))
                element.rotateTexturesAroundYAxis(1)
            } else {
                element.rotatePosAroundYAxis(3, doubleArrayOf(halfDoorWidth, 0.0, 1 - halfDoorWidth))
                element.rotateTexturesAroundYAxis(3)
            }
        } else {
            leftUV.flip(true)
        }
        
        val direction = Direction.of(data.facing)
        addRotation(direction)
        applyRotation()
    }
    
}