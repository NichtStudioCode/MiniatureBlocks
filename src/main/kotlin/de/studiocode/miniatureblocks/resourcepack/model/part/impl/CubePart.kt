package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.DirectionalBlockData
import de.studiocode.miniatureblocks.build.concurrent.OrientableBlockData
import de.studiocode.miniatureblocks.build.concurrent.SnowableBlockData
import de.studiocode.miniatureblocks.build.concurrent.AsyncBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

private val UV = Texture.UV(0.0, 0.0, 1.0, 1.0)
private val FROM = Point3D(0.0, 0.0, 0.0)
private val TO = Point3D(1.0, 1.0, 1.0)

class CubePart(data: AsyncBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    private val textures = blockTexture.textures
    private val snowy = if (data is SnowableBlockData) data.snowy else false
    
    override val elements = listOf(createCubeElement())
    
    init {
        addTextureRotation(blockTexture.defaultRotation)
        
        if (data is DirectionalBlockData) addTextureRotation(Direction.of(data.facing))
        else if (data is OrientableBlockData) addTextureRotation(Direction.of(data.axis))
        applyModifications()
    }
    
    private fun createCubeElement(): Element {
        val startIndex = if (snowy) 6 else 0
        return Element(
            FROM, TO,
            Texture(UV, textures[startIndex]),
            Texture(UV, textures[startIndex + 1]),
            Texture(UV, textures[startIndex + 2]),
            Texture(UV, textures[startIndex + 3]),
            Texture(UV, textures[startIndex + 4]),
            Texture(UV, textures[startIndex + 5])
        )
    }
    
}