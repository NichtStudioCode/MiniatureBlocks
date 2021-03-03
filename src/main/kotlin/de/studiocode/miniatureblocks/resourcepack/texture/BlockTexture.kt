package de.studiocode.miniatureblocks.resourcepack.texture

import com.google.common.base.Preconditions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.Direction.NORTH
import de.studiocode.miniatureblocks.storage.PermanentStorage
import de.studiocode.miniatureblocks.storage.serialization.BlockTextureDeserializer
import de.studiocode.miniatureblocks.util.fromJson
import org.bukkit.Material
import java.util.*
import kotlin.collections.HashSet

class BlockTexture(materialName: String, val textures: Array<String>, val defaultRotation: Direction = NORTH) {
    
    val material = findMaterialByName(materialName)
    
    constructor(material: String, texture: String) : this(material, Array<String>(6) { texture })
    
    constructor(material: String) : this(material, Array<String>(6) { "block/${material.toLowerCase()}" })
    
    fun getTexture(direction: Direction): String {
        Preconditions.checkState(textures.size == 6,
            "This block does not have 6 textures, indicating that direction shouldn't be used to retrieve them.")
        return textures[direction.ordinal]
    }
    
    fun copyOfChange(affectedIndices: List<Int>, textureName: String): BlockTexture {
        val texturesCopy = textures.copyOf()
        affectedIndices.forEach { index -> texturesCopy[index] = textureName }
        return BlockTexture(material?.name ?: "", texturesCopy, defaultRotation)
    }
    
    override fun equals(other: Any?): Boolean {
        return if (other is BlockTexture) {
            material == other.material
                && defaultRotation == other.defaultRotation
                && textures.contentEquals(other.textures)
        } else this === other
    }
    
    override fun hashCode(): Int {
        var result = 3
        result = 31 * result + (material?.hashCode() ?: 0)
        result = 31 * result + defaultRotation.hashCode()
        result = 31 * result + textures.contentHashCode()
        return result
    }
    
    private fun findMaterialByName(name: String): Material? {
        return Material.values().find { it.name == name }
    }
    
    companion object {
        
        private val blockTextures: EnumMap<Material, BlockTexture> = EnumMap(Material::class.java)
        
        @Suppress("ReplaceWithEnumMap")
        val textureOverrides = PermanentStorage.retrieve(HashMap<Material, BlockTexture>(), "textureOverrides")
        
        private val defaultTextureLocations: HashSet<String>
        private val customTextureLocations: ArrayList<String>
        val textureLocations: HashSet<String>
        val supportedMaterials: TreeSet<Material>
        
        init {
            loadBlockTextures()
                .filter { it.material != null }
                .forEach { blockTextures[it.material!!] = it }
            
            supportedMaterials = TreeSet(blockTextures.keys)
            
            defaultTextureLocations = blockTextures.values
                .flatMap { it.textures.toList() }
                .toHashSet()
            
            customTextureLocations = PermanentStorage.retrieve(ArrayList(), "customTextures")
            textureLocations = HashSet(defaultTextureLocations)
            textureLocations.addAll(customTextureLocations)
        }
        
        fun addTextureLocation(location: String) {
            textureLocations.add(location)
            customTextureLocations.add(location)
            
            PermanentStorage.store("customTextures", customTextureLocations)
        }
        
        fun removeTextureLocation(location: String): Boolean {
            if (!defaultTextureLocations.contains(location) && customTextureLocations.remove(location)) {
                PermanentStorage.store("customTextures", customTextureLocations)
                textureLocations.remove(location)
                return true
            }
            return false
        }
        
        fun has(material: Material) = blockTextures.containsKey(material)
        
        fun of(material: Material): BlockTexture {
            return textureOverrides[material] ?: blockTextures[material]!!
        }
        
        fun overrideTextureLocations(material: Material, run: (BlockTexture) -> BlockTexture) {
            val defaultTexture = blockTextures[material]!!
            val overrideTexture = textureOverrides[material]
            val currentTexture = overrideTexture ?: defaultTexture
            
            val newTexture = run(currentTexture)
            if (newTexture == defaultTexture) {
                textureOverrides.remove(material)
            } else textureOverrides[material] = newTexture
            
            PermanentStorage.store("textureOverrides", textureOverrides)
        }
        
        fun removeTextureLocationOverride(material: Material) {
            textureOverrides.remove(material)
            PermanentStorage.store("textureOverrides", textureOverrides)
        }
        
        fun hasOverrides(material: Material) = textureOverrides.containsKey(material)
        
        private fun loadBlockTextures(): HashSet<BlockTexture> {
            val textures = HashSet<BlockTexture>()
            val obj = JsonParser().parse(BlockTexture::class.java.getResource("/textures.json").readText()).asJsonObject
            
            val gson: Gson = GsonBuilder()
                .registerTypeAdapter(BlockTexture::class.java, BlockTextureDeserializer)
                .create()
            
            textures.addAll((obj.get("default").asJsonArray
                + obj.get("delegate").asJsonArray
                + obj.get("custom").asJsonArray)
                .map { gson.fromJson(it)!! })
            
            return textures
        }
        
    }
    
}

