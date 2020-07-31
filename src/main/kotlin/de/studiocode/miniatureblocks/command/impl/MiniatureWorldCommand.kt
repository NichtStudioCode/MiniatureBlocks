package de.studiocode.miniatureblocks.command.impl

import com.mojang.brigadier.context.CommandContext
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.command.PlayerCommand
import org.bukkit.Bukkit

class MiniatureWorldCommand(name: String, permission: String) : PlayerCommand(name, permission) {

    init {
        command = command.executes { handleMiniatureWorldCommand(it); 0 }
    }

    private fun handleMiniatureWorldCommand(context: CommandContext<Any>) {
        val player = getPlayer(context.source)

        val builderWorld = MiniatureBlocks.INSTANCE.builderWorld.world
        if (builderWorld == player.location.world) {
            val worlds = Bukkit.getWorlds().filter { it != builderWorld }
            if (worlds.isNotEmpty()) player.teleport(worlds[0].spawnLocation)
        } else player.teleport(builderWorld.spawnLocation)
    }

}