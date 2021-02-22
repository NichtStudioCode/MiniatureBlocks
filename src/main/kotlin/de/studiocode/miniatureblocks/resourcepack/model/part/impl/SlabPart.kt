package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.resourcepack.model.element.impl.SlabElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bukkit.block.Block
import org.bukkit.block.data.type.Slab

class SlabPart(block: Block) : Part() {
    
    private val blockTexture = BlockTexture.of(block.type)
    private val top = (block.blockData as Slab).type == Slab.Type.TOP
    
    override val elements = listOf(SlabElement(blockTexture))
    override val rotatable = true
    
    init {
        if (top) addPosRotation(2, 0)
        applyRotation()
    }
    
}