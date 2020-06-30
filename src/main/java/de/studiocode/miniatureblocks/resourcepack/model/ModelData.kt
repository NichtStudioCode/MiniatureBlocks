package de.studiocode.miniatureblocks.resourcepack.model

import com.google.gson.JsonObject
import java.io.File

open class ModelData(val jsonObject: JsonObject) {
    
    fun writeToFile(file: String) = writeToFile(File(file))
    
    fun writeToFile(file: File) = file.writeText(jsonObject.toString())
    
}