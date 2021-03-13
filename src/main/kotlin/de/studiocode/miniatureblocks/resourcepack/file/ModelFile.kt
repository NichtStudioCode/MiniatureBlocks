package de.studiocode.miniatureblocks.resourcepack.file

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.studiocode.invui.item.ItemBuilder
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import org.bukkit.Material

abstract class ModelFile(val material: Material, resourcePack: ResourcePack, path: String) : RPFile(resourcePack, path) {
    
    val customModels: ArrayList<CustomModel> by lazy { getCustomModelsFromFile() }
    
    protected var mainObj: JsonObject = JsonObject()
    private var overrides: JsonArray = JsonArray()
    
    private fun getCustomModelsFromFile(): ArrayList<CustomModel> {
        val customModels = ArrayList<CustomModel>()
        if (exists()) {
            mainObj = JsonParser().parse(inputStream().reader()).asJsonObject
            if (mainObj.has("overrides")) {
                overrides = mainObj.get("overrides").asJsonArray
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
        writeToJsonObject()
        writeText(mainObj.toString())
    }
    
    protected open fun writeToJsonObject() {
        mainObj = JsonObject()
        overrides = JsonArray()
        
        for (customModel in customModels) {
            val customModelObj = JsonObject()
            val predicateObj = JsonObject()
            predicateObj.addProperty("custom_model_data", customModel.customModelData)
            customModelObj.add("predicate", predicateObj)
            customModelObj.addProperty("model", customModel.model)
            
            overrides.add(customModelObj)
        }
        mainObj.add("overrides", overrides)
    }
    
    fun hasModel(name: String) = customModels.any { it.name == name }
    
    fun hasCustomModelData(customModelData: Int): Boolean = customModels.any { it.customModelData == customModelData }
    
    fun getNextCustomModelData(): Int = (customModels.map { it.customModelData }.maxOrNull() ?: 0) + 1
    
    fun removeModel(name: String) = customModels.removeIf { it.name == name }
    
    fun getExactModel(name: String, customModelData: Int): CustomModel? =
        customModels.find { it.customModelData == customModelData && it.name == name }
    
    fun getExactModel(jsonArray: JsonArray) = getExactModel(jsonArray[0].asString, jsonArray[1].asInt)
    
    fun getCustomModelFromCustomModelData(customModelData: Int): CustomModel? =
        customModels.find { it.customModelData == customModelData }
    
    fun getCustomModelFromName(name: String): CustomModel? = customModels.find { it.name == name }
    
    inner class CustomModel(val customModelData: Int, val model: String) {
        
        val name = model.substringAfterLast("/")
        
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