package de.studiocode.miniatureblocks.command.impl

import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.command.PlayerCommand
import de.studiocode.miniatureblocks.menu.AnimationMenu
import de.studiocode.miniatureblocks.menu.TexturesMenu
import de.studiocode.miniatureblocks.miniature.armorstand.ArmorStandMoveManager
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStand
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStand.CommandType
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureManager
import de.studiocode.miniatureblocks.miniature.armorstand.hasMiniatureData
import de.studiocode.miniatureblocks.miniature.armorstand.impl.AnimatedMiniatureArmorStand
import de.studiocode.miniatureblocks.miniature.item.impl.AnimatedMiniatureItem
import de.studiocode.miniatureblocks.miniature.item.impl.NormalMiniatureItem
import de.studiocode.miniatureblocks.resourcepack.RPTaskManager
import de.studiocode.miniatureblocks.util.getArgument
import de.studiocode.miniatureblocks.util.getPlayer
import de.studiocode.miniatureblocks.util.getTargetMiniature
import de.studiocode.miniatureblocks.util.sendPrefixedMessage
import org.bukkit.Location
import org.bukkit.entity.Player

class MiniatureCommand(name: String, permission: String) : PlayerCommand(name, permission) {
    
    private val namePattern = "[a-z0-9]*".toRegex()
    
    init {
        command = command
            .then(literal("selection")
                .then(literal("marker")
                    .executes { handleGiveMarkerCommand(it); 0 })
                .then(literal("pos1")
                    .executes { handleSetPosCommand(true, it); 0 })
                .then(literal("pos2")
                    .executes { handleSetPosCommand(false, it); 0 })
                .then(literal("clear")
                    .executes { handleClearSelectionCommand(it); 0 }))
            .then(literal("create")
                .then(argument<String>("name", StringArgumentType.word())
                    .executes { handleCreateCommand(true, it); 0 }
                    .then(literal("silent")
                        .executes { handleCreateCommand(false, it); 0 })))
            .then(literal("autorotate")
                .then(argument<Float>("degreesPerTick", FloatArgumentType.floatArg())
                    .executes { handleAutoRotateCommand(it); 0 }))
            .then(literal("rotation")
                .then(argument<Float>("degrees", FloatArgumentType.floatArg())
                    .executes { handleRotateCommand(it); 0 }))
            .then(literal("bounce")
                .then(argument<Float>("maxHeight", FloatArgumentType.floatArg())
                    .then(argument<Float>("speed", FloatArgumentType.floatArg())
                        .executes { handleBounceCommand(it); 0 })))
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
            .then(literal("move")
                .executes { handleMoveCommand(it); 0 })
            .then(literal("textures")
                .executes { handleTexturesCommand(it); 0 }
                .then(literal("add")
                    .then(argument("name", StringArgumentType.string())
                        .then(argument("url", StringArgumentType.greedyString())
                            .executes { handleAddTextureCommand(false, it); 0 })))
                .then(literal("addanimated")
                    .then(argument("name", StringArgumentType.string())
                        .then(argument("frameTime", IntegerArgumentType.integer())
                            .then(argument("url", StringArgumentType.greedyString())
                                .executes { handleAddTextureCommand(true, it); 0 }))))
                .then(literal("remove")
                    .then(argument("name", StringArgumentType.greedyString())
                        .executes { handleRemoveTextureCommand(it); 0 })))
    }
    
