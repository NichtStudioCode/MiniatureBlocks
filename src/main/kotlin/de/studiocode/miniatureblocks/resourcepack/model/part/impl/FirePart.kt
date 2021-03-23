package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncFire
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import kotlin.random.Random

class FirePart(data: AsyncFire) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    override val elements = ArrayList<Element>()
    
    init {
        val textures = arrayOf(blockTexture.textures[if (Random.nextBoolean()) 0 else 1])
        if (data.faces.isNotEmpty()) {
            data.faces.map(Direction::of).forEach { direction ->
                if (direction == Direction.UP) {
                    val rotate = Random.nextBoolean()
                    SerializedPart.getModelElements("model/fire_up", textures)
                        .forEach {
                            elements += it
                            if (rotate) {
                                it.rotatePosAroundYAxis(1)
                                it.rotateTexturesAroundYAxis(1)
                            }
                        }
                } else {
                    SerializedPart.getModelElements("model/fire_side", textures)
                        .forEach {
                            elements += it
                            it.rotatePosAroundYAxis(direction.yRot)
                            it.rotateTexturesAroundYAxis(direction.yRot)
                        }
                }
            }
        } else {
            elements += SerializedPart.getModelElements("model/fire", textures)
        }
    }
    
}