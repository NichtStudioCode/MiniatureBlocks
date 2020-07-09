package de.studiocode.miniatureblocks.resourcepack.forced

import de.studiocode.miniatureblocks.MiniatureBlocks
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerResourcePackStatusEvent

class ForcedResourcePackManager private constructor() : Listener {

    companion object {
        val INSTANCE = ForcedResourcePackManager()
    }

    private val forcedResourcePacks = ArrayList<ForcedResourcePack>()

    init {
        Bukkit.getPluginManager().registerEvents(this, MiniatureBlocks.INSTANCE)
    }

    fun addForcedResourcePack(forcedResourcePack: ForcedResourcePack) {
        forcedResourcePacks.add(forcedResourcePack)
    }

    fun removeForcedResourcePack(forcedResourcePack: ForcedResourcePack) {
        forcedResourcePacks.remove(forcedResourcePack)
    }

    @EventHandler
    fun handleResourcePackStatus(event: PlayerResourcePackStatusEvent) {
        forcedResourcePacks.filter { it.player == event.player }.forEach { it.handleResourcePackStatus(event.status) }
    }

}