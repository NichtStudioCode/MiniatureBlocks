package de.studiocode.miniatureblocks.commands.impl

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.commands.PlayerCommand
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class MiniatureWorldCommand : PlayerCommand() {

    override fun handleCommand(player: Player, message: String, args: Array<out String>): Boolean {
        val builderWorld = MiniatureBlocks.INSTANCE.builderWorld.world
        if (builderWorld == player.location.world) {
            val worlds = Bukkit.getWorlds().filter { it != builderWorld }
            if (worlds.isNotEmpty()) player.teleport(worlds[0].spawnLocation)
        } else player.teleport(builderWorld.spawnLocation)
        
        return true
    }

}