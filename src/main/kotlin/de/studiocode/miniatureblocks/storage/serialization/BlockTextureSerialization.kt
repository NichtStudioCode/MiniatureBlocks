package de.studiocode.miniatureblocks.storage.serialization

import com.google.gson.*
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.addAll
import de.studiocode.miniatureblocks.util.getAllStrings
import java.lang.reflect.Type

object BlockTextureSerializer : JsonSerializer<BlockTexture> {
    
    override fun serialize(src: BlockTexture, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        if (src.defaultRotation == Direction.NORTH
            && src.model == null
            && src.textures.size == 6
            && src.textures.all { "block/" + (src.material?.name ?: "").toLowerCase() == it }) {
            return JsonPrimitive(src.material?.name ?: "")
        } else {
            val jsonObj = JsonObject()
            
            val textures: JsonElement = if (src.textures.size == 6 && src.textures.all { it == src.textures[0] }) {
                JsonPrimitive(src.textures[0])
            } else JsonArray().apply { addAll(src.textures) }
            
            jsonObj.addProperty("material", src.material?.name ?: "")
            if (src.defaultRotation != Direction.NORTH) jsonObj.addProperty("rotation", src.defaultRotation.name)
            if (src.model != null) jsonObj.addProperty("model", src.model)
            jsonObj.add("textures", textures)
            
            return jsonObj
        }
    }
    
}

object BlockTextureDeserializer : JsonDeserializer<BlockTexture> {
    
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): BlockTexture {
        return if (json is JsonPrimitive) {
            BlockTexture(json.asString)
        } else {
            val jsonObj = json.asJsonObject
            val materialName = jsonObj.get("material").asString
            val textures = jsonObj.get("textures") ?: jsonObj.get("texture") // legacy support
            val rotation = jsonObj.get("rotation")?.asString?.let { Direction.valueOf(it) } ?: Direction.NORTH
            val model = jsonObj.get("model")?.asString
            
            if (textures is JsonArray) BlockTexture(materialName, textures.getAllStrings().toTypedArray(), rotation, model)
            else BlockTexture(materialName, textures.asString, rotation, model)
        }
    }
    
}