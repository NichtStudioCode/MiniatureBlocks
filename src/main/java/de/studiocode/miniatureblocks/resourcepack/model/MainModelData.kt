package de.studiocode.miniatureblocks.resourcepack.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File

class MainModelData(private val file: File) {

    val customModels = ArrayList<CustomModel>()
    
    init {
        if (file.exists()) {
            val jsonObj = JsonParser().parse(file.inputStream().reader()).asJsonObject
            if (jsonObj.has("overrides")) {
                val overrides = jsonObj.get("overrides").asJsonArray
                for (customModelObj in overrides.map { it.asJsonObject }) {
                    val customModelData = customModelObj.get("predicate")?.asJsonObject?.get("custom_model_data")?.asInt ?: 0
                    val model = customModelObj.get("model")?.asString ?: ""
                    
                    customModels.add(CustomModel(customModelData, model))
                }
            }
        }
    }
    
    fun writeToFile() {
        val jsonObject = createJsonObject()
        file.writeText(jsonObject.toString())
    }
    
    private fun createJsonObject(): JsonObject {
        val mainObj = JsonObject()
        mainObj.addProperty("parent", "block/jack_o_lantern")
        
        val overrides = JsonArray()
        for (customModel in customModels) {
            val customModelObj = JsonObject()
            val predicateObj = JsonObject()
            predicateObj.addProperty("custom_model_data", customModel.customModelData)
            customModelObj.add("predicate", predicateObj)
            customModelObj.addProperty("model", customModel.model)
            
            overrides.add(customModelObj)
        }
        mainObj.add("overrides", overrides)
        
        return mainObj
    }

    fun hasCustomModelData(customModelData: Int): Boolean = customModels.any { it.customModelData == customModelData}
    
    fun getNextCustomModelData(): Int = (customModels.map { it.customModelData }.max() ?: 1000000) + 1
    
    fun removeModel(name: String) = customModels.removeIf { it.name == name }
    
    class CustomModel(val customModelData: Int, val model: String) {
        
        val name = model.split("/")[2]
        
    }

}