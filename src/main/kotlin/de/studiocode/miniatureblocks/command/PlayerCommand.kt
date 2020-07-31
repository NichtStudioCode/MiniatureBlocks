package de.studiocode.miniatureblocks.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import de.studiocode.miniatureblocks.utils.ReflectionUtils
import org.bukkit.entity.Player

abstract class PlayerCommand(val name: String, private val permission: String) {

    var command: LiteralArgumentBuilder<Any> = literal(name).requires {
        isExecutedByPlayer(it) && getPlayer(it).hasPermission(permission)
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

    private fun isExecutedByPlayer(source: Any): Boolean = getPlayerOrNull(source) != null

    fun getPlayer(source: Any): Player = getPlayerOrNull(source)!!

}
