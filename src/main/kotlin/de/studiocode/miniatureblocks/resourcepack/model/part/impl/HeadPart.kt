package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import com.google.gson.JsonParser
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.build.concurrent.AsyncHead
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.RotationData
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.Axis
import org.bukkit.Material
import java.util.*

class HeadPart(data: AsyncHead) : Part() {
    
    private val material = data.material
    private val blockTexture = BlockTexture.of(material)
    override val elements = ArrayList<Element>()
    
    init {
        val textures = if (material == Material.PLAYER_HEAD) {
            val url = data.getSkinURL()
            val textures = if (url != null) {
                val path = MiniatureBlocks.INSTANCE.resourcePack.downloadTexture(
                    name = url.substringAfterLast('/'),
                    url = url,
                    frametime = 0,
                    force = false,
                    addTextureLocation = false,
                    replace = false
                )
                
                arrayOf(path)
            } else blockTexture.textures
            
            textures
        } else blockTexture.textures
        
        elements += SerializedPart.getModelElements(blockTexture.models!![0], textures)
        
        val rotation = Direction.ofRotation(data.facing)
        val direction = if (data.wall) {
            move(0.0, 4.0 / 16.0, -4.0 / 16.0)
            rotation.first.opposite
        } else rotation.first
        
        rotate(direction)
        
        val rotationData = RotationData(rotation.second.toFloat(), Axis.Y, Point3D(0.5, 0.5, 0.5), false)
        elements.forEach { it.rotationData = rotationData }
    }
    
    private fun AsyncHead.getSkinURL(): String? {
        try {
            if (gameProfile != null) {
                val properties = gameProfile.properties
                if (properties.containsKey("textures")) {
                    val textureValue = properties["textures"].first().value
                    val json = String(Base64.getDecoder().decode(textureValue))
                    val jsonObj = JsonParser().parse(json).asJsonObject
                    val url = jsonObj.get("textures").asJsonObject.get("SKIN").asJsonObject.get("url").asString
                    return if (url.startsWith("http://textures.minecraft.net")) url else null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return null
    }
    
}