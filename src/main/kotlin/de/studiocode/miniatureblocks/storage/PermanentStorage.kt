package de.studiocode.miniatureblocks.storage

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.storage.serialization.*
import de.studiocode.miniatureblocks.util.fromJson
import org.bukkit.Location
import java.io.File
import java.util.*

object PermanentStorage {
    
    val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(UUID::class.java, UUIDTypeAdapter)
        .registerTypeAdapter(Location::class.java, LocationSerializer)
        .registerTypeAdapter(Location::class.java, LocationDeserializer)
        .registerTypeAdapter(BlockTexture::class.java, BlockTextureSerializer)
        .registerTypeAdapter(BlockTexture::class.java, BlockTextureDeserializer)
        .create()
    
    private val file = File("plugins/MiniatureBlocks/storage.json").apply { parentFile.mkdirs() }
    val mainObj: JsonObject = if (file.exists()) JsonParser().parse(file.readText()).asJsonObject else JsonObject()
    
    fun store(key: String, data: Any) {
        mainObj.add(key, gson.toJsonTree(data))
        file.writeText(gson.toJson(mainObj))
    }
    
    inline fun <reified T> retrieve(alternative: T, key: String): T {
        return retrieveOrNull(key) ?: alternative
    }
    
    inline fun <reified T> retrieveOrNull(key: String): T? {
        return gson.fromJson<T>(mainObj.get(key))
    }
    
}