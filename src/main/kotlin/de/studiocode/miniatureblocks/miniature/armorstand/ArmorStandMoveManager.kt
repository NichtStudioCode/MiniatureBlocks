package de.studiocode.miniatureblocks.miniature.armorstand

import de.studiocode.miniatureblocks.MiniatureBlocks
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerMoveEvent
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.roundToInt

object ArmorStandMoveManager : Listener {

    private val armorStands = ConcurrentHashMap<Player, MoveArmorStand>()

    init {
        Bukkit.getServer().pluginManager.registerEvents(this, MiniatureBlocks.INSTANCE)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(MiniatureBlocks.INSTANCE, this::handleTick, 0, 1)
    }

    fun addToMove(player: Player, armorStand: ArmorStand) {
        armorStands[player] = MoveArmorStand(armorStand, player.eyeLocation.distance(armorStand.location).roundToInt().toDouble())
    }

    private fun handleTick() {
        for ((player, moveArmorStand) in armorStands) {
            val armorStand = moveArmorStand.armorStand
            if (!player.isValid || !armorStand.isValid) {
                armorStands.remove(player)
            }
        }
    }

    @EventHandler
    fun handleMoveEvent(event: PlayerMoveEvent) {
        val player = event.player
        if (armorStands.containsKey(player)) {
            val moveArmorStand = armorStands[player]!!
            updateArmorStandLocation(player, moveArmorStand)
        }
    }
    
    private fun updateArmorStandLocation(player: Player, moveArmorStand: MoveArmorStand) {
        val armorStand = moveArmorStand.armorStand
        val location = player.eyeLocation.clone()
        location.add(location.direction.multiply(moveArmorStand.distance))
        location.yaw = armorStand.location.yaw
        location.pitch = armorStand.location.pitch
        armorStand.teleport(location)
    }

    @EventHandler
    fun handleSlotChange(event: PlayerItemHeldEvent) {
        val player = event.player
        if (armorStands.containsKey(player)) {
            val moveArmorStand = armorStands[player]!!
            moveArmorStand.distance += getSlotDistance(event.previousSlot, event.newSlot) * 0.25
            if (moveArmorStand.distance < 0) moveArmorStand.distance = 0.0
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent("§7Distance: §b${moveArmorStand.distance} §7Right-click to stop moving"))
            updateArmorStandLocation(player, moveArmorStand)
        }
    }

    @EventHandler
    fun handleInteract(event: PlayerInteractEvent) {
        val action = event.action
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            event.isCancelled = handleRightClick(event.player)
        }
    }
    
    @EventHandler
    fun handleEntityInteract(event: PlayerInteractAtEntityEvent) {
        event.isCancelled = handleRightClick(event.player)
    }
    
    private fun handleRightClick(player: Player): Boolean {
        if (armorStands.containsKey(player)) {
            armorStands.remove(player)
            return true
        }
        return false
    }
    
    private fun getSlotDistance(previousSlot: Int, newSlot: Int): Int {
        if (previousSlot == 0 && newSlot == 8) return 0
        if (previousSlot == 8 && newSlot == 0) return -1
        
        return previousSlot - newSlot
    }


    class MoveArmorStand(val armorStand: ArmorStand, var distance: Double)

}