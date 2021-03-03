package de.studiocode.miniatureblocks.util

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.reflect.TypeToken
import java.io.File

fun JsonElement.writeToFile(file: File) =
    file.writeText(toString())

fun JsonElement.isString() =
    this is JsonPrimitive && isString

fun JsonElement.isBoolean() =
    this is JsonPrimitive && isBoolean

fun JsonElement.isNumber() =
    this is JsonPrimitive && isNumber

fun JsonArray.addAll(vararg numbers: Number) =
    numbers.forEach(this::add)

fun JsonArray.addAll(vararg booleans: Boolean) =
    booleans.forEach(this::add)

fun JsonArray.addAll(vararg chars: Char) =
    chars.forEach(this::add)

fun JsonArray.addAll(vararg elements: JsonElement) =
    elements.forEach(this::add)

fun JsonArray.addAll(doubleArray: DoubleArray) =
    doubleArray.forEach(this::add)

fun JsonArray.addAll(stringArray: Array<String>) =
    stringArray.forEach(this::add)

fun JsonArray.getAllStrings() =
    filter(JsonElement::isString).map { it.asString }

inline fun <reified T> Gson.fromJson(jsonElement: JsonElement?): T? {
    if (jsonElement == null) return null
    return fromJson(jsonElement, object : TypeToken<T>() {}.type)
}