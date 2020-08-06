package de.studiocode.miniatureblocks.utils

import de.studiocode.miniatureblocks.MiniatureBlocks
import net.lingala.zip4j.ZipFile
import java.io.File
import java.io.FileOutputStream

object FileUtils {

    fun extractFile(path: String, file: File) {
        val stream = FileUtils::class.java.getResourceAsStream(path)
        val fout = FileOutputStream(file)
        stream.copyTo(fout, 4096)
    }
    
    fun listExtractableFiles(path: String): List<String> {
        val zip = ZipFile(MiniatureBlocks.INSTANCE.pluginFile)
        
        return zip.fileHeaders
                .filter { !it.isDirectory }
                .map { it.fileName }
                .filter { it.startsWith(path) }
    }
    

}