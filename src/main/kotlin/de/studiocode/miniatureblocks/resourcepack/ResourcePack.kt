package de.studiocode.miniatureblocks.resourcepack

import com.google.gson.JsonObject
import de.studiocode.invui.resourcepack.ForceResourcePack
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.resourcepack.forced.ForcedResourcePack
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData
import de.studiocode.miniatureblocks.resourcepack.model.ModelData.CustomModel
import de.studiocode.miniatureblocks.resourcepack.model.TextureItemModelData
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

// TODO: rewrite
class ResourcePack(plugin: MiniatureBlocks) : Listener {
    
    private val config = plugin.config
    
    private val zipFile = File("plugins/MiniatureBlocks/ResourcePack.zip")
    private val dir = File("plugins/MiniatureBlocks/ResourcePack/")
    private val assetsDir = File(dir, "assets/")
    private val minecraftDir = File(assetsDir, "minecraft/")
    val modelsDir = File(minecraftDir, "models/")
    private val itemModelsDir = File(modelsDir, "item/")
    private val moddedItemModelsDir = File(itemModelsDir, "modded/")
    private val texturesDir = File(minecraftDir, "textures/")
    private val moddedBlockTexturesDir = File(texturesDir, "block/modded/")
    private val textureItemsDir = File(itemModelsDir, "textureitem")
    private val packMcmeta = File(dir, "pack.mcmeta")
    private val modelParent = File(itemModelsDir, "miniatureblocksmain.json")
    private val textureModelParent = File(itemModelsDir, "textureitem.json")
    private val oldMainModelDataFile = File(itemModelsDir, "structure_void.json")
    private val mainModelDataFile = File(itemModelsDir, "black_stained_glass.json")
    private val textureModelDataFile = File(itemModelsDir, "dandelion.json")
    val textureModelData = TextureItemModelData(textureModelDataFile)
    val mainModelData: MainModelData
    
    var downloadUrl: String? = PermanentStorage.retrieveOrNull("rp-download-url")
        get() {
            return if (config.hasCustomUploader()) field
            else FileIOUploadUtils.uploadToFileIO(zipFile)
        }
    var hash: ByteArray = getZipHash()
    
    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        
        if (!moddedItemModelsDir.exists()) moddedItemModelsDir.mkdirs()
        if (!moddedBlockTexturesDir.exists()) moddedBlockTexturesDir.mkdirs()
        if (!textureItemsDir.exists()) textureItemsDir.mkdirs()
        if (!packMcmeta.exists()) FileUtils.extractFile("/resourcepack/pack.mcmeta", packMcmeta)
        FileUtils.extractFile("/resourcepack/parent.json", modelParent)
        FileUtils.extractFile("/resourcepack/textureitem.json", textureModelParent)
        extractTextureFiles()
        createTextureModelFiles()
        
        if (oldMainModelDataFile.exists() && !mainModelDataFile.exists()) {
            // server upgraded to this version from version >= 0.6 version <= 0.8
            oldMainModelDataFile.copyTo(mainModelDataFile)
        }
        createZip()
        
        mainModelData = MainModelData(mainModelDataFile)
        
