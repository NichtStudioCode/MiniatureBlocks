package de.studiocode.miniatureblocks.resourcepack

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.build.BuildDataCreator
import de.studiocode.miniatureblocks.resourcepack.model.MiniatureModel
import org.bukkit.Location
import java.util.concurrent.Executors
import java.util.concurrent.Future

object MiniatureCreator {
    
    private val executorService = Executors.newSingleThreadExecutor()
    private var currentTask: Future<*>? = null
    
    fun submitCreationRequest(name: String, forceResourcePack: Boolean, min: Location,
                              max: Location, runAfter: () -> Unit) {
        if (isBusy()) return
        
        if (currentTask?.isDone != false) {
            currentTask = executorService.submit {
                val buildData = BuildDataCreator(min, max).createData()
                val modelData = MiniatureModel(buildData).modelDataObj
                MiniatureBlocks.INSTANCE.resourcePack.addNewModel(name, modelData, forceResourcePack)
                
                runAfter()
            }
        }
    }
    
    fun isBusy(): Boolean = currentTask?.isDone == false
    
}