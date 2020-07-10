package de.studiocode.miniatureblocks.utils

import java.util.*

object MathUtils {
    
    private val random = Random()
    
    fun randomInt(includeLow: Int, excludeHigh: Int): Int = random.nextInt(excludeHigh - includeLow) + includeLow
    
}