package de.studiocode.miniatureblocks.resourcepack.file

import com.google.gson.JsonObject
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import org.bukkit.Material

class MaterialModelDataFile(resourcePack: ResourcePack) :
    ModelFile(
        Material.GRAY_STAINED_GLASS,
        resourcePack,
        "assets/minecraft/models/item/gray_stained_glass.json"
    ) {
    
    override fun createJsonObject(): JsonObject {
        val mainObj = super.createJsonObject()
        mainObj.addProperty("parent", "block/gray_stained_glass")
        return mainObj
    }
    
    fun getModelByMaterial(material: Material): CustomModel? {
        return getCustomModelFromPath("item/materialitem/${material.name.toLowerCase()}")
    }
    
}