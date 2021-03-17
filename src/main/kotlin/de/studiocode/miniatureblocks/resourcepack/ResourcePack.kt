package de.studiocode.miniatureblocks.resourcepack

import com.google.gson.JsonObject
import de.studiocode.invui.resourcepack.ForceResourcePack
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.resourcepack.file.DirectoryFile
import de.studiocode.miniatureblocks.resourcepack.file.MainModelDataFile
import de.studiocode.miniatureblocks.resourcepack.file.TextureModelDataFile
import de.studiocode.miniatureblocks.resourcepack.forced.ForcedResourcePack
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.storage.PermanentStorage
import de.studiocode.miniatureblocks.util.*
import net.lingala.zip4j.ZipFile
import org.apache.commons.io.FilenameUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.io.File
import java.net.URL

class ResourcePack(plugin: MiniatureBlocks) : Listener {
    
    val main = File("plugins/MiniatureBlocks/ResourcePack/")
    val models = DirectoryFile(this, "assets/minecraft/models/")
    private val moddedItemModels = DirectoryFile(this, "assets/minecraft/models/item/modded")
    private val textureItemModels = DirectoryFile(this, "assets/minecraft/models/item/textureitem")
    private val textures = DirectoryFile(this, "assets/minecraft/textures/")
    
    val mainModelData: MainModelDataFile
    val textureModelData: TextureModelDataFile
    
    private val config = plugin.config
    
    private val zipFile = File(main.parentFile, "ResourcePack.zip")
    var hash = ByteArray(0)
    
    private val lastUploadUrl: String?
        get() = PermanentStorage.retrieveOrNull<String>("rp-download-url")
    
    val downloadUrl: String?
        get() {
            return if (config.hasCustomUploader()) {
                if (lastUploadUrl == null) uploadToCustom()
                lastUploadUrl
            } else uploadToDefault()
        }
    
    init {
        Bukkit.getServer().pluginManager.registerEvents(this, plugin)
        
        updateOldFiles()
        mainModelData = MainModelDataFile(this)
        textureModelData = TextureModelDataFile(this)
        
        extractDefaults()
        createTextureModelFiles()
        packZip()
    }
    
    private fun updateOldFiles() {
        val oldMainModelData = File(main, "assets/minecraft/models/item/structure_void.json")
        val oldTextureModelData = File(main, "assets/minecraft/models/item/dandelion.json")
        val newMainModelData = File(main, "assets/minecraft/models/item/black_stained_glass.json")
        val newTextureModelData = File(main, "assets/minecraft/models/item/white_stained_glass.json")
        
        if (oldMainModelData.exists() && !newMainModelData.exists()) {
            // server upgraded to this version from version >= 0.6 version <= 0.8
            oldMainModelData.copyTo(newMainModelData)
        }
        
        if (oldTextureModelData.exists() && !newTextureModelData.exists()) {
            // server upgraded to this version from version 0.9
            oldTextureModelData.copyTo(newTextureModelData)
        }
    }
    
    private fun extractDefaults() = FileUtils.extractFiles("resourcepack/", main)
    
    private fun createTextureModelFiles() {
        BlockTexture.textureLocations.forEach(this::addTextureLocation)
        textureModelData.writeToFile()
    }
    
    fun downloadTexture(name: String, url: String, frametime: Int) {
        val path = "block/$name"
        val texture = textures.getTexture("$path.png")
        URL(url).openStream().use { inStream ->
            texture.outputStream().use { outStream ->
                inStream.copyTo(outStream)
            }
        }
        if (frametime > 0) texture.makeAnimated(frametime)
        
        addTextureLocation(path)
        textureModelData.writeToFile()
        BlockTexture.addTextureLocation(path)
        
        updateResourcePack()
    }
    
    fun deleteTexture(name: String) {
        val path = "block/$name"
        val texture = textures.getTexture("$path.png")
        
        if (texture.isAnimated()) texture.animationData.delete()
        texture.delete()
        
        removeTextureLocation(path)
        BlockTexture.removeTextureLocation(path)
        
        updateResourcePack()
    }
    
