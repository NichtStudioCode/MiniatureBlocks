package de.studiocode.miniatureblocks.command

import de.studiocode.miniatureblocks.command.impl.MiniatureCommand
import de.studiocode.miniatureblocks.command.impl.MiniatureWorldCommand
import de.studiocode.miniatureblocks.command.impl.MiniaturesCommand
import de.studiocode.miniatureblocks.utils.ReflectionUtils
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class CommandManager(val plugin : JavaPlugin) : Listener {
    
    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        registerCommands()
    }
    
    private fun registerCommands() {
        registerCommand(MiniatureCommand("miniature", "miniatureBlocks.miniature"))
        registerCommand(MiniaturesCommand("miniatures", "miniatureBlocks.miniatures"))
        registerCommand(MiniatureWorldCommand("miniatureWorld", "miniatureBlocks.miniatureWorld"))
    }
    
    private fun registerCommand(command: PlayerCommand) {
        ReflectionUtils.registerCommand(command.command)
    }
    
    @EventHandler
    fun handlePlayerJoin(event: PlayerJoinEvent) {
        // updating permission level so the require methods will be called again
        ReflectionUtils.updatePermissionLevelPlayer(event.player)
    }
    
}