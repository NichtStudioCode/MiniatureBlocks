package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.ThreadSafeBlockData
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.Axis

class CrossPart(data: ThreadSafeBlockData) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    
    override val elements = listOf(CrossElement1(), CrossElement2())
    override val rotatable = false
    
    private inner class CrossElement1 :
        Element(
            Point3D(0.078125, 0.0, 0.0), Point3D(1.328125, 1.0, 0.0),
            Texture(Texture.UV(0.0, 0.0, 1.0, 1.0), blockTexture.textures[0])
        ) {
        
        init {
            setRotation(-45f, Axis.Y, 0.0, 0.0, 0.0)
        }
    }
    
    private inner class CrossElement2 :
        Element(
            Point3D(-0.328125, 0.0, 0.0), Point3D(0.921875, 1.0, 0.0),
            Texture(Texture.UV(0.0, 0.0, 1.0, 1.0), blockTexture.textures[1])
        ) {
        
        init {
            setRotation(45f, Axis.Y, 1.0, 0.0, 0.0)
        }
    }
    
}