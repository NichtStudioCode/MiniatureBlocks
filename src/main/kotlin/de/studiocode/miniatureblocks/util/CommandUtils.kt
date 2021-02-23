package de.studiocode.miniatureblocks.util

import com.mojang.brigadier.context.CommandContext

fun CommandContext<*>.getPlayer() =
    ReflectionUtils.getPlayerFromCommandListenerWrapper(source)!!

inline fun <reified V> CommandContext<*>.getArgument(name: String): V =
    getArgument(name, V::class.java)
