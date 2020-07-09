package de.studiocode.miniatureblocks.miniature

import de.studiocode.miniatureblocks.MiniatureBlocks
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.inventory.ItemStack

class MiniatureManager : Listener {

    //TODO: add an option to turn on auto-rotation for miniatures

    init {
        Bukkit.getServer().pluginManager.registerEvents(this, MiniatureBlocks.INSTANCE)
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun handleBlockPlace(event: BlockPlaceEvent) {
        val itemStack = event.itemInHand.clone()
        itemStack.amount = 1
        if (itemStack.isMiniature()) {
            event.isCancelled = true
            spawnArmorStandMiniature(event.blockPlaced.location, itemStack)
            
            val player = event.player

            if (player.gameMode == GameMode.SURVIVAL) {
                event.itemInHand.amount--
            }
        }
    }

    @EventHandler
    fun handleEntityInteract(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked
        if (entity is ArmorStand && entity.isMiniature()) {
            event.isCancelled = true
            val location = entity.location
            location.yaw += 45
            entity.teleport(location)
        }
    }

    @EventHandler
    fun handleEntityDamage(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        if (entity is ArmorStand && entity.isMiniature()) {
            event.isCancelled = true
            val location = entity.location
            val damager = event.damager
            if (damager !is Player || (damager.gameMode == GameMode.SURVIVAL || damager.gameMode == GameMode.ADVENTURE)) {
                location.world!!.dropItem(location, entity.equipment!!.helmet!!)
            }

            entity.remove()
        }
    }

    private fun spawnArmorStandMiniature(location: Location, itemStack: ItemStack) {
        location.add(0.5, 0.0, 0.5)
        val armorStand = location.world!!.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand
        armorStand.equipment?.helmet = itemStack
        armorStand.isVisible = false
        armorStand.isCollidable = false
        armorStand.setGravity(false)
    }

    fun removeMiniatureArmorStands(itemStack: ItemStack) {
        for (world in Bukkit.getWorlds()) {
            for (armorStand in world.entities.filterIsInstance<ArmorStand>()) {
                if (armorStand.isMiniature(itemStack)) armorStand.remove()
            }
        }
    }

    @EventHandler
    fun handleChunkLoad(event: ChunkLoadEvent) {
        for (armorStand in event.chunk.entities.filterIsInstance<ArmorStand>()) {
            if (armorStand.isMiniature()) {
                val customModelData = armorStand.equipment!!.helmet!!.itemMeta!!.customModelData
                if (!MiniatureBlocks.INSTANCE.resourcePack.hasModelData(customModelData)) {
                    armorStand.remove()
                }
            }
        }
    }

    private fun ArmorStand.isMiniature(): Boolean {
        return equipment?.helmet != null && equipment?.helmet?.isMiniature() ?: false
    }

    private fun ArmorStand.isMiniature(itemStack: ItemStack): Boolean {
        return equipment?.helmet != null && equipment?.helmet == itemStack
    }

    private fun ItemStack.isMiniature(): Boolean {
        return type == Material.JACK_O_LANTERN && itemMeta?.hasCustomModelData()!! && itemMeta?.customModelData!! > 1000000
    }

}