    private fun handleGiveMarkerCommand(context: CommandContext<Any>) {
        try {
            val player = context.getPlayer()
            MiniatureBlocks.INSTANCE.regionManager.giveMarker(player)
            player.sendPrefixedMessage("§7The marker has been added to your inventory. " +
                "Left-click to select the first and right-click to select the second position.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun handleSetPosCommand(first: Boolean, context: CommandContext<Any>) {
        try {
            val player = context.getPlayer()
            MiniatureBlocks.INSTANCE.regionManager.markPosition(first, player, player.location)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun handleClearSelectionCommand(context: CommandContext<Any>) {
        try {
            val player = context.getPlayer()
            MiniatureBlocks.INSTANCE.regionManager.regions.remove(player.uniqueId)
            player.sendPrefixedMessage("§7Your selection has been cleared successfully.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun handleCreateCommand(forceResourcePack: Boolean, context: CommandContext<Any>) {
        try {
            val player = context.getPlayer()
            val name = context.getArgument<String>("name")
            if (name.matches(namePattern)) {
                val resourcePack = MiniatureBlocks.INSTANCE.resourcePack
                if (!resourcePack.mainModelData.hasModel(name)) {
                    if (!RPTaskManager.isBusy()) {
                        val builderWorld = MiniatureBlocks.INSTANCE.builderWorld
                        val regionManager = MiniatureBlocks.INSTANCE.regionManager
                        
                        var min: Location? = null
                        var max: Location? = null
                        
                        when {
                            builderWorld.isPlayerInValidBuildArea(player) -> {
                                min = player.location.chunk.getBlock(0, 1, 0).location
                                max = player.location.chunk.getBlock(15, 16, 15).location
                            }
                            
                            regionManager.hasValidRegion(player) -> {
                                regionManager.take(player) {
                                    min = it.getFirst()
                                    max = it.getSecond()
                                }
                            }
                            
                            else -> player.sendPrefixedMessage("§cYou have no selected region.")
                        }
                        
                        if (min != null && max != null) {
                            RPTaskManager.submitMiniatureCreationRequest(name, forceResourcePack, min!!, max!!) {
                                player.sendPrefixedMessage("§7A new model has been created.")
    
                                val item = NormalMiniatureItem.create(
                                    MiniatureBlocks.INSTANCE.resourcePack.mainModelData.getCustomModelFromName(name)!!
                                ).itemStack
                                
                                player.inventory.addItem(item)
                            }
                        }
                    } else player.sendPrefixedMessage("§cMiniatureBlocks is busy, try again later.")
                } else player.sendPrefixedMessage("§cA model with that name already exists.")
            } else player.sendPrefixedMessage("§cName does not match pattern $namePattern. Only lowercase letters and numbers are allowed!")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun handleAutoRotateCommand(context: CommandContext<Any>) {
        val player = context.getPlayer()
        val degreesPerTick = context.getArgument<Float>("degreesPerTick")
        
        getPlayersTargetMiniature(player)?.setAutoRotate(degreesPerTick)
    }
    
    private fun handleRotateCommand(context: CommandContext<Any>) {
        val player = context.getPlayer()
        val degrees = context.getArgument<Float>("degrees")
        
        getPlayersTargetMiniature(player)?.rotate(degrees)
    }
    
    private fun handleBounceCommand(context: CommandContext<Any>) {
        val player = context.getPlayer()
        val maxHeight = context.getArgument<Float>("maxHeight")
        val bounceSpeed = context.getArgument<Float>("speed")
        
        getPlayersTargetMiniature(player)?.setBounce(maxHeight, bounceSpeed)
    }
    
    private fun handleCommandAddCommand(commandType: CommandType, context: CommandContext<Any>) {
        val player = context.getPlayer()
        val command = context.getArgument<String>("command")
        
        val miniature = getPlayersTargetMiniature(player)
        if (miniature != null) {
            miniature.setCommand(commandType, command)
            player.sendPrefixedMessage("§7The command has been set.")
        }
    }
    
    private fun handleCommandRemoveCommand(commandType: CommandType, context: CommandContext<Any>) {
        val player = context.getPlayer()
        
        val miniature = getPlayersTargetMiniature(player)
        if (miniature != null) {
            miniature.removeCommand(commandType)
            player.sendPrefixedMessage("§7The command has been removed.")
        }
    }
    
    private fun handleNoRotateCommand(noRotate: Boolean, context: CommandContext<Any>) {
        val player = context.getPlayer()
        
        val miniature = getPlayersTargetMiniature(player)
        if (miniature != null) {
            miniature.setNoRotate(noRotate)
            if (noRotate) player.sendPrefixedMessage("§7That miniature can't be rotated anymore now.")
            else player.sendPrefixedMessage("§7That miniature can now be rotated again.")
        }
    }
    
    private fun handleSyncCommand(autoRotate: Boolean, context: CommandContext<Any>) {
        val player = context.getPlayer()
        
        val miniatureManager = MiniatureBlocks.INSTANCE.miniatureManager
        if (autoRotate) {
            miniatureManager.loadedMiniatures.values
                .forEach(MiniatureArmorStand::resetMovement)
        } else {
            miniatureManager.loadedMiniatures.values
                .filterIsInstance<AnimatedMiniatureArmorStand>()
                .forEach(AnimatedMiniatureArmorStand::resetAnimationState)
        }
        player.sendPrefixedMessage("§7Successfully synced")
    }
    
    private fun handleAnimationCommand(context: CommandContext<Any>) {
        try {
            val player = context.getPlayer()
            val itemStack = player.inventory.itemInMainHand
            
            val data = if (itemStack.itemMeta?.hasMiniatureData() == true) {
                val item = MiniatureManager.MiniatureType.newInstance(itemStack)
                if (item is AnimatedMiniatureItem) item.data
                else null
            } else null
            
            AnimationMenu(player, data).openWindow()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun handleMoveCommand(context: CommandContext<Any>) {
        val player = context.getPlayer()
        
        val miniature = getPlayersTargetMiniature(player)
        if (miniature != null) {
            ArmorStandMoveManager.addToMove(player, miniature.armorStand)
        }
    }
    
    private fun handleTexturesCommand(context: CommandContext<Any>) {
        try {
            val player = context.getPlayer()
            
            TexturesMenu(player).openWindow()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun handleAddTextureCommand(animated: Boolean, context: CommandContext<Any>) {
        try {
            val player = context.getPlayer()
            val name = context.getArgument<String>("name")
            val url = context.getArgument<String>("url")
            val frameTime = if (animated) context.getArgument("frameTime") else 0
            
            if (name.matches(namePattern)) {
                if (!RPTaskManager.isBusy()) {
                    RPTaskManager.submitTextureDownloadRequest(player, name, url, frameTime)
                } else player.sendPrefixedMessage("§cMiniatureBlocks is busy, try again later.")
            } else player.sendPrefixedMessage("§cName does not match pattern $namePattern. Only lowercase letters and numbers are allowed!")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun handleRemoveTextureCommand(context: CommandContext<Any>) {
        try {
            val player = context.getPlayer()
            val name = context.getArgument<String>("name")
            
            if (name.matches(namePattern)) {
                if (!RPTaskManager.isBusy()) {
                    RPTaskManager.submitTextureRemovalRequest(player, name)
                } else player.sendPrefixedMessage("§cMiniatureBlocks is busy, try again later.")
            } else player.sendPrefixedMessage("§cName does not match pattern $namePattern. Only lowercase letters and numbers are allowed!")
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
