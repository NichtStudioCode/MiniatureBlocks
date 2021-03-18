package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.storage.serialization.ModelDeserializer
import de.studiocode.miniatureblocks.util.fromJson
import de.studiocode.miniatureblocks.util.registerTypeAdapter

abstract class SerializedPart(model: JsonArray) : Part() {
    
    final override val elements = GsonBuilder()
        .registerTypeAdapter(ModelDeserializer)
        .create()
        .fromJson<List<Element>>(model)!!
    
    init {
        elements.forEach { element ->
            element.textures
                .map { it.value }
                .forEach { texture ->
                    val index = texture.textureLocation.toIntOrNull()
                    if (index != null)
                        texture.textureLocation = getTextureLocation(index)
                }
        }
    }
    
    protected abstract fun getTextureLocation(i: Int): String
    
}