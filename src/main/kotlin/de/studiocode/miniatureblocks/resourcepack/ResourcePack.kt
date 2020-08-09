package de.studiocode.miniatureblocks.resourcepack

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.resourcepack.forced.ForcedResourcePack
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData.CustomModel
import de.studiocode.miniatureblocks.resourcepack.model.ModelData
import de.studiocode.miniatureblocks.utils.CustomUploaderUtils
import de.studiocode.miniatureblocks.utils.FileIOUploadUtils
import de.studiocode.miniatureblocks.utils.FileUtils
import de.studiocode.miniatureblocks.utils.HashUtils
import net.lingala.zip4j.ZipFile
import org.apache.commons.io.FilenameUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.io.File

class ResourcePack(private val plugin: MiniatureBlocks) : Listener {

    private val config = plugin.config

    private val zipFile = File("plugins/MiniatureBlocks/ResourcePack.zip")
    private val dir = File("plugins/MiniatureBlocks/ResourcePack/")
    private val assetsDir = File(dir, "assets/")
    private val modelDir = File(assetsDir, "minecraft/models/")
    private val itemModelsDir = File(modelDir, "item/")
    private val moddedItemModelsDir = File(itemModelsDir, "modded/")
    private val moddedBlockTexturesDir = File(assetsDir, "minecraft/textures/block/modded/")
    private val packMcmeta = File(dir, "pack.mcmeta")
    private val modelParent = File(itemModelsDir, "miniatureblocksmain.json")
    private val oldMainModelDataFile = File(itemModelsDir, "bedrock.json")
    private val mainModelDataFile = File(itemModelsDir, "structure_void.json")
    val mainModelData: MainModelData

    var downloadUrl: String? = config.getRPDownloadUrl()
    get() {
        return if (config.hasCustomUploader()) field
        else FileIOUploadUtils.uploadToFileIO(zipFile)
    }
    var hash: ByteArray = getZipHash()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)

        if (!moddedItemModelsDir.exists()) moddedItemModelsDir.mkdirs()
        if (!moddedBlockTexturesDir.exists()) moddedBlockTexturesDir.mkdirs()
        if (!packMcmeta.exists()) FileUtils.extractFile("/resourcepack/pack.mcmeta", packMcmeta)
        if (!modelParent.exists()) FileUtils.extractFile("/resourcepack/parent.json", modelParent)
        extractTextureFiles()
        
        if (oldMainModelDataFile.exists() && !mainModelDataFile.exists()) {
            // server upgraded to this version from version <= 0.5
            oldMainModelDataFile.copyTo(mainModelDataFile)
            createZip()
        }
        
        mainModelData = MainModelData(mainModelDataFile)
        
        if (config.hasCustomUploader() && hasCustomModels() && config.getRPDownloadUrl() == null) uploadToCustom()
    }
    
    private fun extractTextureFiles() {
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
            config.setRPDownloadUrl(url)
            downloadUrl = url
        }
    }

    private fun createZip() {
        if (zipFile.exists()) zipFile.delete()
        val zip = ZipFile(zipFile)
        zip.addFolder(assetsDir)
        zip.addFile(packMcmeta)

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

    fun addNewModel(name: String, modelData: ModelData, forceResourcePack: Boolean = true): Int {
        val modelFile = File(moddedItemModelsDir, "$name.json")
        modelData.writeToFile(modelFile)

        return addOverride("item/modded/$name", forceResourcePack)
    }

    fun removeModel(name: String) {
        val modelFile = File(moddedItemModelsDir, "$name.json")
        modelFile.delete()

        removeOverride(name)
    }

    private fun addOverride(model: String, forceResourcePack: Boolean = true): Int {
        val customModelData = mainModelData.getNextCustomModelData()
        mainModelData.customModels.add(CustomModel(customModelData, model))
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
        if (hasCustomModels()) {
            Bukkit.getScheduler().runTaskLater(plugin, Runnable { forcePlayerResourcePack(event.player) }, 5)
        }
    }

    private fun forcePlayerResourcePack(vararg players: Player) {
        players.forEach { ForcedResourcePack(it, this).force() }
    }

}