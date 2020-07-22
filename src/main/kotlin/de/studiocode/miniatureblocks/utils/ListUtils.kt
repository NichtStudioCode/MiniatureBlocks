package de.studiocode.miniatureblocks.utils

import kotlin.math.absoluteValue

fun <E> ArrayList<E>.shift(shift: Int = 1) {
    val shiftRight = shift > 0

    repeat(shift.absoluteValue) {
        if (shiftRight) {
            add(0, get(size - 1))
            removeAt(size - 1)
        } else {
            add(size, get(0))
            removeAt(0)
        }
    }
}
