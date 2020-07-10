package de.studiocode.miniatureblocks.commands.impl

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.commands.PlayerCommand
import de.studiocode.miniatureblocks.resourcepack.model.BuildDataModelParser
import de.studiocode.miniatureblocks.utils.containsOnly
import org.bukkit.entity.Player

class CreateMiniatureCommand : PlayerCommand() {

    companion object {
        const val ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz"
    }

    override fun handleCommand(player: Player, message: String, args: Array<out String>): Boolean {
        if (args.size == 1) {
            val name = args[0].toLowerCase()
            if (name.containsOnly(ALLOWED_CHARACTERS)) {
                val resourcePack = MiniatureBlocks.INSTANCE.resourcePack
                if (!resourcePack.hasModel(name)) {
                    val builderWorld = MiniatureBlocks.INSTANCE.builderWorld
                    if (builderWorld.isPlayerInValidBuildArea(player)) {
                        val buildData = builderWorld.getBuildData(player)
                        val parser = BuildDataModelParser(buildData)
                        val modelData = parser.parse()
                        resourcePack.addNewModel(name, modelData)

                        player.sendMessage("§7A new model has been created.")
                        return true
                    } else player.sendMessage("§cYou're not in a build area.")
                } else player.sendMessage("§cA model with that name already exists.")
            } else player.sendMessage("§7This name is not allowed.")
        } else player.sendMessage("§cWrong syntax")
        return false
    }

}