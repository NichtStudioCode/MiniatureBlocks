package de.studiocode.miniatureblocks.resourcepack

import com.google.gson.JsonObject
import de.studiocode.invui.resourcepack.ForceResourcePack
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.build.BuildDataCreator
import de.studiocode.miniatureblocks.build.concurrent.SyncTaskExecutor
import de.studiocode.miniatureblocks.resourcepack.file.DirectoryFile
import de.studiocode.miniatureblocks.resourcepack.file.MainModelDataFile
import de.studiocode.miniatureblocks.resourcepack.file.MaterialModelDataFile
import de.studiocode.miniatureblocks.resourcepack.file.TextureModelDataFile
import de.studiocode.miniatureblocks.resourcepack.forced.ForcedResourcePack
import de.studiocode.miniatureblocks.resourcepack.model.MiniatureModel
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.storage.PermanentStorage
import de.studiocode.miniatureblocks.util.*
import net.lingala.zip4j.ZipFile
import org.apache.commons.io.FilenameUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import java.io.File
import java.net.URL

class ResourcePack(plugin: MiniatureBlocks) : Listener {
    
    val main = File("plugins/MiniatureBlocks/ResourcePack/")
    val models = DirectoryFile(this, "assets/minecraft/models/")
    val moddedItemModels = DirectoryFile(this, "assets/minecraft/models/item/modded")
    private val textureItemModels = DirectoryFile(this, "assets/minecraft/models/item/textureitem")
    private val materialItemModels = DirectoryFile(this, "assets/minecraft/models/item/materialitem")
    private val textures = DirectoryFile(this, "assets/minecraft/textures/")
    
    val mainModelData: MainModelDataFile
    val textureModelData: TextureModelDataFile
    val materialModelData = MaterialModelDataFile(this)
    
    private val config = plugin.config
    
    private val zipFile = File(main.parentFile, "ResourcePack.zip")
    var hash = ByteArray(0)
    
    private val lastUploadUrl: String?
        @Synchronized get() = PermanentStorage.retrieveOrNull<String>("rp-download-url")
    
    val downloadUrl: String?
        @Synchronized get() {
            return if (config.hasCustomUploader()) {
                if (lastUploadUrl == null) uploadToCustom()
                lastUploadUrl
            } else uploadToDefault()
        }
    
    @Volatile
    private var initialized = false
    
