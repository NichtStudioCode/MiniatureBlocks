package de.studiocode.miniatureblocks

import de.studiocode.miniatureblocks.build.BuilderWorld
import de.studiocode.miniatureblocks.command.CommandManager
import de.studiocode.miniatureblocks.config.Config
import de.studiocode.miniatureblocks.menu.Menus
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureManager
import de.studiocode.miniatureblocks.region.RegionManager
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bstats.bukkit.Metrics
import org.bstats.charts.SingleLineChart
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class MiniatureBlocks : JavaPlugin() {
    
    companion object {
        lateinit var INSTANCE: MiniatureBlocks
    }
    
    val disableHandlers = ArrayList<() -> Unit>()
    
    lateinit var pluginFile: File
    lateinit var config: Config
    lateinit var builderWorld: BuilderWorld
    lateinit var resourcePack: ResourcePack
    lateinit var miniatureManager: MiniatureManager
    lateinit var regionManager: RegionManager
    
    override fun onEnable() {
        INSTANCE = this
        pluginFile = file
        config = Config(this)
        builderWorld = BuilderWorld()
        resourcePack = ResourcePack(this)
        miniatureManager = MiniatureManager(this)
        regionManager = RegionManager(this)
        CommandManager(this)
        
        server.pluginManager.registerEvents(builderWorld, this)
        
        Menus.registerGlobalIngredients()
        
        setupMetrics()
    }
    
    override fun onDisable() {
        disableHandlers.forEach { it() }
    }
    
    private fun setupMetrics() {
        val metrics = Metrics(this, 8307)
        
        metrics.addCustomChart(SingleLineChart("miniatureAmount") {
            resourcePack.mainModelData.customModels.size
        })
        
        metrics.addCustomChart(SingleLineChart("textureOverrides") {
            BlockTexture.textureOverrides.size
        })
    }
    
}

