package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.*
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.RotationData
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.Axis

private val CUBE_UV = Texture.UV(0.0, 0.0, 1.0, 1.0)
private val CUBE_FROM = Point3D(0.0, 0.0, 0.0)
private val CUBE_TO = Point3D(1.0, 1.0, 1.0)

open class DefaultPart(data: AsyncData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    private val textures = blockTexture.textures
    final override val elements = ArrayList<Element>()
    
    init {
        // create elements
        val cube =
            if (blockTexture.model != null) {
                elements += SerializedPart.getModelElements(blockTexture.model)
                
                val textures = if (data is AsyncTwoState) {
                    val texturesNeeded = SerializedPart.countTexturesNeeded(elements)
                    if (texturesNeeded < this.textures.size) cutTextures(!data.state)
                    else this.textures
                } else this.textures
                
                SerializedPart.applyTextures(elements, textures)
                false
            } else {
                elements += createCubeElement(
                    if (data is AsyncTwoState) cutTextures(!data.state)
                    else this.textures
                )
                true
            }
        
        // remove elements
        if (data is AsyncTwoState) {
            elements.removeIf { (data.state && it.name.equals("0")) || (!data.state && it.name.equals("1")) }
        }
    
        // apply correct rotation
        val defaultRotation = blockTexture.defaultRotation
        rotate(-defaultRotation.xRot, -defaultRotation.yRot, cube)
        when (data) {
            is AsyncDirectional -> rotate(Direction.of(data.facing), cube)
            is AsyncOrientable -> rotate(Direction.of(data.axis), cube)
            is AsyncRotatable -> {
                val rotation = Direction.ofRotation(data.rotation)
                rotate(rotation.first)
                val rotationData = RotationData(rotation.second.toFloat(), Axis.Y, Point3D(0.5, 0.0, 0.5), false)
                elements.forEach { it.rotationData = rotationData }
            }
        }
    }
    
    private fun rotate(direction: Direction, cube: Boolean) {
        if (cube) rotateTextures(direction)
        else rotate(direction)
    }
    
    private fun rotate(x: Int, y: Int, cube: Boolean) {
        if (cube) rotateTextures(x, y)
        else rotate(x, y)
    }
    
    private fun createCubeElement(textures: Array<String>) =
        Element(
            CUBE_FROM, CUBE_TO,
            Texture(CUBE_UV, textures[0]),
            Texture(CUBE_UV, textures[1]),
            Texture(CUBE_UV, textures[2]),
            Texture(CUBE_UV, textures[3]),
            Texture(CUBE_UV, textures[4]),
            Texture(CUBE_UV, textures[5])
        )
    
    private fun cutTextures(first: Boolean): Array<String> {
        val size = textures.size
        return if (first) {
            textures.copyOfRange(0, size / 2)
        } else textures.copyOfRange(size / 2, size)
    }
    
}