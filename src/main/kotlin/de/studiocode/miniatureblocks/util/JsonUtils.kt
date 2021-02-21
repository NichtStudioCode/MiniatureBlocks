package de.studiocode.miniatureblocks.util

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import java.io.File

fun JsonArray.addAll(vararg numbers: Number) =
    numbers.forEach { add(it) }

fun JsonArray.addAll(vararg strings: String) =
    strings.forEach { add(it) }

fun JsonArray.addAll(vararg booleans: Boolean) =
    booleans.forEach { add(it) }

fun JsonArray.addAll(vararg chars: Char) =
    chars.forEach { add(it) }

fun JsonArray.addAll(vararg elements: JsonElement) =
    elements.forEach { add(it) }

fun JsonArray.addAll(doubleArray: DoubleArray) =
    doubleArray.forEach { add(it) }

fun JsonElement.writeToFile(file: File) =
    file.writeText(toString())