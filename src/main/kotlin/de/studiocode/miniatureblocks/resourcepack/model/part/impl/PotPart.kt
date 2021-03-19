package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncBlockData
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.element.impl.SameTextureElement
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.Material

private const val POT_SIZE = 6.0 / 16.0
private const val HALF_POT_SIZE = POT_SIZE / 2

private const val BASE_HEIGHT = 5.0 / 16.0
private const val RAIL_SIZE = 1.0 / 16.0

class PotPart(data: AsyncBlockData) : Part() {
    
    private val textures = BlockTexture.of(data.material).textures
    private val potTexture = textures[0]
    private val dirtTexture = textures[1]
    
    override val elements = ArrayList<Element>()
    
    init {
        elements += createEmptyPotElements()
        
        if (data.material == Material.POTTED_CACTUS) {
            elements += createCactusElement()
        } else if (data.material != Material.FLOWER_POT) {
            val pottedTextures = textures.sliceArray(2 until textures.size)
            val plantElements = SerializedPart.getModelElements("model/cross", pottedTextures)
            elements += plantElements.onEach { it.scaleCentred(0.75); it.move(0.0, 0.125, 0.0) }
        }
    }
    
    private fun createEmptyPotElements(): List<Element> {
        val baseElement = Element(
            Point3D(0.5 - HALF_POT_SIZE, 0.0, 0.5 - HALF_POT_SIZE),
            Point3D(0.5 + HALF_POT_SIZE, BASE_HEIGHT, 0.5 + HALF_POT_SIZE),
            Texture(potTexture), Texture(potTexture), Texture(potTexture), Texture(potTexture),
            Texture(dirtTexture),
            Texture(Texture.UV(0.5 - HALF_POT_SIZE, 10.0 / 16.0, 0.5 + HALF_POT_SIZE, 1.0), potTexture),
        )
        
        return createRailingElements() + baseElement
    }
    
    private fun createRailingElements(): List<Element> {
        val elements = ArrayList<Element>()
        repeat(2) {
            // rail element on z axis
            val start = if (it == 0) 0.5 - HALF_POT_SIZE else 0.5 + HALF_POT_SIZE - RAIL_SIZE
            val end = start + RAIL_SIZE
            
            elements += SameTextureElement(
                Point3D(0.5 - HALF_POT_SIZE, BASE_HEIGHT, start),
                Point3D(0.5 + HALF_POT_SIZE, BASE_HEIGHT + RAIL_SIZE, end),
                potTexture
            )
            
            // rail element on x axis
            elements += SameTextureElement(
                Point3D(start, BASE_HEIGHT, 0.5 - HALF_POT_SIZE + RAIL_SIZE),
                Point3D(end, BASE_HEIGHT + RAIL_SIZE, 0.5 + HALF_POT_SIZE - RAIL_SIZE),
                potTexture
            )
        }
        
        return elements
    }
    
    private fun createCactusElement(): Element {
        return Element(
            Point3D(0.5 - HALF_POT_SIZE + RAIL_SIZE, BASE_HEIGHT, 0.5 - HALF_POT_SIZE + RAIL_SIZE),
            Point3D(0.5 + HALF_POT_SIZE - RAIL_SIZE, 1.0, 0.5 + HALF_POT_SIZE - RAIL_SIZE),
            Texture(textures[2]),
            Texture(textures[3]),
            Texture(textures[4]),
            Texture(textures[5]),
            Texture(textures[6]),
            Texture(""),
        )
    }
    
}