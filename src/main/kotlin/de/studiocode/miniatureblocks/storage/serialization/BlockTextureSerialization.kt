package de.studiocode.miniatureblocks.storage.serialization

import com.google.gson.*
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.addAll
import de.studiocode.miniatureblocks.util.getAllStrings
import java.lang.reflect.Type

object BlockTextureSerializer : JsonSerializer<BlockTexture> {
    
    override fun serialize(src: BlockTexture, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.addProperty("material", src.material?.name ?: "")
        obj.addProperty("rotation", src.defaultRotation.name)
        obj.add("textures", JsonArray().apply { addAll(src.textures) })
        return obj
    }
    
}

object BlockTextureDeserializer : JsonDeserializer<BlockTexture> {
    
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): BlockTexture {
        val obj = json.asJsonObject
        val materialName = obj.get("material").asString
        val rotation = Direction.valueOf(obj.get("rotation").asString)
        val textures = obj.get("textures").asJsonArray.getAllStrings().toTypedArray()
        return BlockTexture(materialName, textures, rotation)
    }
    
}