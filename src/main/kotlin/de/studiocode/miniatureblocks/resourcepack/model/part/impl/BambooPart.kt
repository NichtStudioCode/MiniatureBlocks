package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncBamboo
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bukkit.block.data.type.Bamboo

private val STEM_MODELS = listOf(
    listOf(
        "model/bamboo/bamboo_age_0_type_0",
        "model/bamboo/bamboo_age_0_type_1",
        "model/bamboo/bamboo_age_0_type_2",
        "model/bamboo/bamboo_age_0_type_3"
    ),
    
    listOf(
        "model/bamboo/bamboo_age_1_type_0",
        "model/bamboo/bamboo_age_1_type_1",
        "model/bamboo/bamboo_age_1_type_2",
        "model/bamboo/bamboo_age_1_type_3"
    )
)

private val LEAVE_MODELS = listOf("model/bamboo/bamboo_small_leaves", "model/bamboo/bamboo_large_leaves")

class BambooPart(data: AsyncBamboo) : Part() {
    
    private val textures = BlockTexture.of(data.material).textures
    override val elements = ArrayList<Element>()
    
    init {
        when (data.leaves) {
            Bamboo.Leaves.SMALL -> elements += SerializedPart.getModelElements(LEAVE_MODELS[0], textures)
            Bamboo.Leaves.LARGE -> elements += SerializedPart.getModelElements(LEAVE_MODELS[1], textures)
            else -> Unit
        }
        
        elements += SerializedPart.getModelElements(STEM_MODELS[data.age].random(), textures)
    }
    
}