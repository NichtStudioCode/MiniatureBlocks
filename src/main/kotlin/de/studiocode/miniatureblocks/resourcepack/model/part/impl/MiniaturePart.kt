package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.build.concurrent.AsyncMiniature
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.RotationData
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.storage.serialization.ModelDeserializer
import de.studiocode.miniatureblocks.util.fromJson
import de.studiocode.miniatureblocks.util.point.Point3D
import de.studiocode.miniatureblocks.util.registerTypeAdapter
import org.bukkit.Axis
import java.io.File
import kotlin.math.round

private val GSON = GsonBuilder()
    .registerTypeAdapter(ModelDeserializer)
    .create()

class MiniaturePart(data: AsyncMiniature) : Part() {
    
    override val elements = ArrayList<Element>()
    
    init {
        val file = File(MiniatureBlocks.INSTANCE.resourcePack.moddedItemModels, data.model.name + ".json")
        val jsonObject = file.reader().use { JsonParser().parse(it) } as JsonObject
        val textures = jsonObject.get("textures") as JsonObject
        val elements = jsonObject.get("elements") as JsonArray
        
        this.elements += GSON.fromJson<List<Element>>(elements)!!
        
        this.elements.forEach { element ->
            element.textures.values.forEach { texture ->
                val textureLocation = texture.textureLocation
                if (textureLocation.startsWith("#")) {
                    val index = textureLocation.substring(1)
                    texture.textureLocation = textures.get(index).asString
                }
            }
        }
        
        
        val rotation = (round(data.yaw / 22.5).toInt() + 8).mod(16)
        var fullRotation = rotation / 4
        var otherRotation = (rotation - (fullRotation * 4))
        if (otherRotation == 3) {
            fullRotation += 1
            otherRotation = -1
        }
        
        rotate(0, fullRotation)
        
        if (otherRotation != 0) {
            this.elements.forEach { it.rotationData = RotationData(otherRotation * -22.5f, Axis.Y, Point3D(0.5, 0.5, 0.5), false) }
        }
    }
    
}