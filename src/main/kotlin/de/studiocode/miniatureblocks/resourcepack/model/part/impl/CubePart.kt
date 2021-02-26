package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.Direction.*
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bukkit.block.Block
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Orientable

class CubePart(block: Block) : Part() {
    
    private val uv = doubleArrayOf(0.0, 0.0, 1.0, 1.0)
    private val blockTexture = BlockTexture.of(block.type)
    
    override val elements = listOf(CubeElement())
    override val rotatable = false
    
    init {
        addRotation(blockTexture.defaultRotation)
        
        val data = block.blockData
        if (data is Directional) addRotation(Direction.of(data.facing))
        else if (data is Orientable) addRotation(Direction.of(data.axis))
        applyRotation()
    }
    
    inner class CubeElement : Element(
        doubleArrayOf(0.0, 0.0, 0.0), doubleArrayOf(1.0, 1.0, 1.0),
        Texture(uv, blockTexture.getTexture(NORTH)),
        Texture(uv, blockTexture.getTexture(EAST)),
        Texture(uv, blockTexture.getTexture(SOUTH)),
        Texture(uv, blockTexture.getTexture(WEST)),
        Texture(uv, blockTexture.getTexture(UP)),
        Texture(uv, blockTexture.getTexture(DOWN)),
    )
    
}