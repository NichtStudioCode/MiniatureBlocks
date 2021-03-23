package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.storage.serialization.ModelDeserializer
import de.studiocode.miniatureblocks.util.FileUtils
import de.studiocode.miniatureblocks.util.fromJson
import de.studiocode.miniatureblocks.util.registerTypeAdapter
import kotlin.math.max

object SerializedPart {
    
    private val MODELS: Map<String, List<Element>>
    
    init {
        val models = HashMap<String, List<Element>>()
        
        val gson = GsonBuilder()
            .registerTypeAdapter(ModelDeserializer)
            .create()
        
        FileUtils.listExtractableFiles("model/").forEach {
            val bytes = FileUtils.getFileInJar("/$it")
            val jsonArray = JsonParser().parse(String(bytes)).asJsonArray
            models[it.substringBeforeLast('.')] = gson.fromJson(jsonArray)!!
        }
        
        MODELS = models
    }
    
    fun getModelElements(model: String): List<Element> {
        return getCloneOfElements(model)
    }
    
    fun getModelElements(model: String, textures: Array<String>): List<Element> {
        return getCloneOfElements(model).also { applyTextures(it, textures) }
    }
    
    private fun getCloneOfElements(model: String): List<Element> {
        val clonedList = ArrayList<Element>()
        MODELS[model]!!.forEach {
            clonedList.add(it.clone())
        }
        
        return clonedList
    }
    
    fun applyTextures(elements: List<Element>, textures: Array<String>) {
        elements.forEach { element ->
            element.textures
                .map { it.value }
                .forEach { texture ->
                    val index = texture.textureLocation.toIntOrNull()
                    if (index != null)
                        texture.textureLocation = textures[index]
                }
        }
    }
    
    fun countTexturesNeeded(elements: List<Element>): Int {
        var highest = 0
        elements.forEach { element ->
            element.textures
                .map { it.value }
                .forEach { texture ->
                    val index = texture.textureLocation.toIntOrNull()
                    if (index != null) highest = max(highest, index)
                }
        }
        
        return highest + 1
    }
    
}