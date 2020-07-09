package de.studiocode.miniatureblocks.utils

fun String.containsOnly(characters: String, ignoreCase: Boolean = false): Boolean {
    
    if (ignoreCase) {
        toLowerCase()
        characters.toLowerCase()
    }
    
    val characterChars = characters.toCharArray()
    return toCharArray().none { !characterChars.contains(it) }
}