    private fun addTextureLocation(textureLocation: String) {
        val modelName = textureLocation.replace("block/", "").replace("/", "")
        val modelPath = "item/textureitem/$modelName"
        val file = File(textureItemModels, "$modelName.json")
        if (!file.exists()) {
            val mainObj = JsonObject()
            mainObj.addProperty("parent", "item/textureitem")
            mainObj.add("textures", JsonObject().apply { addProperty("1", textureLocation) })
            file.writeText(mainObj.toString())
            
            textureModelData.customModels += textureModelData.CustomModel(textureModelData.getNextCustomModelData(), modelPath)
        }
    }
    
    private fun removeTextureLocation(textureLocation: String) {
        val modelName = textureLocation.replace("block/", "").replace("/", "")
        val modelPath = "item/textureitem/$modelName"
        val modelFile = File(textureItemModels, "$modelName.json")
        if (modelFile.exists()) {
            modelFile.delete()
            textureModelData.customModels.removeIf { it.model == modelPath }
            textureModelData.writeToFile()
        }
    }
    
    fun addMiniature(name: String, modelDataObj: JsonObject, force: Boolean = true) {
        modelDataObj.writeToFile(File(moddedItemModels, "$name.json"))
        
        val customModelData = mainModelData.getNextCustomModelData()
        mainModelData.customModels.add(mainModelData.CustomModel(customModelData, "item/modded/$name"))
        mainModelData.writeToFile()
        
        updateResourcePack(force)
    }
    
    fun removeMiniature(name: String) {
        File(moddedItemModels, "$name.json").delete()
        
        mainModelData.removeModel(name)
        mainModelData.writeToFile()
        
        updateResourcePack()
    }
    
    private fun updateResourcePack(force: Boolean = true) {
        packZip()
        if (force) forceResourcePack(*Bukkit.getOnlinePlayers().toTypedArray())
    }
    
    private fun packZip() {
        if (zipFile.exists()) zipFile.delete()
        
        val invUIRP = File("plugins/MiniatureBlocks/InvUIRP.zip")
        val mbRP = File("plugins/MiniatureBlocks/MiniatureBlocks.zip")
        
        // use InvUI ResourcePack as base
        invUIRP.downloadFrom(URL(ForceResourcePack.LIGHT_RESOURCE_PACK_URL))
        
        // create MiniatureBlocks ResourcePack
        val zip = ZipFile(mbRP)
        main.listFiles()!!.forEach { if (it.isDirectory) zip.addFolder(it) else zip.addFile(it) }
        
        // Merge both packs
        FileUtils.mergeZips(zipFile, invUIRP, mbRP)
        
        // delete temp files
        mbRP.delete()
        invUIRP.delete()
        
        updateZipHash() // update hash
        if (config.hasCustomUploader()) uploadToCustom() // upload if custom uploader is set
    }
    
    private fun updateZipHash() {
        hash = if (zipFile.exists()) {
            HashUtils.createSha1Hash(zipFile)
        } else ByteArray(0)
    }
    
    private fun uploadToCustom() {
        val reqUrl = config.getCustomUploaderRequest()
        val hostUrl = config.getCustomUploaderHost()
        val key = config.getCustomUploaderKey()
        
        if (lastUploadUrl != null) {
            // delete previous resource pack
            val fileName = FilenameUtils.getBaseName(downloadUrl)
            CustomUploaderUtils.deleteFile(reqUrl, key, fileName)
        }
        
        // upload new resource pack
        val url = CustomUploaderUtils.uploadFile(reqUrl, hostUrl, key, zipFile)!!
        PermanentStorage.store("rp-download-url", url)
    }
    
    private fun uploadToDefault() = FileIOUploadUtils.uploadToFileIO(zipFile)
    
    @EventHandler
    fun handlePlayerJoin(event: PlayerJoinEvent) {
        runTaskLater(5) { forceResourcePack(event.player) }
    }
    
    private fun forceResourcePack(vararg players: Player) {
        players.forEach { ForcedResourcePack(it, this).force() }
    }
    
}