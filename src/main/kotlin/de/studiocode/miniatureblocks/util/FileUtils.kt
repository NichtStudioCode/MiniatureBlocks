package de.studiocode.miniatureblocks.util

import de.studiocode.miniatureblocks.MiniatureBlocks
import net.lingala.zip4j.ZipFile
import java.io.File
import java.io.FileOutputStream
import java.net.URL

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
    
    fun mergeZips(out: File, vararg zipFiles: File) {
        val tempDir = File("temp" + StringUtils.randomString(10)).apply { mkdirs() }
        for (file in zipFiles) {
            val zip = ZipFile(file)
            zip.extractAll(tempDir.absolutePath)
        }
        
        val zip = ZipFile(out)
        tempDir.listFiles()!!.forEach { if (it.isFile) zip.addFile(it) else zip.addFolder(it) }
        tempDir.deleteRecursively()
    }
    
    
}

fun File.downloadFrom(url: URL) =
    url.openStream().use { inStream -> outputStream().use { outStream -> inStream.copyTo(outStream) } }
