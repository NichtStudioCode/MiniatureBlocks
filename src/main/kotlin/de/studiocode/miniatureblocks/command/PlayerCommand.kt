package de.studiocode.miniatureblocks.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import de.studiocode.miniatureblocks.util.ReflectionUtils.createPlayerFromCommandListenerWrapper

abstract class PlayerCommand(val name: String, private val permission: String) {
    
    var command: LiteralArgumentBuilder<Any> = literal(name).requires {
        val player = createPlayerFromCommandListenerWrapper(it)
        player?.hasPermission(permission) ?: false
    }
    
    fun literal(name: String): LiteralArgumentBuilder<Any> {
        return LiteralArgumentBuilder.literal(name)
    }
    
    fun <T> argument(name: String, argumentType: ArgumentType<T>): RequiredArgumentBuilder<Any, T> {
        return RequiredArgumentBuilder.argument(name, argumentType)
    }
    
}
