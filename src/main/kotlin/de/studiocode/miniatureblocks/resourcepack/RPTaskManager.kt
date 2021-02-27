package de.studiocode.miniatureblocks.resourcepack

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.build.BuildDataCreator
import de.studiocode.miniatureblocks.resourcepack.model.MiniatureModel
import de.studiocode.miniatureblocks.util.sendPrefixedMessage
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.concurrent.Executors
import java.util.concurrent.Future

object RPTaskManager {
    
    private val executorService = Executors.newSingleThreadExecutor()
    private var currentTask: Future<*>? = null
    
    fun submitMiniatureCreationRequest(name: String, forceResourcePack: Boolean, min: Location,
                                       max: Location, runAfter: () -> Unit) {
        if (isBusy()) return
        
        if (currentTask?.isDone != false) {
            currentTask = executorService.submit {
                try {
                    val buildData = BuildDataCreator(min, max).createData()
                    val modelData = MiniatureModel(buildData).modelDataObj
                    MiniatureBlocks.INSTANCE.resourcePack.addNewModel(name, modelData, forceResourcePack)
                    
                    runAfter()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    fun submitTextureDownloadRequest(player: Player, name: String, url: String) {
        if (isBusy()) return
        
        if (currentTask?.isDone != false) {
            currentTask = executorService.submit {
                MiniatureBlocks.INSTANCE.resourcePack.downloadTexture(name, url)
                player.sendPrefixedMessage("§7The texture §b$name§7 has been downloaded.")
            }
        }
    }
    
    fun submitTextureRemovalRequest(player: Player, name: String) {
        if (isBusy()) return
        
        if (currentTask?.isDone != false) {
            currentTask = executorService.submit {
                MiniatureBlocks.INSTANCE.resourcePack.deleteTexture(name)
                player.sendPrefixedMessage("§7The texture §b$name§7 has been deleted successfully.")
            }
        }
    }
    
    fun isBusy(): Boolean = currentTask?.isDone == false
    
}