package de.studiocode.miniatureblocks.storage.serialization

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import de.studiocode.miniatureblocks.build.EMPTY_TEXTURE
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.RotationData
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.model.element.toUV
import de.studiocode.miniatureblocks.util.getAllDoubles
import de.studiocode.miniatureblocks.util.point.Point3D
import de.studiocode.miniatureblocks.util.point.toPoint3D
import org.bukkit.Axis
import java.lang.reflect.Type

object ModelDeserializer : JsonDeserializer<List<Element>> {
    
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<Element> {
        val elements = ArrayList<Element>()
        json.asJsonArray
            .map(JsonElement::getAsJsonObject)
            .forEach { elementObj ->
                val positions = getPositions(elementObj)
                val from = positions.first
                val to = positions.second
                val shade = getShade(elementObj)
                val textures = getTextures(elementObj.getAsJsonObject("faces"))
                
                val element = Element(from, to, *textures.toTypedArray())
                element.shade = shade
                
                val rotationObj = elementObj.get("rotation")?.asJsonObject
                if (rotationObj != null) element.setRotation(rotationObj)
                
                elements += element
            }
        
        return elements
    }
    
    private fun getPositions(elementObj: JsonObject): Pair<Point3D, Point3D> {
        val from = elementObj.get("from")
            .asJsonArray
            .getAllDoubles()
            .map { it / 16.0 }
            .toPoint3D()
        
        val to = elementObj.get("to")
            .asJsonArray
            .getAllDoubles()
            .map { it / 16.0 }
            .toPoint3D()
        
        return from to to
    }
    
    private fun getShade(elementObj: JsonObject) = elementObj.get("shade")?.asBoolean ?: true
    
    private fun getTextures(faces: JsonObject): List<Texture> {
        val textures = ArrayList<Texture>()
        Direction.values().forEach { direction ->
            val face = faces.get(direction.name.toLowerCase())?.asJsonObject
            textures.add(
                if (face != null) {
                    val uv = face.get("uv")
                        ?.asJsonArray
                        ?.getAllDoubles()
                        ?.map { it / 16.0 }
                        ?.toUV()
                    val rotation = face.get("rotation")?.asInt?.div(90) ?: 0
                    val texture = face.get("texture")!!.asString
                    val tintIndex = face.get("tintindex")?.asInt
                    
                    if (uv == null) Texture(texture, rotation, tintIndex)
                    else Texture(uv, texture, rotation, tintIndex)
                } else EMPTY_TEXTURE
            )
        }
        
        return textures
    }
    
    private fun Element.setRotation(rotation: JsonObject) {
        val angle = rotation.get("angle").asFloat
        val axis = Axis.valueOf(rotation.get("axis").asString.toUpperCase())
        val origin = rotation.get("origin").asJsonArray.getAllDoubles().map { it / 16.0 }.toPoint3D()
        val rescale = rotation.get("rescale")?.asBoolean ?: false
        
        rotationData = RotationData(angle, axis, origin, rescale)
    }
    
}