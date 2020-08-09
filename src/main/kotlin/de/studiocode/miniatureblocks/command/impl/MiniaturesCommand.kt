package de.studiocode.miniatureblocks.command.impl

import com.mojang.brigadier.context.CommandContext
import de.studiocode.miniatureblocks.command.PlayerCommand
import de.studiocode.miniatureblocks.menu.inventory.impl.MiniaturesMenu
import de.studiocode.miniatureblocks.utils.openInventory

class MiniaturesCommand(name: String, permission: String) : PlayerCommand(name, permission) {

    init {
        command = command.executes { handleMiniaturesCommand(it); 0 }
    }
    
    private fun handleMiniaturesCommand(context: CommandContext<Any>) {
        try {
            val player = getPlayer(context.source)
            player.openInventory(MiniaturesMenu())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

