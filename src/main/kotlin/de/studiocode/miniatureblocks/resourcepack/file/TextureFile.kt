package de.studiocode.miniatureblocks.resourcepack.file

import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import java.io.File

class TextureFile : RPFile {
    
    val animationData: AnimationDataFile
        get() = parentFile.getAnimationData("$name.mcmeta")
    
    constructor(resourcePack: ResourcePack, file: File) : super(resourcePack, file)
    
    constructor(resourcePack: ResourcePack, path: String) : super(resourcePack, path)
    
    fun isAnimated() = animationData.exists()
    
    fun makeAnimated(frameTime: Int) {
        val animationDataFile = animationData
        animationDataFile.frametime = frameTime
        animationDataFile.save()
    }
    
}
