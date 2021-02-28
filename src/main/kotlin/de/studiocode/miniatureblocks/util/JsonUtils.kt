package de.studiocode.miniatureblocks.util

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.io.File

fun JsonElement.writeToFile(file: File) =
    file.writeText(toString())

fun JsonElement.isString() =
    isJsonPrimitive && asJsonPrimitive.isString

fun JsonElement.isBoolean() =
    isJsonPrimitive && asJsonPrimitive.isBoolean

fun JsonElement.isNumber() =
    isJsonPrimitive && asJsonPrimitive.isNumber

fun JsonArray.addAll(vararg numbers: Number) =
    numbers.forEach { add(it) }

fun JsonArray.addAll(vararg booleans: Boolean) =
    booleans.forEach { add(it) }

fun JsonArray.addAll(vararg chars: Char) =
    chars.forEach { add(it) }

fun JsonArray.addAll(vararg elements: JsonElement) =
    elements.forEach { add(it) }

fun JsonArray.addAll(doubleArray: DoubleArray) =
    doubleArray.forEach { add(it) }

fun JsonArray.addAll(stringArray: Array<String>) =
    stringArray.forEach { add(it) }

fun JsonArray.getAllStrings(): List<String> {
    val strings = ArrayList<String>()
    forEach { if (it.isString()) strings.add(it.asString) }
    return strings
}

inline fun <reified T> Gson.fromJson(jsonElement: JsonElement?): T? {
    if (jsonElement == null) return null
    return fromJson(jsonElement, object : TypeToken<T>() {}.type)
}