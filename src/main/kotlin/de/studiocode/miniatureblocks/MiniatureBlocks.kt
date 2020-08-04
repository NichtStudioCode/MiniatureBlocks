package de.studiocode.miniatureblocks

import de.studiocode.miniatureblocks.builderworld.BuilderWorld
import de.studiocode.miniatureblocks.command.CommandManager
import de.studiocode.miniatureblocks.config.Config
import de.studiocode.miniatureblocks.menu.inventory.MenuInventoryManager
import de.studiocode.miniatureblocks.miniature.MiniatureManager
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin

class MiniatureBlocks : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: MiniatureBlocks
    }

    lateinit var config: Config
    lateinit var builderWorld: BuilderWorld
    lateinit var resourcePack: ResourcePack
    lateinit var miniatureManager: MiniatureManager
    lateinit var commandManager: CommandManager

    override fun onEnable() {
        INSTANCE = this
        config = Config(this)
        builderWorld = BuilderWorld()
        resourcePack = ResourcePack(this)
        miniatureManager = MiniatureManager(this)
        commandManager = CommandManager(this)

        server.pluginManager.registerEvents(builderWorld, this)

        Metrics(this, 8307)
    }

    override fun onDisable() {
        MenuInventoryManager.INSTANCE.closeAllMenuInventories()
    }
}

