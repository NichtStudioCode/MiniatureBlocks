package de.studiocode.miniatureblocks.resourcepack.file

import com.google.gson.JsonObject
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import org.bukkit.Material

class MainModelDataFile(resourcePack: ResourcePack) :
    ModelFile(
        Material.BLACK_STAINED_GLASS,
        resourcePack,
        "assets/minecraft/models/item/black_stained_glass.json"
    ) {
    
    override fun createJsonObject(): JsonObject {
        val mainObj = super.createJsonObject()
        mainObj.addProperty("parent", "block/black_stained_glass")
        return mainObj
    }
    
}