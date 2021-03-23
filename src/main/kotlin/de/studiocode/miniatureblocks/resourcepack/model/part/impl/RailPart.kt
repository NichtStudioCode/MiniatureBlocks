package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncRail
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.RotationData
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.Axis
import org.bukkit.block.data.Rail.Shape
import org.bukkit.block.data.Rail.Shape.*

private const val SPACE = 1.0 / 16.0

class RailPart(data: AsyncRail) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    override val elements = ArrayList<Element>()
    
    init {
        val texture = blockTexture.textures[if (shouldUseSecondTexture(data)) 1 else 0]
        
        val element = Element(
            Point3D(0.0, 0.0, 0.0),
            Point3D(1.0, 0.0, 1.0),
            *Array(6) { Texture(if (it == 4 || it == 5) texture else "") }
        ).also(elements::add)
        
        val rotation = getRequiredRotation(data.shape)
        val angle = rotation.second
        if (angle != 0.0) {
            addMove(0.0, 0.5, 0.0)
            element.rotationData = RotationData(angle.toFloat(), Axis.X, Point3D(0.5, 0.5 + SPACE, 0.5), true)
        }
        
        addMove(0.0, SPACE, 0.0)
        addRotation(0, rotation.first)
        applyModifications()
    }
    
    private fun getRequiredRotation(shape: Shape) =
        when (shape) {
            NORTH_SOUTH -> 0 to 0.0
            EAST_WEST -> 1 to 0.0
            ASCENDING_EAST -> 1 to 45.0
            ASCENDING_WEST -> 3 to 45.0
            ASCENDING_NORTH -> 0 to 45.0
            ASCENDING_SOUTH -> 2 to 45.0
            SOUTH_EAST -> 0 to 0.0
            SOUTH_WEST -> 1 to 0.0
            NORTH_WEST -> 2 to 0.0
            NORTH_EAST -> 3 to 0.0
        }
    
    private fun shouldUseSecondTexture(data: AsyncRail): Boolean {
        val shape = data.shape
        return data.powered 
            || shape == SOUTH_EAST
            || shape == SOUTH_WEST
            || shape == NORTH_WEST
            || shape == NORTH_EAST
    }


    
}