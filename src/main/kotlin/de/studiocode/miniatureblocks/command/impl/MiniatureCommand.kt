package de.studiocode.miniatureblocks.command.impl

import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.command.PlayerCommand
import de.studiocode.miniatureblocks.menu.inventory.impl.AnimationMenu
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStand
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStand.CommandType
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStandManager
import de.studiocode.miniatureblocks.miniature.armorstand.hasMiniatureData
import de.studiocode.miniatureblocks.miniature.armorstand.impl.AnimatedMiniatureArmorStand
import de.studiocode.miniatureblocks.miniature.item.impl.AnimatedMiniatureItem
import de.studiocode.miniatureblocks.resourcepack.model.BuildDataModelParser
import de.studiocode.miniatureblocks.utils.getTargetMiniature
import de.studiocode.miniatureblocks.utils.openInventory
import de.studiocode.miniatureblocks.utils.sendPrefixedMessage
import org.bukkit.entity.Player

class MiniatureCommand(name: String, permission: String) : PlayerCommand(name, permission) {

    private val namePattern = "[A-Za-z0-9]*".toRegex()

    init {
        command = command
                .then(literal("create")
                        .then(argument<String>("name", StringArgumentType.word())
                                .executes { handleCreateCommand(true, it); 0 }))
                .then(literal("createsilently")
                        .then(argument<String>("name", StringArgumentType.word())
                                .executes { handleCreateCommand(false, it); 0 }))
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
                .then(literal("sync")
                        .then(literal("autorotate")
                                .executes { handleSyncCommand(true, it); 0 })
                        .then(literal("animation")
                                .executes { handleSyncCommand(false, it); 0 }))
                .then(literal("animation")
                        .executes { handleAnimationCommand(it); 0 })
    }

    private fun handleCreateCommand(forceResourcePack: Boolean, context: CommandContext<Any>) {
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
                    resourcePack.addNewModel(name, modelData, forceResourcePack)

                    player.sendPrefixedMessage("§7A new model has been created.")
                } else player.sendPrefixedMessage("§cYou're not in a build area.")
            } else player.sendPrefixedMessage("§cA model with that name already exists.")
        } else player.sendPrefixedMessage("§cName does not match pattern $namePattern")
    }

    private fun handleAutoRotateCommand(context: CommandContext<Any>) {
        val player = getPlayer(context.source)
        val degreesPerTick = context.getArgument("degreesPerTick", Float::class.java)

        getPlayersTargetMiniature(player)?.setAutoRotate(degreesPerTick)
    }

    private fun handleRotateCommand(context: CommandContext<Any>) {
        val player = getPlayer(context.source)
        val degrees = context.getArgument("degrees", Float::class.java)

        getPlayersTargetMiniature(player)?.rotate(degrees)
    }

    private fun handleCommandAddCommand(commandType: CommandType, context: CommandContext<Any>) {
        val player = getPlayer(context.source)
        val command = context.getArgument("command", String::class.java)

        val miniature = getPlayersTargetMiniature(player)
        if (miniature != null) {
            miniature.setCommand(commandType, command)
            player.sendPrefixedMessage("§7The command has been set.")
        }
    }

    private fun handleCommandRemoveCommand(commandType: CommandType, context: CommandContext<Any>) {
        val player = getPlayer(context.source)

        val miniature = getPlayersTargetMiniature(player)
        if (miniature != null) {
            miniature.removeCommand(commandType)
            player.sendPrefixedMessage("§7The command has been removed.")
        }
    }

    private fun handleNoRotateCommand(noRotate: Boolean, context: CommandContext<Any>) {
        val player = getPlayer(context.source)

        val miniature = getPlayersTargetMiniature(player)
        if (miniature != null) {
            miniature.setNoRotate(noRotate)
            if (noRotate) player.sendPrefixedMessage("§7That miniature can't be rotated anymore now.")
            else player.sendPrefixedMessage("§7That miniature can now be rotated again.")
        }
    }

    private fun handleSyncCommand(autoRotate: Boolean, context: CommandContext<Any>) {
        val player = getPlayer(context.source)

        val miniatureManager = MiniatureBlocks.INSTANCE.miniatureManager
        if (autoRotate) {
            miniatureManager.loadedMiniatures.values
                    .forEach(MiniatureArmorStand::resetAutoRotate)
        } else {
            miniatureManager.loadedMiniatures.values
                    .filterIsInstance<AnimatedMiniatureArmorStand>()
                    .forEach(AnimatedMiniatureArmorStand::resetAnimationState)
        }
        player.sendPrefixedMessage("§7Successfully synced")
    }

    private fun handleAnimationCommand(context: CommandContext<Any>) {
        try {
            val player = getPlayer(context.source)
            val itemStack = player.inventory.itemInMainHand
            
            val data = if (itemStack.itemMeta?.hasMiniatureData() == true) {
                val item =  MiniatureArmorStandManager.MiniatureType.newInstance(itemStack)
                if (item is AnimatedMiniatureItem) item.data
                else null
            } else null
            
            player.openInventory(AnimationMenu(data))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPlayersTargetMiniature(player: Player): MiniatureArmorStand? {
        val miniature = player.getTargetMiniature()
        if (miniature == null) player.sendPrefixedMessage("§cPlease look at the miniature and try again.")
        return miniature
    }

}
