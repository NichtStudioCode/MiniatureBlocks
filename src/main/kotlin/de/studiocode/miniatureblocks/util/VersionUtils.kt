package de.studiocode.miniatureblocks.util

import org.bukkit.Bukkit

object VersionUtils {
    
    private val major: Int
    private val minor: Int
    private val patch: Int
    
    init {
        val version = Bukkit.getVersion().substringAfter("MC: ").substringBefore(')')
        val parts = parseVersion(version)
        major = parts[0]
        minor = parts[1]
        patch = parts[2]
    }
    
    private fun parseVersion(version: String) = version.split('.').map(Integer::parseInt).toTypedArray()
    
    fun isVersionOrHigher(version: String): Boolean {
        val parts = parseVersion(version)
        return major >= parts[0] && minor >= parts[1] && patch >= parts[2]
    }
    
}