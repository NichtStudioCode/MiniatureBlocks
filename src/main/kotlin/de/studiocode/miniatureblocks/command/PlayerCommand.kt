package de.studiocode.miniatureblocks.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.utils.ReflectionUtils
import de.studiocode.miniatureblocks.utils.ReflectionUtils.getEntityFromCommandListenerWrapper
import de.studiocode.miniatureblocks.utils.ReflectionUtils.getPlayerFromEntityPlayer
import org.bukkit.entity.Player

abstract class PlayerCommand(val name: String, private val permission: String) {

    var command: LiteralArgumentBuilder<Any> = literal(name).requires {
        val entity = getEntityFromCommandListenerWrapper(it)
        if (entity != null) {
            val player = getPlayerFromEntityPlayer(entity)
            if (player != null) {
                if (player.hasPermission(permission)) return@requires true
            } else {
                // command is executed by a nms player but there is no bukkit player for it and the permission can't be checked
                // try again later
                MiniatureBlocks.INSTANCE.commandManager.permissionUpdateEntities.add(entity)
            }
        }
        
        return@requires false
    }

    fun literal(name: String): LiteralArgumentBuilder<Any> {
        return LiteralArgumentBuilder.literal<Any>(name)
    }

    fun <T> argument(name: String, argumentType: ArgumentType<T>): RequiredArgumentBuilder<Any, T> {
        return RequiredArgumentBuilder.argument<Any, T>(name, argumentType)
    }

    private fun getPlayerOrNull(source: Any): Player? {
        return ReflectionUtils.getPlayerFromCommandListenerWrapper(source)
    }
    
    fun getPlayer(source: Any): Player = getPlayerOrNull(source)!!

}
