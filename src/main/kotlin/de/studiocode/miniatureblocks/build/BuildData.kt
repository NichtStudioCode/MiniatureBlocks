package de.studiocode.miniatureblocks.build

import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.part.Part

class BuildData(val data: Map<BuildBlockData, Part>, val size: Int) {
    
    class BuildBlockData(val x: Int, val y: Int, val z: Int, private val blockedSides: List<Direction>) {
        
        fun isSideVisible(side: Direction) = !blockedSides.contains(side)
        
    }
    
}