    init {
        Bukkit.getServer().pluginManager.registerEvents(this, plugin)
        
        updateOldFiles()
        mainModelData = MainModelDataFile(this)
        textureModelData = TextureModelDataFile(this)
        
        extractDefaults()
        createTextureModelFiles()
        
        runAsyncTask {
            createMaterialModelFiles()
            packZip()
            
            initialized = true
            println("[MiniatureBlocks] ResourcePack initialized")
        }
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
    
    @Synchronized
    private fun extractDefaults() = FileUtils.extractFiles("resourcepack/", main)
    
    @Synchronized
    private fun createTextureModelFiles() {
        BlockTexture.textureLocations.forEach(this::addTextureLocation)
        textureModelData.writeToFile()
    }
    
    @Synchronized
    private fun createMaterialModelFiles() {
        val executor = SyncTaskExecutor()
        val block = MiniatureBlocks.INSTANCE.builderWorld.world.getBlockAt(0, 200, 0)
        
        BlockTexture.supportedMaterials
            .filterNot { it.isItem }
            .forEach { material ->
                val file = File(materialItemModels, "${material.name.lowercase()}.json")
                if (!file.exists()) {
                    executor.submit {
                        val blockState = block.state
                        blockState.type = material
                        blockState.update(true, false)
                    }
                    executor.awaitCompletion()
                    
                    val data = BuildDataCreator(block.location, block.location).createData()
                    val modelDataObj = MiniatureModel(data).modelDataObj
                    modelDataObj.writeToFile(file)
                    
                    val customModelData = materialModelData.getNextCustomModelData()
                    materialModelData.customModels += materialModelData.CustomModel(customModelData, "item/materialitem/${material.name.lowercase()}")
                }
            }
        
        materialModelData.writeToFile()
        
        executor.submit { block.type = Material.AIR }
    }
    
    @Synchronized
    fun downloadTexture(
        name: String,
        url: String,
        frametime: Int,
        force: Boolean = true,
        addTextureLocation: Boolean = true,
        replace: Boolean = true
    ): String {
        
        val path = "block/$name"
        val texture = textures.getTexture("$path.png")
        if (replace || !texture.exists()) {
            URL(url).openStream().use { inStream ->
                texture.outputStream().use { outStream ->
                    inStream.copyTo(outStream)
                }
            }
            if (frametime > 0) texture.makeAnimated(frametime)
            
            if (addTextureLocation) {
                addTextureLocation(path)
                textureModelData.writeToFile()
                BlockTexture.addTextureLocation(path)
            }
            
            updateResourcePack(force)
        }
        
        return path
    }
    
    @Synchronized
    fun deleteTexture(name: String) {
        val path = "block/$name"
        val texture = textures.getTexture("$path.png")
        
        if (texture.isAnimated()) texture.animationData.delete()
        texture.delete()
        
        removeTextureLocation(path)
        BlockTexture.removeTextureLocation(path)
        
        updateResourcePack()
    }
    
    @Synchronized
    private fun addTextureLocation(textureLocation: String) {
        val modelPath = "item/textureitem/$textureLocation"
        val file = File(textureItemModels, "$textureLocation.json")
        if (!file.exists()) {
            file.parentFile.mkdirs()
            
            val mainObj = JsonObject()
            mainObj.addProperty("parent", "item/textureitem")
            mainObj.add("textures", JsonObject().apply { addProperty("1", textureLocation) })
            file.writeText(mainObj.toString())
            
            textureModelData.customModels += textureModelData.CustomModel(textureModelData.getNextCustomModelData(), modelPath)
        }
    }
    
    @Synchronized
    private fun removeTextureLocation(textureLocation: String) {
        val modelPath = "item/textureitem/$textureLocation"
        val modelFile = File(textureItemModels, "$textureLocation.json")
        if (modelFile.exists()) {
            modelFile.delete()
            textureModelData.customModels.removeIf { it.path == modelPath }
            textureModelData.writeToFile()
        }
    }
    
    
    @Synchronized
    fun addMiniature(name: String, modelDataObj: JsonObject, force: Boolean = true) {
        modelDataObj.writeToFile(File(moddedItemModels, "$name.json"))
        
        val customModelData = mainModelData.getNextCustomModelData()
        mainModelData.customModels.add(mainModelData.CustomModel(customModelData, "item/modded/$name"))
        mainModelData.writeToFile()
        
        updateResourcePack(force)
    }
    
    @Synchronized
    fun removeMiniature(name: String, updateResourcePack: Boolean) {
        File(moddedItemModels, "$name.json").delete()
        
        mainModelData.removeModel(name)
        mainModelData.writeToFile()
        
        if (updateResourcePack) updateResourcePack()
    }
    
    @Synchronized
    private fun updateResourcePack(force: Boolean = true) {
        packZip()
        if (force) forceResourcePack(*Bukkit.getOnlinePlayers().toTypedArray())
    }
    
    @Synchronized
    private fun packZip() {
        if (zipFile.exists()) zipFile.delete()
        
        val invUIRP = File("plugins/MiniatureBlocks/InvUIRP.zip")
        val mbRP = File("plugins/MiniatureBlocks/MiniatureBlocks.zip")
        
        // use InvUI ResourcePack as base
        invUIRP.downloadFrom(URL(ForceResourcePack.RESOURCE_PACK_URL))
        
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
    
    @Synchronized
    private fun updateZipHash() {
        hash = if (zipFile.exists()) {
            HashUtils.createSha1Hash(zipFile)
        } else ByteArray(0)
    }
    
    @Synchronized
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
    
    @Synchronized
    private fun uploadToDefault() = FileIOUploadUtils.uploadToFileIO(zipFile)
    
    @EventHandler
    fun handlePlayerLogin(event: PlayerLoginEvent) {
        if (!initialized) event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "")
    }
    
    @EventHandler
    fun handlePlayerJoin(event: PlayerJoinEvent) {
        runTaskLater(5) { forceResourcePack(event.player) }
    }
    
    private fun forceResourcePack(vararg players: Player) {
        players.forEach { ForcedResourcePack(it, this).force() }
    }
    
}