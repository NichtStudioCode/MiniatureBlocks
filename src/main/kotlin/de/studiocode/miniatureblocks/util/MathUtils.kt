package de.studiocode.miniatureblocks.util

fun Int.isEven() = this and 1 == 0

val Boolean.intValue: Int
    get() = if (this) 1 else 0
