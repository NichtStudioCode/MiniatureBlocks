package de.studiocode.miniatureblocks.command

import de.studiocode.miniatureblocks.command.impl.MiniatureCommand
import de.studiocode.miniatureblocks.command.impl.MiniatureWorldCommand
import de.studiocode.miniatureblocks.command.impl.MiniaturesCommand
import de.studiocode.miniatureblocks.util.ReflectionUtils
import org.bukkit.plugin.java.JavaPlugin

class CommandManager(val plugin: JavaPlugin) {
    
    init {
        registerCommands()
    }
    
    private fun registerCommands() {
        registerCommand(MiniatureCommand::class.java, "miniatureBlocks.miniature", "miniature", "m")
        registerCommand(MiniaturesCommand::class.java, "miniatureBlocks.miniatures", "miniatures", "ms")
        registerCommand(MiniatureWorldCommand::class.java, "miniatureBlocks.miniatureWorld", "miniatureWorld", "mw")
    }
    
    private fun registerCommand(commandClass: Class<out PlayerCommand>, permission: String, vararg names: String) {
        for (name in names) {
            val command = commandClass.getConstructor(String::class.java, String::class.java)
                .newInstance(name, permission)
            
            ReflectionUtils.registerCommand(command.command)
        }
    }
    
}