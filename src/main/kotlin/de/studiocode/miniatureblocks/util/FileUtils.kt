package de.studiocode.miniatureblocks.util

import com.google.common.base.Preconditions
import de.studiocode.miniatureblocks.MiniatureBlocks
import net.lingala.zip4j.ZipFile
import java.io.File
import java.io.FileOutputStream
import java.net.URL

object FileUtils {
    
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
    
    fun extractFiles(path: String, to: File) {
        Preconditions.checkArgument(to.isDirectory, "To is not a directory")
        
        listExtractableFiles(path).forEach {
            val file = File(to, it.substring(path.length))
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            extractFile("/$it", file)
        }
    }
    
    private fun extractFile(path: String, file: File) {
        if (file.parentFile.exists()) file.parentFile.mkdirs()
        FileUtils::class.java.getResourceAsStream(path).use { inStream ->
            FileOutputStream(file).use { outStream ->
                inStream.copyTo(outStream)
            }
        }
    }
    
    fun listExtractableFiles(path: String): List<String> {
        val zip = ZipFile(MiniatureBlocks.INSTANCE.pluginFile)
        
        return zip.fileHeaders
            .filter { !it.isDirectory }
            .map { it.fileName }
            .filter { it.startsWith(path) }
    }
    
    fun getFileInJar(path: String) = FileUtils::class.java.getResource(path).readBytes()
    
}

fun File.downloadFrom(url: URL) =
    url.openStream().use { inStream -> outputStream().use { outStream -> inStream.copyTo(outStream) } }

fun File.isParentOf(other: File) = other.toPath().normalize().toAbsolutePath().startsWith(toPath().normalize().toAbsolutePath())

fun File.isChildOf(other: File) = toPath().normalize().toAbsolutePath().startsWith(other.toPath().normalize().toAbsolutePath())
