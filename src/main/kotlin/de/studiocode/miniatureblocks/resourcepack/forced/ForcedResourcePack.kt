package de.studiocode.miniatureblocks.resourcepack.forced

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.resourcepack.ResourcePack
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status
import org.bukkit.scheduler.BukkitTask

class ForcedResourcePack(val player: Player, private val resourcePack: ResourcePack) {

    private var bukkitTask: BukkitTask? = null

    fun force() {
        ForcedResourcePackManager.INSTANCE.addForcedResourcePack(this)
        Thread {
            player.sendMessage("§7Uploading resource pack, please wait...")
            val url = resourcePack.upload()
            if (url != null) {
                player.sendMessage("§7Please accept the custom resource pack, you will be kicked otherwise")
                player.setResourcePack(url)

                kickLater()
            } else {
                player.sendMessage("§cAn error occurred while uploading the resource pack.")
            }
        }.start()
    }

    fun handleResourcePackStatus(status: Status) {
        if (status == Status.DECLINED) {
            kickPlayer()
        } else bukkitTask?.cancel()
    }

    private fun kickLater() {
        bukkitTask = Bukkit.getScheduler().runTaskLater(MiniatureBlocks.INSTANCE, this::kickPlayer, 20 * 5)
    }

    private fun kickPlayer() {
        player.kickPlayer("§cPlease accept the custom resource pack")

        remove()
    }

    private fun remove() {
        ForcedResourcePackManager.INSTANCE.removeForcedResourcePack(this)
    }

}