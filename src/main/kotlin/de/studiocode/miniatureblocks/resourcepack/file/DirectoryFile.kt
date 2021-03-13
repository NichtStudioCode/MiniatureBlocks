package de.studiocode.miniatureblocks.resourcepack.file

import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import java.io.File

class DirectoryFile : RPFile {
    
    init {
        if (!exists()) mkdirs()
    }
    
    constructor(resourcePack: ResourcePack, file: File) : super(resourcePack, file)
    
    constructor(resourcePack: ResourcePack, path: String) : super(resourcePack, path)
    
    fun getDirectory(path: String) = DirectoryFile(resourcePack, getChild(path))
    
    fun getTexture(path: String) = TextureFile(resourcePack, getChild(path))
    
    fun getAnimationData(path: String) = AnimationDataFile(resourcePack, getChild(path))
    
    private fun getChild(path: String) = File(this, path)
    
}