package de.studiocode.miniatureblocks.resourcepack.model

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

class MainModelData(jsonObject: JsonObject) : ModelData(jsonObject) {

    private val overrides: JsonArray

    init {
        jsonObject.addProperty("parent", "block/jack_o_lantern")

        overrides = if (jsonObject.has("overrides")) jsonObject.get("overrides").asJsonArray
        else JsonArray()

        jsonObject.add("overrides", overrides)
    }

    fun addOverride(customModelData: Int, model: String) {
        val overrideObj = JsonObject()
        val predicate = JsonObject()
        predicate.addProperty("custom_model_data", customModelData)
        overrideObj.add("predicate", predicate)
        overrideObj.addProperty("model", model)

        overrides.add(overrideObj)
    }

    fun removeOverride(name: String) {
        overrides.remove(overrides.map(JsonElement::getAsJsonObject)
                        .first { it.get("model").asString.split("/")[2] == name })
    }

    fun getNextCustomModelData(): Int {
        var highest = 10000000

        for (overrideObj in overrides.map(JsonElement::getAsJsonObject)) {
            if (overrideObj.has("predicate")) {
                val predicate = overrideObj.get("predicate").asJsonObject
                if (predicate.has("custom_model_data")) {
                    val customModelData = predicate.get("custom_model_data").asInt
                    if (customModelData > highest) highest = customModelData
                }
            }
        }

        return highest + 1
    }

    fun getModels(): ArrayList<Model> {
        val models = ArrayList<Model>()
        for (overrideObj in overrides.map(JsonElement::getAsJsonObject)) {
            val name = overrideObj.get("model").asString.split("/")[2]
            val customModelData = overrideObj.get("predicate").asJsonObject.get("custom_model_data").asInt

            models.add(Model(name, customModelData))
        }

        return models
    }

}