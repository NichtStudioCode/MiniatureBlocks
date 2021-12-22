package de.studiocode.miniatureblocks.region

import de.studiocode.invui.item.builder.ItemBuilder
import de.studiocode.miniatureblocks.util.runTaskTimer
import de.studiocode.miniatureblocks.util.sendPrefixedMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action.LEFT_CLICK_BLOCK
import org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class RegionManager(plugin: JavaPlugin) : Listener {
    
    private val marker = ItemBuilder(Material.STONE_AXE).setDisplayName("§bMiniatureBlocks Marker").get()
    val regions = HashMap<UUID, Region>()
    
    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        runTaskTimer(0, 4) { regions.values.forEach(Region::drawOutline) }
    }
    
    @EventHandler
    fun handleBlockClick(event: PlayerInteractEvent) {
        val action = event.action
        if ((action == LEFT_CLICK_BLOCK || action == RIGHT_CLICK_BLOCK)
            && marker.isSimilar(event.item)
            && event.player.hasPermission("miniatureBlocks.miniature")) {
            
            event.isCancelled = true
            markPosition(action == LEFT_CLICK_BLOCK, event.player, event.clickedBlock!!.location)
        }
    }
    
    fun markPosition(first: Boolean, player: Player, location: Location) {
        val uuid = player.uniqueId
        if (!regions.containsKey(uuid)) regions[uuid] = Region(player)
        val region = regions[uuid]!!
        if (first) {
            region.setFirst(location)
            player.sendPrefixedMessage("§7First position set.")
        } else {
            region.setSecond(location)
            player.sendPrefixedMessage("§7Second position set.")
        }
    }
    
    fun giveMarker(player: Player) {
        player.inventory.addItem(marker.clone())
    }
    
    fun hasValidRegion(player: Player) = regions[player.uniqueId]?.isValid() ?: false
    
    inline fun take(player: Player, execute: (Region) -> Unit) {
        execute.invoke(regions[player.uniqueId]!!)
        regions.remove(player.uniqueId)
    }
    
}