package de.studiocode.miniatureblocks

import de.studiocode.miniatureblocks.builderworld.BuilderWorld
import de.studiocode.miniatureblocks.command.CommandManager
import de.studiocode.miniatureblocks.config.Config
import de.studiocode.miniatureblocks.menu.inventory.MenuInventoryManager
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStandManager
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class MiniatureBlocks : JavaPlugin() {
    
    companion object {
        lateinit var INSTANCE: MiniatureBlocks
    }
    
    lateinit var pluginFile: File
    lateinit var config: Config
    lateinit var builderWorld: BuilderWorld
    lateinit var resourcePack: ResourcePack
    lateinit var miniatureManager: MiniatureArmorStandManager
    
    override fun onEnable() {
        INSTANCE = this
        pluginFile = file
        config = Config(this)
        builderWorld = BuilderWorld()
        resourcePack = ResourcePack(this)
        miniatureManager = MiniatureArmorStandManager(this)
        CommandManager(this)
        
        server.pluginManager.registerEvents(builderWorld, this)
        
        Metrics(this, 8307)
    }
    
    override fun onDisable() {
        MenuInventoryManager.INSTANCE.closeAllMenuInventories()
    }
}

