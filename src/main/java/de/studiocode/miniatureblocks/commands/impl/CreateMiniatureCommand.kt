package de.studiocode.miniatureblocks.commands.impl

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.commands.PlayerCommand
import de.studiocode.miniatureblocks.resourcepack.model.BuildDataModelParser
import org.bukkit.entity.Player

class CreateMiniatureCommand : PlayerCommand() {

    //TODO: only accept certain characters

    override fun handleCommand(player: Player, message: String, args: Array<out String>): Boolean {
        if (args.size == 1) {
            val name = args[0].toLowerCase()
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
        } else player.sendMessage("§cWrong syntax")
        return false
    }

}