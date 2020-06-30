package de.studiocode.miniatureblocks.commands.impl

import de.studiocode.miniatureblocks.commands.PlayerCommand
import de.studiocode.miniatureblocks.menu.inventory.impl.MiniaturesMenu
import de.studiocode.miniatureblocks.utils.openInventory
import org.bukkit.Material
import org.bukkit.entity.Player

class MiniaturesCommand : PlayerCommand() {
    
    override fun handleCommand(player: Player, message: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            player.openInventory(MiniaturesMenu())
            return true
        } else player.sendMessage("§cWrong syntax")
        return false
    }
    
}
