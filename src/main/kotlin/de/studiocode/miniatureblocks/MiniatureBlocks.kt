package de.studiocode.miniatureblocks

import de.studiocode.miniatureblocks.builderworld.BuilderWorld
import de.studiocode.miniatureblocks.commands.impl.AutoRotateCommand
import de.studiocode.miniatureblocks.commands.impl.CreateMiniatureCommand
import de.studiocode.miniatureblocks.commands.impl.MiniatureWorldCommand
import de.studiocode.miniatureblocks.commands.impl.MiniaturesCommand
import de.studiocode.miniatureblocks.menu.inventory.MenuInventoryManager
import de.studiocode.miniatureblocks.miniature.MiniatureManager
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import org.bukkit.plugin.java.JavaPlugin

class MiniatureBlocks : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: MiniatureBlocks
    }

    lateinit var builderWorld: BuilderWorld
    lateinit var resourcePack: ResourcePack
    lateinit var miniatureManager: MiniatureManager

    override fun onEnable() {
        INSTANCE = this
        builderWorld = BuilderWorld()
        resourcePack = ResourcePack()
        miniatureManager = MiniatureManager()

        getCommand("miniatureworld")!!.setExecutor(MiniatureWorldCommand())
        getCommand("createminiature")!!.setExecutor(CreateMiniatureCommand())
        getCommand("miniatures")!!.setExecutor(MiniaturesCommand())
        getCommand("autorotate")!!.setExecutor(AutoRotateCommand())
        server.pluginManager.registerEvents(builderWorld, this)
    }

    override fun onDisable() {
        MenuInventoryManager.INSTANCE.closeAllMenuInventories()
    }
}

