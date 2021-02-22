package de.studiocode.miniatureblocks.resourcepack.model.part

import de.studiocode.miniatureblocks.resourcepack.model.part.impl.CubePart
import de.studiocode.miniatureblocks.resourcepack.model.part.impl.SlabPart
import de.studiocode.miniatureblocks.resourcepack.model.part.impl.StairPart
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.isSlab
import de.studiocode.miniatureblocks.util.isStair
import org.bukkit.block.Block
import java.lang.reflect.Constructor

object PartManager {
    
    private val partTypes = LinkedHashMap<(Block) -> Boolean, Constructor<out Part>>()
    
    init {
        addPartType(StairPart::class.java) { it.isStair() }
        addPartType(SlabPart::class.java) { it.isSlab(false) }
        addPartType(CubePart::class.java) { BlockTexture.has(it.type) }
    }
    
    private fun addPartType(clazz: Class<out Part>, acceptsTypeTest: (Block) -> Boolean) {
        partTypes[acceptsTypeTest] = clazz.getConstructor(Block::class.java)
    }
    
    fun getConstructOf(block: Block): Part {
        for ((test, constructor) in partTypes) {
            if (test.invoke(block)) {
                return constructor.newInstance(block)
            }
        }
        throw UnsupportedOperationException("Unsupported block type")
    }
    
}