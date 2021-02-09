package de.studiocode.miniatureblocks.miniature.data.types

import com.google.gson.JsonParser
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.miniature.data.impl.NormalMiniatureData
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

object NormalMiniatureDataType : PersistentDataType<String, NormalMiniatureData> {
    
    private val MAIN_MODEL_DATA = MiniatureBlocks.INSTANCE.resourcePack.mainModelData
    
    override fun getPrimitiveType() = String::class.java
    
    override fun getComplexType() = NormalMiniatureData::class.java
    
    override fun toPrimitive(complex: NormalMiniatureData, context: PersistentDataAdapterContext): String {
        if (!complex.isValid()) throw IllegalArgumentException("Miniature data is invalid")
        
        return complex.model!!.asJsonArray().toString()
    }
    
    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): NormalMiniatureData {
        val jsonArray = JsonParser().parse(primitive).asJsonArray
        return NormalMiniatureData(MAIN_MODEL_DATA.getExactModel(jsonArray))
    }
    
}