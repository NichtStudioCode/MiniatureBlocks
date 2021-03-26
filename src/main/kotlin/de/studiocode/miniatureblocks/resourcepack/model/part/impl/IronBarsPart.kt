package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncMultipleFacing
import de.studiocode.miniatureblocks.resourcepack.model.Direction

class IronBarsPart(data: AsyncMultipleFacing) : MultipleFacingPart(data) {
    
    init {
        if (data.faces.size != 0) elements.removeIf { it.name.equals("middle") }
        val keepCap: Direction? = if (data.faces.size == 1) Direction.of(data.faces.first()).opposite else null
        elements.removeIf {
            val name = it.name
            return@removeIf if (name != null) {
                it.name!!.startsWith("cap") && !it.name!!.substringAfter("cap ").equals(keepCap?.name, true)
            } else false
        }
    }
    
}