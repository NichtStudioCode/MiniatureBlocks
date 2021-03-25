package de.studiocode.miniatureblocks.resourcepack.file

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import org.bukkit.Material
import java.io.File

class TextureModelDataFile(resourcePack: ResourcePack) :
    ModelFile(
        Material.WHITE_STAINED_GLASS,
        resourcePack,
        "assets/minecraft/models/item/white_stained_glass.json"
    ) {
    
    override fun createJsonObject(): JsonObject {
        val mainObj = super.createJsonObject()
        mainObj.addProperty("parent", "block/white_stained_glass")
        return mainObj
    }
    
    fun getModelByTextureLocation(textureLocation: String): CustomModel? {
        return getCustomModelFromPath("item/textureitem/$textureLocation")
    }
    
    fun getTextureLocation(customModel: CustomModel): String {
        val modelFile = File(resourcePack.models, customModel.path + ".json")
        return JsonParser()
            .parse(modelFile.readText()).asJsonObject
            .get("textures").asJsonObject
            .get("1").asString
    }
    
}