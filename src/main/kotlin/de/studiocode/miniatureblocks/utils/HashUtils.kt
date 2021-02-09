package de.studiocode.miniatureblocks.utils

import java.io.File
import java.security.MessageDigest

object HashUtils {
    
    fun createSha1Hash(file: File): ByteArray {
        val inputStream = file.inputStream()
        val md = MessageDigest.getInstance("SHA1")
        var len = -1
        val buffer = ByteArray(4096)
        while ({ len = inputStream.read(buffer); len }() != -1) {
            md.update(buffer, 0, len)
        }
        inputStream.close()
        return md.digest()
    }
    
}