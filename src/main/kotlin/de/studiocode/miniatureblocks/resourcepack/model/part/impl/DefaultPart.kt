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

open class DefaultPart(private val data: AsyncData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    private val textures = blockTexture.textures
    final override var elements = ArrayList<Element>()
    
    init {
        // create elements
        val isCube = blockTexture.models == null
        if (!isCube) {
            val model = if (data is AsyncMultiModel && blockTexture.models!!.size > data.model) blockTexture.models[data.model] else blockTexture.models!![0]
            elements += SerializedPart.getModelElements(model)
            SerializedPart.applyTextures(elements, getTextures(SerializedPart.countTexturesNeeded(elements)))
        } else {
            elements += createCubeElement(getTextures(6))
        }
        
        // apply correct rotation
        val defaultRotation = blockTexture.defaultRotation
        rotate(-defaultRotation.xRot, -defaultRotation.yRot, isCube)
        
        if (data is AsyncMultiCopyable) {
            val newElements = ArrayList<Element>()
            data.faces.map(Direction::of).forEach { direction ->
                val elementsCopy = ArrayList<Element>()
                elements.forEach { elementsCopy.add(it.clone()) }
                
                elementsCopy.forEach {
                    it.rotatePosAroundXAxis(direction.xRot)
                    it.rotatePosAroundYAxis(direction.yRot)
                    it.rotateTexturesAroundXAxis(direction.xRot)
                    it.rotateTexturesAroundYAxis(direction.yRot)
                }
                
                newElements.addAll(elementsCopy)
            }
            
            elements = newElements
        }
        
        when (data) {
            is AsyncDirectional -> rotate(Direction.of(data.facing), isCube)
            is AsyncOrientable -> rotate(Direction.of(data.axis), isCube)
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
            *Array(6) { Texture(CUBE_UV, textures[it]) }
        )
    
    private fun getTextures(texturesNeeded: Int): Array<String> {
        return if (data is AsyncMultiTexture && textures.size > data.texture * texturesNeeded) {
            val startIndex = texturesNeeded * data.texture
            textures.copyOfRange(startIndex, startIndex + texturesNeeded)
        } else textures
    }
    
}