package de.studiocode.miniatureblocks.resourcepack.model.parser.part

import de.studiocode.miniatureblocks.resourcepack.model.parser.Direction
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bukkit.block.Block
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Orientable

class CubePart(block: Block) : Part() {
    
    private val uv = doubleArrayOf(0.0, 0.0, 16.0, 16.0)
    private val blockTexture = BlockTexture.of(block.type)
    
    override val elements = listOf(CubeElement())
    override val rotatable = false
    
    init {
        addRotation(blockTexture.defaultRotation)
    
        val data = block.blockData
        if (data is Directional) addRotation(Direction.of(data.facing))
        else if (data is Orientable) addRotation(Direction.of(data.axis))
    }
    
    inner class CubeElement : Element(doubleArrayOf(0.0, 0.0, 0.0), doubleArrayOf(1.0, 1.0, 1.0),
        Texture(uv, blockTexture.textureFront),
        Texture(uv, blockTexture.textureRight),
        Texture(uv, blockTexture.textureBack),
        Texture(uv, blockTexture.textureLeft),
        Texture(uv, blockTexture.textureTop),
        Texture(uv, blockTexture.textureBottom),
    )
    
}