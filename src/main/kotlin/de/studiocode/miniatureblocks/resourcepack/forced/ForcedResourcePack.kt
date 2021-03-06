package de.studiocode.miniatureblocks.resourcepack.forced

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import de.studiocode.miniatureblocks.util.kickPlayerPrefix
import de.studiocode.miniatureblocks.util.runTaskLater
import de.studiocode.miniatureblocks.util.sendPrefixedMessage
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status
import org.bukkit.scheduler.BukkitTask

class ForcedResourcePack(val player: Player, private val resourcePack: ResourcePack) {
    
    private var bukkitTask: BukkitTask? = null
    
    fun force() {
        ForcedResourcePackManager.INSTANCE.addForcedResourcePack(this)
        Thread {
            player.sendPrefixedMessage("§7Uploading resource pack, please wait...")
            val url = resourcePack.downloadUrl
            if (url != null) {
                player.sendPrefixedMessage("§7Please accept the custom resource pack, you will be kicked otherwise.")
                player.setResourcePack(url, resourcePack.hash)
                
                kickLater()
            } else {
                player.sendPrefixedMessage("§cAn error occurred while uploading the resource pack.")
                if (player.isOp && !MiniatureBlocks.INSTANCE.config.hasCustomUploader()) {
                    player.sendPrefixedMessage("§cTo prevent this from happening, you should set a custom uploader in the config.yml file.")
                }
            }
        }.start()
    }
    
    fun handleResourcePackStatus(status: Status) {
        if (status == Status.DECLINED) {
            kickPlayer()
        } else {
            bukkitTask?.cancel()
            if (status == Status.FAILED_DOWNLOAD) println("${player.name} failed to download the resource pack.")
        }
    }
    
    private fun kickLater() {
        bukkitTask = runTaskLater(20 * 15, this::kickPlayer)
    }
    
    private fun kickPlayer() {
        player.kickPlayerPrefix("§cPlease accept the custom resource pack")
        
        remove()
    }
    
    private fun remove() {
        ForcedResourcePackManager.INSTANCE.removeForcedResourcePack(this)
    }
    
}