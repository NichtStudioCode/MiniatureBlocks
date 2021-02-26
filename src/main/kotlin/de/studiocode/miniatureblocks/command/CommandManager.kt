package de.studiocode.miniatureblocks.command

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.command.impl.MiniatureCommand
import de.studiocode.miniatureblocks.command.impl.MiniatureWorldCommand
import de.studiocode.miniatureblocks.command.impl.MiniaturesCommand
import de.studiocode.miniatureblocks.util.ReflectionUtils

class CommandManager(val plugin: MiniatureBlocks) {
    
    private val registeredCommands = ArrayList<String>()
    
    init {
        registerCommands()
        plugin.disableHandlers += this::unregisterCommands
    }
    
    private fun registerCommands() {
        registerCommand(MiniatureCommand::class.java, "miniatureBlocks.miniature", "miniature", "m")
        registerCommand(MiniaturesCommand::class.java, "miniatureBlocks.miniatures", "miniatures", "ms")
        registerCommand(MiniatureWorldCommand::class.java, "miniatureBlocks.miniatureWorld", "miniatureWorld", "mw")
    }
    
    private fun registerCommand(commandClass: Class<out PlayerCommand>, permission: String, vararg names: String) {
        for (name in names) {
            registeredCommands += name
            val command = commandClass.getConstructor(String::class.java, String::class.java)
                .newInstance(name, permission)
            
            ReflectionUtils.registerCommand(command.command)
        }
        ReflectionUtils.syncCommands()
    }
    
    private fun unregisterCommands() {
        registeredCommands.forEach { ReflectionUtils.unregisterCommand(it) }
    }
    
}