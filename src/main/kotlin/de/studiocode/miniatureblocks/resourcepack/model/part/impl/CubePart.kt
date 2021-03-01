package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.DirectionalBlockData
import de.studiocode.miniatureblocks.build.concurrent.OrientableBlockData
import de.studiocode.miniatureblocks.build.concurrent.ThreadSafeBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.Direction.*
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

class CubePart(data: ThreadSafeBlockData) : Part() {
    
    private val uv = Texture.UV(0.0, 0.0, 1.0, 1.0)
    private val blockTexture = BlockTexture.of(data.material)
    
    override val elements = listOf(CubeElement())
    override val rotatable = false
    
    init {
        addRotation(blockTexture.defaultRotation)
        
        if (data is DirectionalBlockData) addRotation(Direction.of(data.facing))
        else if (data is OrientableBlockData) addRotation(Direction.of(data.axis))
        applyRotation()
    }
    
    inner class CubeElement : Element(
        Point3D(0.0, 0.0, 0.0), Point3D(1.0, 1.0, 1.0),
        Texture(uv, blockTexture.getTexture(NORTH)),
        Texture(uv, blockTexture.getTexture(EAST)),
        Texture(uv, blockTexture.getTexture(SOUTH)),
        Texture(uv, blockTexture.getTexture(WEST)),
        Texture(uv, blockTexture.getTexture(UP)),
        Texture(uv, blockTexture.getTexture(DOWN)),
    )
    
}