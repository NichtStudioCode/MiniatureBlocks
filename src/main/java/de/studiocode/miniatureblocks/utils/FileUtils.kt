package de.studiocode.miniatureblocks.utils

import java.io.File
import java.io.FileOutputStream

object FileUtils {
    
    fun extractFile(path: String, file: File) {
        val stream = FileUtils::class.java.getResourceAsStream(path)
        val fout = FileOutputStream(file)
        stream.copyTo(fout, 4096)
    }
    
}