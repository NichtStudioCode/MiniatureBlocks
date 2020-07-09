package de.studiocode.miniatureblocks.commands.impl

import de.studiocode.miniatureblocks.commands.PlayerCommand
import de.studiocode.miniatureblocks.menu.inventory.impl.MiniaturesMenu
import de.studiocode.miniatureblocks.utils.openInventory
import org.bukkit.Material
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Orientable
import org.bukkit.entity.Player

class MiniaturesCommand : PlayerCommand() {
    
    override fun handleCommand(player: Player, message: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            player.openInventory(MiniaturesMenu())
            
            val location = player.location
            for (blockData in Material.values().filter { it.isBlock }.map { it.createBlockData() }) {
                if (blockData is Directional || blockData is Orientable) {
                    location.add(0.0, 0.0, 1.0)
                    location.block.type = blockData.material
                }
            }
            
            return true
        } else player.sendMessage("§cWrong syntax")
        return false
    }
    
}
