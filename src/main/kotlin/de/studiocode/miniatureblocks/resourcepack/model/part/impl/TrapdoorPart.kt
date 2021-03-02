package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.TrapdoorBlockData
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.Material

private val INVERTED_TRAPDOOR_MATERIALS = listOf(Material.OAK_TRAPDOOR, Material.DARK_OAK_TRAPDOOR) // some trapdoors have their textures rotated 180°
private const val TRAPDOOR_HEIGHT = 3.0 / 16.0
private const val HALF_TRAPDOOR_HEIGHT = TRAPDOOR_HEIGHT / 2

class TrapdoorPart(data: TrapdoorBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    private val inverted = INVERTED_TRAPDOOR_MATERIALS.contains(data.material)
    private val open = data.open
    private val top = data.top
    
    override val elements: List<Element>
    override val rotatable = true
    
    init {
        val element = createTrapdoorElement()
        elements = listOf(element)
        
        val facing = Direction.of(data.facing)
        
        if (open) {
            if (top) element.rotateTexturesAroundYAxis(2)
            // rotation is always the same because direction rotation hasn't been applied yet
            val origin = doubleArrayOf(0.0, HALF_TRAPDOOR_HEIGHT, 1.0 - HALF_TRAPDOOR_HEIGHT)
            element.rotatePosAroundXAxis(1, origin)
            element.rotateTexturesAroundXAxis(1)
        } else if (top) {
            element.rotatePosAroundXAxis(2)
        }
        
        addRotation(facing)
        applyRotation()
    }
    
    private fun createTrapdoorElement(): Element {
        val fromPos = Point3D(0.0, 0.0, 0.0)
        val toPos = Point3D(1.0, TRAPDOOR_HEIGHT, 1.0)
        
        val element = Element(fromPos, toPos,
            Texture(Texture.UV(0.0, 0.0, 1.0, TRAPDOOR_HEIGHT), blockTexture.getTexture(Direction.NORTH)),
            Texture(Texture.UV(0.0, 0.0, 1.0, TRAPDOOR_HEIGHT), blockTexture.getTexture(Direction.EAST)),
            Texture(Texture.UV(0.0, 0.0, 1.0, TRAPDOOR_HEIGHT), blockTexture.getTexture(Direction.SOUTH)),
            Texture(Texture.UV(0.0, 0.0, 1.0, TRAPDOOR_HEIGHT), blockTexture.getTexture(Direction.WEST)),
            Texture(Texture.UV(0.0, 0.0, 1.0, 1.0), blockTexture.getTexture(Direction.UP), 2),
            Texture(Texture.UV(0.0, 0.0, 1.0, 1.0), blockTexture.getTexture(Direction.DOWN))
        )
        
        if (inverted) {
            if (open) {
                element.addTextureRotation(2, *Direction.values())
            } else {
                element.addTextureRotation(if (top) -1 else 1, Direction.UP, Direction.DOWN)
            }
        }
        
        return element
    }
    
}