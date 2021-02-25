package de.studiocode.miniatureblocks.storage

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import de.studiocode.miniatureblocks.storage.serialization.LocationDeserializer
import de.studiocode.miniatureblocks.storage.serialization.LocationSerializer
import de.studiocode.miniatureblocks.storage.serialization.UUIDTypeAdapter
import org.bukkit.Location
import java.io.File
import java.util.*

object PermanentStorage {
    
    val builder: Gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(UUID::class.java, UUIDTypeAdapter)
        .registerTypeAdapter(Location::class.java, LocationSerializer)
        .registerTypeAdapter(Location::class.java, LocationDeserializer)
        .create()
    
    private val file = File("plugins/MiniatureBlocks/storage.json").apply { parentFile.mkdirs() }
    val mainObj: JsonObject = if (file.exists()) JsonParser().parse(file.readText()).asJsonObject else JsonObject()
    
    fun store(key: String, data: Any) {
        mainObj.add(key, builder.toJsonTree(data))
        file.writeText(builder.toJson(mainObj))
    }
    
    inline fun <reified T> retrieve(alternative: T, key: String): T {
        return retrieveOrNull(key) ?: alternative
    }
    
    inline fun <reified T> retrieveOrNull(key: String): T? {
        return builder.fromJson(mainObj.get(key), object : TypeToken<T>() {}.type)
    }
    
}