package de.studiocode.miniatureblocks.resourcepack.file

import com.google.common.base.Preconditions
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import de.studiocode.miniatureblocks.util.isParentOf
import java.io.File

abstract class RPFile(val resourcePack: ResourcePack, file: File) : File(file.absolutePath) {
    
    init {
        Preconditions.checkArgument(resourcePack.main.isParentOf(file), "File is not inside the resource pack (${file.absolutePath})")
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
    }
    
    constructor(resourcePack: ResourcePack, path: String) :
        this(resourcePack, File(resourcePack.main, path))
    
    override fun getParentFile() = DirectoryFile(resourcePack, super.getParentFile())
    
}