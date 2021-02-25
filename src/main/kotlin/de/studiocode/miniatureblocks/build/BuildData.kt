package de.studiocode.miniatureblocks.build

import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import org.bukkit.block.Block

class BuildData(val data: Map<BuildBlockData, Part>, val size: Int) {
    
    class BuildBlockData(val x: Int, val y: Int, val z: Int, val block: Block, private val blockedSides: List<Direction>) {
        
        fun isSideVisible(side: Direction) = !blockedSides.contains(side)
        
    }
    
}