package de.studiocode.miniatureblocks.commands.impl

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.commands.PlayerCommand
import org.bukkit.entity.Player

class MiniatureWorldCommand : PlayerCommand() {
    
    override fun handleCommand(player: Player, message: String, args: Array<out String>): Boolean {
        val builderWorld = MiniatureBlocks.INSTANCE.builderWorld
        player.teleport(builderWorld.spawnLocation)
        
        return true
    }

}