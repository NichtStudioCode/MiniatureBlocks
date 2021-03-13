package de.studiocode.miniatureblocks.resourcepack.file

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import de.studiocode.miniatureblocks.util.IntAccessor
import de.studiocode.miniatureblocks.util.writeToFile
import java.io.File

class AnimationDataFile : RPFile {
    
    private val jsonObject = if (exists()) JsonParser().parse(readText()).asJsonObject else JsonObject()
    private val animationObj = jsonObject.get("animation")?.asJsonObject
        ?: JsonObject().also { jsonObject.add("animation", it) }
    
    var frametime by IntAccessor(animationObj, "frametime")
    
    constructor(resourcePack: ResourcePack, file: File) : super(resourcePack, file)
    
    constructor(resourcePack: ResourcePack, path: String) : super(resourcePack, path)
    
    fun save() {
        jsonObject.writeToFile(this)
    }
    
}