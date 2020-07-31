package de.studiocode.miniatureblocks.utils

import kotlin.collections.MutableMap.MutableEntry

fun <K, V> LinkedHashMap<K, V>.getKeyAtIndex(index: Int): K {
    return ArrayList<K>(keys)[index]
}

fun <K, V> LinkedHashMap<K, V>.getValueAtIndex(index: Int): V {
    return ArrayList<V>(values)[index]
}

fun <K, V> LinkedHashMap<K, V>.getAtIndex(index: Int): MutableEntry<K, V> {
    return ArrayList<MutableEntry<K, V>>(entries)[index]
}

fun <K, V> LinkedHashMap<K, V>.getFirstKey(): K {
    return getKeyAtIndex(0)
}

fun <K, V> LinkedHashMap<K, V>.getFirstValue(): V {
    return getValueAtIndex(0)
}

fun <K, V> LinkedHashMap<K, V>.getFirst(): MutableEntry<K, V> {
    return getAtIndex(0)
}

fun <K, V> LinkedHashMap<K, V>.getLastKey(): K {
    return getKeyAtIndex(size - 1)
}

fun <K, V> LinkedHashMap<K, V>.getLastValue(): V {
    return getValueAtIndex(size - 1)
}

fun <K, V> LinkedHashMap<K, V>.getLast(): MutableEntry<K, V> {
    return getAtIndex(size - 1)
}
