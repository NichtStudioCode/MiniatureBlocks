package de.studiocode.miniatureblocks.utils

import java.io.File
import java.security.MessageDigest

object HashUtils {
    
    fun createSha1Hash(file: File): ByteArray {
        val md = MessageDigest.getInstance("SHA1")
        return md.digest(file.readBytes())
    }
    
}