package de.studiocode.miniatureblocks.resourcepack.model

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.studiocode.miniatureblocks.MiniatureBlocks
import org.bukkit.Material
import java.io.File

class TextureItemModelData(file: File) : ModelData(file, Material.WHITE_STAINED_GLASS) {
    
    override fun createJsonObject(): JsonObject {
        val mainObj = super.createJsonObject()
        mainObj.addProperty("parent", "item/generated")
        val texturesObj = JsonObject()
        texturesObj.addProperty("layer0", "block/dandelion")
        mainObj.add("textures", texturesObj)
        return mainObj
    }
    
    fun getModelByTextureLocation(textureLocation: String): CustomModel? {
        val modelName = textureLocation.replace("block/", "").replace("/", "")
        return getCustomModelFromName(modelName)
    }
    
    fun getTextureLocation(customModel: CustomModel): String {
        val modelFile = File(MiniatureBlocks.INSTANCE.resourcePack.modelsDir, customModel.model + ".json")
        return JsonParser()
            .parse(modelFile.readText()).asJsonObject
            .get("textures").asJsonObject
            .get("1").asString
    }
    
}