package de.studiocode.miniatureblocks.resourcepack

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.resourcepack.forced.ForcedResourcePack
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData.CustomModel
import de.studiocode.miniatureblocks.resourcepack.model.ModelData
import de.studiocode.miniatureblocks.utils.FileIOUploadUtils
import de.studiocode.miniatureblocks.utils.FileUtils
import net.lingala.zip4j.ZipFile
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.io.File

class ResourcePack : Listener {

    private val zipFile = File("plugins/MiniatureBlocks/ResourcePack.zip")
    private val dir = File("plugins/MiniatureBlocks/ResourcePack/")
    private val assetsDir = File(dir, "assets/")
    private val modelDir = File(assetsDir, "minecraft/models/")
    private val itemModelsDir = File(modelDir, "item/")
    private val moddedItemModelsDir = File(itemModelsDir, "modded/")

    private val packMcmeta = File(dir, "pack.mcmeta")
    private val modelParent = File(itemModelsDir, "miniatureblocksmain.json")

    private val mainModelDataFile = File(itemModelsDir, "bedrock.json")
    val mainModelData: MainModelData

    init {
        Bukkit.getPluginManager().registerEvents(this, MiniatureBlocks.INSTANCE)

        if (!dir.exists()) {
            dir.mkdirs()
            modelDir.mkdirs()
            itemModelsDir.mkdirs()
            moddedItemModelsDir.mkdirs()
            FileUtils.extractFile("/resourcepack/pack.mcmeta", packMcmeta)
            FileUtils.extractFile("/resourcepack/parent.json", modelParent)
        }

        mainModelData = MainModelData(mainModelDataFile)
    }

    fun upload(): String? {
        return FileIOUploadUtils.uploadFile(zipFile)
    }

    private fun createZip() {
        if (zipFile.exists()) zipFile.delete()
        val zip = ZipFile(zipFile)
        zip.addFolder(assetsDir)
        zip.addFile(packMcmeta)
    }

    private fun hasCustomModels(): Boolean {
        return moddedItemModelsDir.listFiles()?.isNotEmpty() ?: false
    }

    fun hasModel(name: String): Boolean {
        val modelFile = File(moddedItemModelsDir, "$name.json")
        return modelFile.exists()
    }

    fun hasModelData(customModelData: Int): Boolean {
        return mainModelData.hasCustomModelData(customModelData)
    }

    fun addNewModel(name: String, modelData: ModelData): Int {
        val modelFile = File(moddedItemModelsDir, "$name.json")
        modelData.writeToFile(modelFile)

        return addOverride("item/modded/$name")
    }

    fun removeModel(name: String) {
        val modelFile = File(moddedItemModelsDir, "$name.json")
        modelFile.delete()

        removeOverride(name)
    }

    private fun addOverride(model: String): Int {
        val customModelData = mainModelData.getNextCustomModelData()
        mainModelData.customModels.add(CustomModel(customModelData, model))
        mainModelData.writeToFile()

        createZip()
        forcePlayerResourcePack(*Bukkit.getOnlinePlayers().toTypedArray())
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
            Bukkit.getScheduler().runTaskLater(MiniatureBlocks.INSTANCE, Runnable { forcePlayerResourcePack(event.player) }, 5)
        }
    }

    private fun forcePlayerResourcePack(vararg players: Player) {
        players.forEach { ForcedResourcePack(it, this).force() }
    }

}