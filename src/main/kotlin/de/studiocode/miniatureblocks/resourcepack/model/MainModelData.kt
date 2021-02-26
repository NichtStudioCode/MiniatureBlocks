package de.studiocode.miniatureblocks.resourcepack.model

import com.google.gson.JsonObject
import org.bukkit.Material
import java.io.File

class MainModelData(file: File) : ModelData(file, Material.STRUCTURE_VOID) {
    
    override fun createJsonObject(): JsonObject {
        val mainObj = super.createJsonObject()
        
        mainObj.addProperty("parent", "item/generated")
        val texturesObj = JsonObject()
        texturesObj.addProperty("layer0", "item/structure_void")
        mainObj.add("textures", texturesObj)
        
        return mainObj
    }
    
}