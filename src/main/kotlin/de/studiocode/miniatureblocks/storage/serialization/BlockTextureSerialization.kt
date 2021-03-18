package de.studiocode.miniatureblocks.storage.serialization

import com.google.gson.*
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.addAll
import de.studiocode.miniatureblocks.util.getAllStrings
import java.lang.reflect.Type

object BlockTextureSerializer : JsonSerializer<BlockTexture> {
    
    override fun serialize(src: BlockTexture, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return if (src.defaultRotation == Direction.NORTH && src.model == null && src.textures.size == 6) {
            if (src.textures.all { "block/" + (src.material?.name ?: "").toLowerCase() == it }) {
                serializeToDefault(src)
            } else if (src.textures.isNotEmpty() && src.textures.all { it == src.textures[0] }) {
                serializeToDelegate(src)
            } else {
                serializeToCustom(src)
            }
        } else serializeToCustom(src)
    }
    
    private fun serializeToDefault(src: BlockTexture): JsonElement {
        return JsonPrimitive(src.material?.name ?: "")
    }
    
    private fun serializeToDelegate(src: BlockTexture): JsonElement {
        val obj = JsonObject()
        obj.addProperty("material", src.material?.name ?: "")
        obj.addProperty("texture", src.textures[0])
        return obj
    }
    
    private fun serializeToCustom(src: BlockTexture): JsonElement {
        val obj = JsonObject()
        obj.addProperty("material", src.material?.name ?: "")
        if (src.defaultRotation != Direction.NORTH) obj.addProperty("rotation", src.defaultRotation.name)
        if (src.model != null) obj.addProperty("model", src.model)
        obj.add("textures", JsonArray().apply { addAll(src.textures) })
        return obj
    }
    
}

object BlockTextureDeserializer : JsonDeserializer<BlockTexture> {
    
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): BlockTexture {
        return if (json.isJsonPrimitive) {
            deserializeFromDefault(json)
        } else {
            val obj = json.asJsonObject
            val materialName = obj.get("material").asString
            if (obj.has("texture")) {
                deserializeFromDelegate(obj, materialName)
            } else {
                deserializeFromCustom(obj, materialName)
            }
        }
    }
    
    private fun deserializeFromDefault(json: JsonElement): BlockTexture {
        return BlockTexture(json.asString)
    }
    
    private fun deserializeFromDelegate(obj: JsonObject, materialName: String): BlockTexture {
        val texture = obj.get("texture").asString
        return BlockTexture(materialName, texture)
    }
    
    private fun deserializeFromCustom(obj: JsonObject, materialName: String): BlockTexture {
        val rotation = if (obj.has("rotation")) Direction.valueOf(obj.get("rotation").asString) else Direction.NORTH
        val model = if (obj.has("model")) obj.get("model").asString else null
        val textures = obj.get("textures").asJsonArray.getAllStrings().toTypedArray()
        return BlockTexture(materialName, textures, rotation, model)
    }
    
}