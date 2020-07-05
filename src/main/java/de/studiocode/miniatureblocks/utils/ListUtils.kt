package de.studiocode.miniatureblocks.utils

fun <E> ArrayList<E>.shiftRight(times: Int = 1) {
    
    for (i in 0 until times) {
        add(0, get(size - 1))
        removeAt(size - 1)
    }
    
    
}