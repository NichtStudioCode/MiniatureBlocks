package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.*
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.RotationData
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.Axis
import org.bukkit.block.data.Bisected.Half.TOP

private val CUBE_UV = Texture.UV(0.0, 0.0, 1.0, 1.0)
private val CUBE_FROM = Point3D(0.0, 0.0, 0.0)
private val CUBE_TO = Point3D(1.0, 1.0, 1.0)

class DefaultPart(data: AsyncBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    private val textures: Array<String>
    
    override val elements = ArrayList<Element>()
    
    init {
        val size = blockTexture.textures.size
        textures = if (data is SnowableBlockData || data is BisectedBlockData) {
            if ((data is BisectedBlockData && data.half == TOP) || (data is SnowableBlockData && data.snowy)) {
                blockTexture.textures.copyOfRange(size / 2, size)
            } else blockTexture.textures.copyOfRange(0, size / 2)
        } else blockTexture.textures
        
        val cube =
            if (blockTexture.model != null) {
                loadCustomElements()
                false
            } else {
                elements += createCubeElement()
                true
            }
        
        addRotation(blockTexture.defaultRotation, cube)
        when (data) {
            is DirectionalBlockData -> addRotation(Direction.of(data.facing), cube)
            is OrientableBlockData -> addRotation(Direction.of(data.axis), cube)
            is RotatableBlockData -> {
                val rotation = Direction.ofRotation(data.rotation)
                addRotation(rotation.first)
                val rotationData = RotationData(rotation.second.toFloat(), Axis.Y, Point3D(0.5, 0.0, 0.5), false)
                elements.forEach { it.rotationData = rotationData }
            }
        }
        applyModifications()
    }
    
    private fun addRotation(direction: Direction, cube: Boolean) {
        if (cube) addTextureRotation(direction)
        else addRotation(direction)
    }
    
    private fun loadCustomElements() {
        elements += SerializedPart.getModelElements(blockTexture.model!!, textures)
    }
    
    private fun createCubeElement() =
        Element(
            CUBE_FROM, CUBE_TO,
            Texture(CUBE_UV, textures[0]),
            Texture(CUBE_UV, textures[1]),
            Texture(CUBE_UV, textures[2]),
            Texture(CUBE_UV, textures[3]),
            Texture(CUBE_UV, textures[4]),
            Texture(CUBE_UV, textures[5])
        )
    
}