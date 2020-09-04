package de.studiocode.miniatureblocks.utils

object StringUtils {

    private const val ALPHABET = "abcedfghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    fun randomString(length: Int, characters: String = ALPHABET): String {
        val stringBuilder = StringBuilder()
        repeat(length) {
            stringBuilder.append(characters.random())
        }
        return stringBuilder.toString()
    }
    
}
