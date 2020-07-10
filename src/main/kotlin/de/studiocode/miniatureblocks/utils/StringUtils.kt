package de.studiocode.miniatureblocks.utils

fun String.containsOnly(characters: String, ignoreCase: Boolean = false): Boolean {

    if (ignoreCase) {
        toLowerCase()
        characters.toLowerCase()
    }

    val characterChars = characters.toCharArray()
    return toCharArray().none { !characterChars.contains(it) }
}

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