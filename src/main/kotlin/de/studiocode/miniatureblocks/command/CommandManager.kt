package de.studiocode.miniatureblocks.command

import de.studiocode.miniatureblocks.command.impl.MiniatureCommand
import de.studiocode.miniatureblocks.command.impl.MiniatureWorldCommand
import de.studiocode.miniatureblocks.command.impl.MiniaturesCommand
import de.studiocode.miniatureblocks.utils.ReflectionUtils
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.ConcurrentHashMap

class CommandManager(val plugin: JavaPlugin) {

    val permissionUpdateEntities = ConcurrentHashMap.newKeySet<Any>()!! // provides a concurrent HashSet
    
    init {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::handleTick, 1, 1)
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

    private fun handleTick() {
        for (entity in permissionUpdateEntities) {
            ReflectionUtils.updatePermissionLevel(entity)
            permissionUpdateEntities.remove(entity)
        }
    }
    
}