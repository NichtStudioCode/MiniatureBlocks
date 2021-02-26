package de.studiocode.miniatureblocks

import de.studiocode.invui.InvUI
import de.studiocode.miniatureblocks.build.BuilderWorld
import de.studiocode.miniatureblocks.command.CommandManager
import de.studiocode.miniatureblocks.config.Config
import de.studiocode.miniatureblocks.menu.Menus
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStandManager
import de.studiocode.miniatureblocks.region.RegionManager
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import org.bstats.bukkit.Metrics
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class MiniatureBlocks : JavaPlugin(), Listener {
    
    companion object {
        lateinit var INSTANCE: MiniatureBlocks
    }
    
    val disableHandlers = ArrayList<() -> Unit>()
    
    lateinit var pluginFile: File
    lateinit var config: Config
    lateinit var builderWorld: BuilderWorld
    lateinit var resourcePack: ResourcePack
    lateinit var miniatureManager: MiniatureArmorStandManager
    lateinit var regionManager: RegionManager
    
    override fun onEnable() {
        INSTANCE = this
        pluginFile = file
        config = Config(this)
        builderWorld = BuilderWorld()
        resourcePack = ResourcePack(this)
        miniatureManager = MiniatureArmorStandManager(this)
        regionManager = RegionManager(this)
        CommandManager(this)
        
        server.pluginManager.registerEvents(builderWorld, this)
        
        InvUI.getInstance().plugin = this
        Menus.registerGlobalIngredients()
        
        Metrics(this, 8307)
    }
    
    override fun onDisable() {
        disableHandlers.forEach { it() }
    }
    
}

