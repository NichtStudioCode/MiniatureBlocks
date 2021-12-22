package de.studiocode.miniatureblocks.resourcepack.file

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.studiocode.invui.item.builder.ItemBuilder
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import org.bukkit.Material

abstract class ModelFile(val material: Material, resourcePack: ResourcePack, path: String) : RPFile(resourcePack, path) {
    
    val customModels: ArrayList<CustomModel> by lazy { getCustomModelsFromFile() }
    
    private fun getCustomModelsFromFile(): ArrayList<CustomModel> {
        val customModels = ArrayList<CustomModel>()
        if (exists()) {
            val mainObj = JsonParser().parse(readText()).asJsonObject
            if (mainObj.has("overrides")) {
                val overrides = mainObj.get("overrides").asJsonArray
                for (customModelObj in overrides.map { it.asJsonObject }) {
                    val customModelData = customModelObj.get("predicate")
                        ?.asJsonObject
                        ?.get("custom_model_data")
                        ?.asInt ?: 0
                    
                    val model = customModelObj.get("model")?.asString ?: ""
                    customModels.add(CustomModel(customModelData, model))
                }
            }
        }
        
        return customModels
    }
    
    fun writeToFile() {
        writeText(createJsonObject().toString())
    }
    
    protected open fun createJsonObject(): JsonObject {
        val mainObj = JsonObject()
        val overrides = JsonArray()
        
        for (customModel in customModels) {
            val customModelObj = JsonObject()
            val predicateObj = JsonObject()
            predicateObj.addProperty("custom_model_data", customModel.customModelData)
            customModelObj.add("predicate", predicateObj)
            customModelObj.addProperty("model", customModel.path)
            
            overrides.add(customModelObj)
        }
        mainObj.add("overrides", overrides)
        
        return mainObj
    }
    
    fun hasModel(name: String) = customModels.any { it.name == name }
    
    fun getNextCustomModelData(): Int = (customModels.map { it.customModelData }.maxOrNull() ?: 0) + 1
    
    fun removeModel(name: String) = customModels.removeIf { it.name == name }
    
    fun getCustomModelFromPath(path: String): CustomModel? = customModels.find { it.path == path }
    
    fun getCustomModelFromName(name: String): CustomModel? = customModels.find { it.name == name }
    
    fun getExactModel(jsonArray: JsonArray) = getExactModel(jsonArray[0].asString, jsonArray[1].asInt)
    
    private fun getExactModel(name: String, customModelData: Int): CustomModel? =
        customModels.find { it.customModelData == customModelData && it.name == name }
    
    inner class CustomModel(val customModelData: Int, val path: String) {
        
        val name = path.substringAfterLast("/")
        
        fun createItemBuilder(): ItemBuilder =
            ItemBuilder(material).setDisplayName("§f$name").setCustomModelData(customModelData)
        
        fun asJsonArray(): JsonArray {
            val array = JsonArray()
            array.add(name)
            array.add(customModelData)
            
            return array
        }
        
    }
    
}