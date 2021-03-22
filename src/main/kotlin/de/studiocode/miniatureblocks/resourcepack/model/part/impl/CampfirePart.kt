package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncCampfire
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

private val models = arrayOf("model/campfire", "model/campfire_off")

class CampfirePart(data: AsyncCampfire) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    override val elements = SerializedPart.getModelElements(models[if (data.lit) 0 else 1], blockTexture.textures)
    
    init {
        addRotation(Direction.of(data.facing))
        applyModifications()
    }
    
}