package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import com.google.gson.JsonParser
import de.studiocode.miniatureblocks.build.concurrent.ChestBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

private val models = arrayOf("chest", "chest_left", "chest_right").map {
    JsonParser().parse(ChestPart::class.java.getResource("/model/$it.json").readText()).asJsonArray
}
 
class ChestPart(val data: ChestBlockData) : Part() {
    
    private val textures = BlockTexture.of(data.material).textures
    override val elements = ArrayList<Element>()
    
    init {
        elements += object : SerializedPart(models[data.type.ordinal]!!) {
            override fun getTextureLocation(i: Int) = textures[data.type.ordinal]
        }.elements
        
        addRotation(Direction.of(data.facing))
        applyModifications()
    }
    
}