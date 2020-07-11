package de.studiocode.miniatureblocks.commands.impl

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.commands.PlayerCommand
import org.bukkit.entity.Player

class AutoRotateCommand : PlayerCommand() {
    
    override fun handleCommand(player: Player, message: String, args: Array<out String>): Boolean {
        if (args.size == 1) {
            val rotation = args[0].toFloatOrNull()
            if (rotation != null) {
                MiniatureBlocks.INSTANCE.miniatureManager.playerRotationMap[player] = rotation
                player.sendMessage("§7Right-click the miniature you want to rotate.")
                return true
            }
        }
        return false
    }
    
}