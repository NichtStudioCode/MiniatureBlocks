package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncBlockData
import de.studiocode.miniatureblocks.build.concurrent.DirectionalBlockData
import de.studiocode.miniatureblocks.build.concurrent.OrientableBlockData
import de.studiocode.miniatureblocks.build.concurrent.SnowableBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

private val CUBE_UV = Texture.UV(0.0, 0.0, 1.0, 1.0)
private val CUBE_FROM = Point3D(0.0, 0.0, 0.0)
private val CUBE_TO = Point3D(1.0, 1.0, 1.0)

class DefaultPart(data: AsyncBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    private val textures = blockTexture.textures
    private val snowy = if (data is SnowableBlockData) data.snowy else false
    
    override val elements = ArrayList<Element>()
    
    init {
        val cube =
            if (blockTexture.model != null) {
                loadCustomElements()
                false
            } else {
                elements += createCubeElement()
                true
            }
        
        addRotation(blockTexture.defaultRotation, cube)
        if (data is DirectionalBlockData) addRotation(Direction.of(data.facing), cube)
        else if (data is OrientableBlockData) addRotation(Direction.of(data.axis), cube)
        applyModifications()
    }
    
    private fun addRotation(direction: Direction, cube: Boolean) {
        if (cube) addTextureRotation(direction)
        else addRotation(direction)
    }
    
    private fun loadCustomElements() {
        elements += SerializedPart.getModelElements(blockTexture.model!!, blockTexture.textures)
    }
    
    private fun createCubeElement(): Element {
        val startIndex = if (snowy) 6 else 0
        return Element(
            CUBE_FROM, CUBE_TO,
            Texture(CUBE_UV, textures[startIndex]),
            Texture(CUBE_UV, textures[startIndex + 1]),
            Texture(CUBE_UV, textures[startIndex + 2]),
            Texture(CUBE_UV, textures[startIndex + 3]),
            Texture(CUBE_UV, textures[startIndex + 4]),
            Texture(CUBE_UV, textures[startIndex + 5])
        )
    }
    
}