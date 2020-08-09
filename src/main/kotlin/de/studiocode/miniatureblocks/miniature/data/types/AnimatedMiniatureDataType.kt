package de.studiocode.miniatureblocks.miniature.data.types

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.miniature.data.impl.AnimatedMiniatureData
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData.CustomModel
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

object AnimatedMiniatureDataType : PersistentDataType<String, AnimatedMiniatureData> {
    
    private val MAIN_MODEL_DATA = MiniatureBlocks.INSTANCE.resourcePack.mainModelData
    private const val TICK_DELAY_KEY = "tickDelay"
    private const val MODELS_KEY = "models"

    override fun getPrimitiveType() = String::class.java

    override fun getComplexType() = AnimatedMiniatureData::class.java

    override fun toPrimitive(complex: AnimatedMiniatureData, context: PersistentDataAdapterContext): String {
        if (!complex.isValid()) throw IllegalArgumentException("Miniature data is invalid")

        val jsonObject = JsonObject()
        jsonObject.addProperty(TICK_DELAY_KEY, complex.tickDelay)
        val modelsArray = JsonArray()
        complex.models!!
                .map(CustomModel::asJsonArray)
                .forEach(modelsArray::add)
        jsonObject.add(MODELS_KEY, modelsArray)
        return jsonObject.toString()
    }

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): AnimatedMiniatureData {
        val jsonObject = JsonParser().parse(primitive).asJsonObject
        val tickDelay = jsonObject.get(TICK_DELAY_KEY).asInt
        val modelsArray = jsonObject.get(MODELS_KEY).asJsonArray
        val models = modelsArray.mapNotNull { MAIN_MODEL_DATA.getExactModel(it.asJsonArray) }
        return if (models.size == modelsArray.size()) {
            AnimatedMiniatureData(tickDelay, models.toTypedArray())
        } else AnimatedMiniatureData(tickDelay, null)
    }

}