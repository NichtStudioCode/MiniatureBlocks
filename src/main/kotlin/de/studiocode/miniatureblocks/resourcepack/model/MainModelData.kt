package de.studiocode.miniatureblocks.resourcepack.model

import com.google.gson.JsonObject
import org.bukkit.Material
import java.io.File

class MainModelData(file: File) : ModelData(file, Material.BLACK_STAINED_GLASS) {
    
    override fun createJsonObject(): JsonObject {
        val mainObj = super.createJsonObject()
        mainObj.addProperty("parent", "block/black_stained_glass")
        return mainObj
    }
    
}