        if (config.hasCustomUploader() && hasCustomModels() && PermanentStorage.retrieveOrNull<String>("rp-download-url") == "") uploadToCustom()
        
    }
    
    private fun createTextureModelFiles() {
        BlockTexture.textureLocations.forEach(this::addTextureLocation)
        textureModelData.writeToFile()
    }
    
    fun downloadTexture(name: String, url: String) {
        val path = "block/$name"
        val file = File(texturesDir, "$path.png")
        URL(url).openStream().use { inStream ->
            file.outputStream().use { outStream ->
                inStream.copyTo(outStream)
            }
        }
        
        addTextureLocation(path)
        BlockTexture.addTextureLocation(path)
        textureModelData.writeToFile()
        createZip()
        forcePlayerResourcePack(*Bukkit.getOnlinePlayers().toTypedArray())
    }
    
    fun deleteTexture(name: String) {
        val texturePath = "block/$name"
        val image = File(texturesDir, "block/$name.png")
        if (image.exists()) image.delete()
        
        val modelPath = "item/textureitem/$name"
        val modelFile = File(modelsDir, "$modelPath.json")
        if (modelFile.exists()) {
            modelFile.delete()
            textureModelData.customModels.removeIf { it.name == name }
            textureModelData.writeToFile()
            BlockTexture.removeTextureLocation(texturePath)
            
            createZip()
            forcePlayerResourcePack(*Bukkit.getOnlinePlayers().toTypedArray())
        }
    }
    
    private fun addTextureLocation(textureLocation: String) {
        val modelName = "item/textureitem/" + (textureLocation.replace("block/", "").replace("/", ""))
        val file = File(modelsDir, "$modelName.json")
        if (!file.exists()) {
            val mainObj = JsonObject()
            mainObj.addProperty("parent", "item/textureitem")
            mainObj.add("textures", JsonObject().apply { addProperty("1", textureLocation) })
            file.writeText(mainObj.toString())
            
            textureModelData.customModels += textureModelData.CustomModel(textureModelData.getNextCustomModelData(), modelName)
        }
    }
    
    private fun extractTextureFiles() {
        // extract MiniatureBlocks stuff
        for (fileName in FileUtils.listExtractableFiles("resourcepack/textures/")) {
            val file = File(moddedBlockTexturesDir, FilenameUtils.getName(fileName))
            if (!file.exists()) FileUtils.extractFile("/$fileName", file)
        }
    }
    
    private fun uploadToCustom() {
        val reqUrl = config.getCustomUploaderRequest()
        val hostUrl = config.getCustomUploaderHost()
        val key = config.getCustomUploaderKey()
        
        if (downloadUrl != null) {
            // delete previous resource pack
            val fileName = FilenameUtils.getBaseName(downloadUrl)
            CustomUploaderUtils.deleteFile(reqUrl, key, fileName)
        }
        
        // upload file
        val url = CustomUploaderUtils.uploadFile(reqUrl, hostUrl, key, zipFile)
        if (url != null) {
            PermanentStorage.store("rp-download-url", url)
            downloadUrl = url
        }
    }
    
    private fun createZip() {
        if (zipFile.exists()) zipFile.delete()
        
        val invUIRP = File("plugins/MiniatureBlocks/InvUIRP.zip")
        val mbRP = File("plugins/MiniatureBlocks/MiniatureBlocks.zip")
        
        // use InvUI ResourcePack as base
        invUIRP.downloadFrom(URL(ForceResourcePack.LIGHT_RESOURCE_PACK_URL))
        
        // create MiniatureBlocks ResourcePack
        val zip = ZipFile(mbRP)
        zip.addFile(packMcmeta)
        zip.addFolder(assetsDir)
        
        // Merge both packs
        FileUtils.mergeZips(zipFile, invUIRP, mbRP)
        
        // delete temp files
        mbRP.delete()
        invUIRP.delete()
        
        hash = getZipHash() // update hash
        if (config.hasCustomUploader()) uploadToCustom() // upload if custom uploader is set
    }
    
    private fun getZipHash(): ByteArray {
        return if (zipFile.exists()) {
            HashUtils.createSha1Hash(zipFile)
        } else ByteArray(0)
    }
    
    private fun hasCustomModels(): Boolean {
        return moddedItemModelsDir.listFiles()?.isNotEmpty() ?: false
    }
    
    fun hasModel(name: String): Boolean {
        val modelFile = File(moddedItemModelsDir, "$name.json")
        return modelFile.exists()
    }
    
    fun addNewModel(name: String, modelDataObj: JsonObject, forceResourcePack: Boolean = true): Int {
        val modelFile = File(moddedItemModelsDir, "$name.json")
        modelDataObj.writeToFile(modelFile)
        
        return addOverride("item/modded/$name", forceResourcePack)
    }
    
    fun removeModel(name: String) {
        val modelFile = File(moddedItemModelsDir, "$name.json")
        modelFile.delete()
        
        removeOverride(name)
    }
    
    private fun addOverride(model: String, forceResourcePack: Boolean = true): Int {
        val customModelData = mainModelData.getNextCustomModelData()
        mainModelData.customModels.add(mainModelData.CustomModel(customModelData, model))
        mainModelData.writeToFile()
        
        createZip()
        if (forceResourcePack) forcePlayerResourcePack(*Bukkit.getOnlinePlayers().toTypedArray())
        return customModelData
    }
    
    private fun removeOverride(name: String) {
        mainModelData.removeModel(name)
        mainModelData.writeToFile()
        
        createZip()
        forcePlayerResourcePack(*Bukkit.getOnlinePlayers().toTypedArray())
    }
    
    fun getModels(): ArrayList<CustomModel> {
        return mainModelData.customModels
    }
    
    @EventHandler
    fun handlePlayerJoin(event: PlayerJoinEvent) {
        runTaskLater(5) { forcePlayerResourcePack(event.player) }
    }
    
    private fun forcePlayerResourcePack(vararg players: Player) {
        players.forEach { ForcedResourcePack(it, this).force() }
    }
    
}