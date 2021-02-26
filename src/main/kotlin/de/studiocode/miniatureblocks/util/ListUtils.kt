package de.studiocode.miniatureblocks.util

import me.xdrop.fuzzywuzzy.FuzzySearch
import kotlin.math.absoluteValue

fun <E> MutableList<E>.shift(shift: Int = 1) {
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

fun <E> Collection<E>.searchFor(search: String, getString: (E) -> String): List<E> {
    val strings = ArrayList<String>()
    val elements = ArrayList<Pair<String, E>>()
    
    forEach {
        val string = getString(it)
        if (getString(it).contains(search, true)) {
            strings += string
            elements += string to it
        }
    }
    
    val results = FuzzySearch.extractAll(search, strings).apply { sortByDescending { it.score } }
    return results.map { result -> elements.find { it.first == result.string }!!.second }
}

fun Collection<String>.searchFor(search: String): List<String> {
    val results = FuzzySearch.extractAll(search, filter { it.contains(search, true) }).apply { sortByDescending { it.score } }
    return results.map { it.string }
}
