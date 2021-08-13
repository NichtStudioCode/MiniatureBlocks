package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncMultipleFacing
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

private const val SIDE_MODEL = "model/chorus_plant/chorus_plant_side"
private val NO_SIDE_MODELS = listOf(
    "model/chorus_plant/chorus_plant_0",
    "model/chorus_plant/chorus_plant_1",
    "model/chorus_plant/chorus_plant_2",
    "model/chorus_plant/chorus_plant_3"
)

class ChorusPlantPart(data: AsyncMultipleFacing) : Part() {
    
    private val texture = BlockTexture.of(data.material)
    override val elements = ArrayList<Element>()
    
    init {
        val directions = data.faces.mapTo(HashSet(), Direction::of)
        Direction.values().forEach { direction ->
            val elements = SerializedPart.getModelElements(if (directions.contains(direction)) SIDE_MODEL else NO_SIDE_MODELS.random(), texture.textures)
            this.elements += elements.onEach { element -> element.rotate(direction) }
        }
    }
    
}