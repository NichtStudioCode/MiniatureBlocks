package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bukkit.block.Block
import org.bukkit.block.data.type.Slab

class SlabPart(block: Block) : Part() {
    
    private val blockTexture = BlockTexture.of(block.type)
    private val top = (block.blockData as Slab).type == Slab.Type.TOP
    
    private val sideTexture = Texture(doubleArrayOf(0.0, if (top) 0.0 else 8.0, 16.0, if (top) 8.0 else 16.0), blockTexture.textureFront)
    private val topBotTexture = Texture(doubleArrayOf(0.0, 0.0, 16.0, 16.0), blockTexture.textureTop)
    private val bottomTexture = Texture(doubleArrayOf(0.0, 0.0, 16.0, 16.0), blockTexture.textureBottom)
    
    override val elements = listOf(SlabElement())
    override val rotatable = true
    
    init {
        if (top) addRotation(2, 0)
    }
    
    inner class SlabElement : Element(doubleArrayOf(0.0, 0.0, 0.0), doubleArrayOf(1.0, 0.5, 1.0),
        sideTexture, sideTexture, sideTexture, sideTexture, topBotTexture, bottomTexture) {
        
        override fun rotateTexturesAroundXAxis(rotation: Int) {
            // prevent texture rotation
        }
        
        override fun rotateTexturesAroundYAxis(rotation: Int) {
            // prevent texture rotation
        }
    }
    
}