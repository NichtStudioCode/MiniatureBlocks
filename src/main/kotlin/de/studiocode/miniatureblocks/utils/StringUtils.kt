package de.studiocode.miniatureblocks.utils

object StringUtils {

    private const val ALPHABET = "abcedfghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    fun randomString(length: Int, characters: String = ALPHABET): String {
        val chars = characters.toCharArray()
        val charLen = chars.size
        val stringBuilder = StringBuilder()
        repeat(length) {
            stringBuilder.append(characters[MathUtils.randomInt(0, charLen)])
        }
        return stringBuilder.toString()
    }
    
}
