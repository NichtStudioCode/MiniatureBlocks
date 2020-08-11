package de.studiocode.miniatureblocks.utils

import java.util.*
import kotlin.math.roundToInt

object MathUtils {
    
    private val random = Random()
    
    fun randomInt(includeLow: Int, excludeHigh: Int): Int = random.nextInt(excludeHigh - includeLow) + includeLow
    
    fun roundToDecimalDigits(number: Double, digits: Int): Double {
        val multiplier = digits * 10
        return (number * multiplier).roundToInt().toDouble() / multiplier
    }
    
}