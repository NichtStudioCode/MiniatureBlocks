package de.studiocode.miniatureblocks.resourcepack.texture

private val PREFIXES = mapOf(
    "block/modded" to 1,
    "block/" to 0,
    "entity/" to 2
)

object TextureLocationComparator : Comparator<String> {
    
    override fun compare(a: String?, b: String?): Int {
        if (a == null && b == null) return 0
        else if (a == null && b != null) return 1
        else if (a != null && b == null) return -1
        
        a!!; b!!
        
        val prefixPriorityA = PREFIXES[PREFIXES.keys.first { a.startsWith(it) }]!!
        val prefixPriorityB = PREFIXES[PREFIXES.keys.first { b.startsWith(it) }]!!
        
        // prioritize block/ before block/modded before entity/
        return when {
            prefixPriorityA == prefixPriorityB -> a.compareTo(b) // compare actual text
            prefixPriorityA > prefixPriorityB -> 1
            else -> -1
        }
    }
    
}