package de.studiocode.miniatureblocks.command.impl

import com.mojang.brigadier.context.CommandContext
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.command.PlayerCommand
import de.studiocode.miniatureblocks.storage.PermanentStorage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

class MiniatureWorldCommand(name: String, permission: String) : PlayerCommand(name, permission) {
    
    private val lastLocations: HashMap<UUID, Location> = PermanentStorage.retrieve(HashMap(), "lastLocations")
    
    init {
        command = command.executes { handleMiniatureWorldCommand(it); 0 }
    }
    
    private fun handleMiniatureWorldCommand(context: CommandContext<Any>) {
        val player = getPlayer(context.source)
        
        val builderWorld = MiniatureBlocks.INSTANCE.builderWorld.world
        if (builderWorld == player.location.world) {
            
            val worlds = Bukkit.getWorlds().filter { it != builderWorld }
            if (worlds.isNotEmpty()) {
                val lastLocation = lastLocations[player.uniqueId] ?: worlds[0].spawnLocation
                setLastLocation(player)
                player.teleport(lastLocation)
            }
        } else {
            val lastLocation = lastLocations[player.uniqueId] ?: builderWorld.spawnLocation
            setLastLocation(player)
            player.teleport(lastLocation)
        }
    }
    
    private fun setLastLocation(player: Player) {
        lastLocations[player.uniqueId] = player.location
        PermanentStorage.store("lastLocations", lastLocations)
    }
    
}