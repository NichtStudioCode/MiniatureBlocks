package de.studiocode.miniatureblocks.command.impl

import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.command.PlayerCommand
import de.studiocode.miniatureblocks.miniature.MiniatureManager.CommandType
import de.studiocode.miniatureblocks.resourcepack.model.BuildDataModelParser
import de.studiocode.miniatureblocks.utils.getTargetMiniature
import de.studiocode.miniatureblocks.utils.sendPrefixedMessage
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player

class MiniatureCommand(name: String, permission: String) : PlayerCommand(name, permission) {

    private val namePattern = "[A-Za-z0-9]*".toRegex()
    private val miniatureManager = MiniatureBlocks.INSTANCE.miniatureManager
    
    init {
        command = command
                .then(literal("create")
                        .then(argument<String>("name", StringArgumentType.word())
                                .executes { handleCreateCommand(it); 0 }))
                .then(literal("autorotate")
                        .then(argument<Float>("degreesPerTick", FloatArgumentType.floatArg())
                                .executes { handleAutoRotateCommand(it); 0 }))
                .then(literal("rotation")
                        .then(argument<Float>("degrees", FloatArgumentType.floatArg())
                                .executes { handleRotateCommand(it); 0 }))
                .then(literal("command")
                        .then(literal("set")
                                .then(literal("shift-right")
                                        .then(argument("command", StringArgumentType.greedyString())
                                                .executes { handleCommandAddCommand(CommandType.SHIFT_RIGHT, it); 0 }))
                                .then(literal("right")
                                        .then(argument("command", StringArgumentType.greedyString())
                                                .executes { handleCommandAddCommand(CommandType.RIGHT, it); 0 })))
                        .then(literal("remove")
                                .then(literal("shift-right")
                                        .executes { handleCommandRemoveCommand(CommandType.SHIFT_RIGHT, it); 0 })
                                .then(literal("right")
                                        .executes { handleCommandRemoveCommand(CommandType.RIGHT, it); 0 })))
                .then(literal("norotate")
                        .then(literal("on")
                                .executes { handleNoRotateCommand(true, it); 0 })
                        .then(literal("off")
                                .executes { handleNoRotateCommand(false, it); 0 }))
    }

    private fun handleCreateCommand(context: CommandContext<Any>) {
        val player = getPlayer(context.source)
        val name = context.getArgument("name", String::class.java)

        if (name.matches(namePattern)) {
            val resourcePack = MiniatureBlocks.INSTANCE.resourcePack
            if (!resourcePack.hasModel(name)) {
                val builderWorld = MiniatureBlocks.INSTANCE.builderWorld
                if (builderWorld.isPlayerInValidBuildArea(player)) {
                    val buildData = builderWorld.getBuildData(player)
                    val parser = BuildDataModelParser(buildData)
                    val modelData = parser.parse()
                    resourcePack.addNewModel(name, modelData)

                    player.sendPrefixedMessage("§7A new model has been created.")
                } else player.sendPrefixedMessage("§cYou're not in a build area.")
            } else player.sendPrefixedMessage("§cA model with that name already exists.")
        } else player.sendPrefixedMessage("§cName does not match pattern $namePattern")
    }

    private fun handleAutoRotateCommand(context: CommandContext<Any>) {
        val player = getPlayer(context.source)
        val degreesPerTick = context.getArgument("degreesPerTick", Float::class.java)

        val miniature = getPlayersTargetMiniature(player)
        if (miniature != null) {
            miniatureManager.setMiniatureAutoRotate(miniature, degreesPerTick)
        }
    }

    private fun handleRotateCommand(context: CommandContext<Any>) {
        val player = getPlayer(context.source)
        val degrees = context.getArgument("degrees", Float::class.java)

        val miniature = getPlayersTargetMiniature(player)
        if (miniature != null) {
            miniatureManager.rotateMiniature(miniature, degrees)
        }
    }

    private fun handleCommandAddCommand(commandType: CommandType, context: CommandContext<Any>) {
        val player = getPlayer(context.source)
        val command = context.getArgument("command", String::class.java)

        val miniature = getPlayersTargetMiniature(player)
        if (miniature != null) {
            miniatureManager.setMiniatureCommand(miniature, commandType, command)
            player.sendPrefixedMessage("§7The command has been set.")
        }
    }

    private fun handleCommandRemoveCommand(commandType: CommandType, context: CommandContext<Any>) {
        val player = getPlayer(context.source)

        val miniature = getPlayersTargetMiniature(player)
        if (miniature != null) {
            miniatureManager.removeMiniatureCommand(miniature, commandType)
            player.sendPrefixedMessage("§7The command has been removed.")
        }
    }

    private fun handleNoRotateCommand(noRotate: Boolean, context: CommandContext<Any>) {
        val player = getPlayer(context.source)

        val miniature = getPlayersTargetMiniature(player)
        if (miniature != null) {
            miniatureManager.setMiniatureNoRotate(miniature, noRotate)
            if (noRotate) player.sendPrefixedMessage("§7That miniature can't be rotated anymore now.")
            else player.sendPrefixedMessage("§7That miniature can now be rotated again.")
        }
    }

    private fun getPlayersTargetMiniature(player: Player): ArmorStand? {
        val miniature = player.getTargetMiniature()
        if (miniature == null) player.sendPrefixedMessage("§cPlease look at the miniature and try again.")
        return miniature
    }

}
