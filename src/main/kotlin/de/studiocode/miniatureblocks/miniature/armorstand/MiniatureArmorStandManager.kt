package de.studiocode.miniatureblocks.miniature.armorstand

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.miniature.Miniature
import de.studiocode.miniatureblocks.miniature.armorstand.impl.AnimatedMiniatureArmorStand
import de.studiocode.miniatureblocks.miniature.armorstand.impl.NormalMiniatureArmorStand
import de.studiocode.miniatureblocks.miniature.data.impl.AnimatedMiniatureData
import de.studiocode.miniatureblocks.miniature.data.impl.NormalMiniatureData
import de.studiocode.miniatureblocks.miniature.item.MiniatureItem
import de.studiocode.miniatureblocks.miniature.item.impl.AnimatedMiniatureItem
import de.studiocode.miniatureblocks.miniature.item.impl.NormalMiniatureItem
import de.studiocode.miniatureblocks.resourcepack.file.ModelFile.CustomModel
import de.studiocode.miniatureblocks.util.getTargetEntity
import de.studiocode.miniatureblocks.util.runTaskTimer
import de.studiocode.miniatureblocks.util.sendPrefixedMessage
import org.bukkit.*
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryCreativeEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.event.world.ChunkUnloadEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataHolder

fun ArmorStand.getMiniature(): MiniatureArmorStand? = MiniatureBlocks.INSTANCE.miniatureManager.loadedMiniatures[this]

fun PersistentDataHolder.hasMiniatureData() = Miniature.hasTypeId(this)

class MiniatureArmorStandManager(plugin: MiniatureBlocks) : Listener {
    
    val loadedMiniatures = HashMap<ArmorStand, MiniatureArmorStand>()
    
    init {
        Bukkit.getServer().pluginManager.registerEvents(this, plugin)
        runTaskTimer(0, 1, this::handleTick)
        
        Bukkit.getWorlds().forEach { it.loadedChunks.forEach(this::handleChunkLoad) }
    }
    
    private fun handleTick() {
        loadedMiniatures.values.forEach(MiniatureArmorStand::handleTick)
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun handleBlockPlace(event: BlockPlaceEvent) {
        val itemStack = event.itemInHand.clone()
        itemStack.amount = 1
        
        val itemMeta = itemStack.itemMeta!!
        if (itemMeta.hasMiniatureData()) {
            event.isCancelled = true
            
            val player = event.player
            val spawnLocation = event.blockPlaced.location
            
            val miniatureItem = MiniatureType.newInstance(itemStack)
            if (miniatureItem?.isValid() == true) {
                spawnMiniature(spawnLocation, miniatureItem)
                
                // decrease item count if player is in survival mode
                if (player.gameMode == GameMode.SURVIVAL) {
                    event.itemInHand.amount--
                }
            } else {
                player.inventory.remove(event.itemInHand)
                player.sendPrefixedMessage("§cThe miniature you tried to place was invalid and has been removed from your inventory.")
            }
        }
    }
    
    private fun spawnMiniature(location: Location, item: MiniatureItem) {
        if (item is NormalMiniatureItem) {
            val miniature = NormalMiniatureArmorStand.spawn(location, item)
            loadedMiniatures[miniature.armorStand] = miniature
        } else if (item is AnimatedMiniatureItem) {
            val miniature = AnimatedMiniatureArmorStand.spawn(location, item)
            loadedMiniatures[miniature.armorStand] = miniature
        }
    }
    
    fun removeMiniatureArmorStands(customModel: CustomModel) {
        loadedMiniatures.values
            .filter { it.containsModel(customModel) }
            .forEach { it.armorStand.remove() }
    }
    
    @EventHandler
    fun handleEntityInteract(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked
        
        if (entity is ArmorStand && entity.hasMiniatureData())
            entity.getMiniature()?.handleEntityInteract(event)
    }
    
    @EventHandler
    fun handleEntityDamage(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        if (entity is ArmorStand && entity.hasMiniatureData()) {
            val miniature = entity.getMiniature()
            if (miniature != null) {
                event.isCancelled = true
                val location = entity.location
                val damager = event.damager
                if (damager !is Player || (damager.gameMode == GameMode.SURVIVAL || damager.gameMode == GameMode.ADVENTURE)) {
                    val item = if (miniature is NormalMiniatureArmorStand) NormalMiniatureItem.create(
                        NormalMiniatureData(miniature)
                    )
                    else AnimatedMiniatureItem.create(AnimatedMiniatureData(miniature as AnimatedMiniatureArmorStand))
                    
                    location.world!!.dropItem(location, item.itemStack)
                }
                entity.remove()
            }
        }
    }
    
    @EventHandler
    fun handleChunkLoad(event: ChunkLoadEvent) = handleChunkLoad(event.chunk)
    
    private fun handleChunkLoad(chunk: Chunk) {
        chunk.entities
            .filterValidCoordinates()
            .filterIsInstance<ArmorStand>()
            .filter { it.hasMiniatureData() }
            .forEach { armorStand ->
                val miniature = MiniatureType.newInstance(armorStand)!!
                
                if (miniature.isValid()) {
                    //  put it into map
                    loadedMiniatures[armorStand] = miniature
                } else {
                    // remove armor stand if this miniature model does no longer exist
                    armorStand.remove()
                }
            }
    }
    
    @EventHandler
    fun handleChunkUnload(event: ChunkUnloadEvent) {
        // remove miniature from map when the chunk gets unloaded
        event.chunk.entities
            .filterIsInstance<ArmorStand>()
            .forEach { loadedMiniatures.remove(it) }
    }
    
    @EventHandler
    fun handleMiniatureClone(event: InventoryCreativeEvent) {
        val cloned = event.cursor
        if (cloned.type == Material.ARMOR_STAND) {
            
            val player = event.whoClicked as Player
            val entity = player.getTargetEntity(8.0)
            
            if (entity != null && entity is ArmorStand) {
                val miniature = entity.getMiniature()
                if (miniature != null) {
                    val item = if (miniature is NormalMiniatureArmorStand) NormalMiniatureItem.create(
                        NormalMiniatureData(miniature)
                    )
                    else AnimatedMiniatureItem.create(AnimatedMiniatureData(miniature as AnimatedMiniatureArmorStand))
                    
                    event.cursor = item.itemStack
                }
            }
        }
    }
    
    enum class MiniatureType(
        val id: Byte,
        armorStandClass: Class<out MiniatureArmorStand>,
        itemClass: Class<out MiniatureItem>
    ) {
        
        NORMAL(0, NormalMiniatureArmorStand::class.java, NormalMiniatureItem::class.java),
        ANIMATED(1, AnimatedMiniatureArmorStand::class.java, AnimatedMiniatureItem::class.java);
        
        private val armorStandConstructor = armorStandClass.getConstructor(ArmorStand::class.java)
        private val itemConstructor = itemClass.getConstructor(ItemStack::class.java)
        
        private fun newInstance(armorStand: ArmorStand): MiniatureArmorStand {
            return armorStandConstructor.newInstance(armorStand)
        }
        
        private fun newInstance(itemStack: ItemStack): MiniatureItem {
            return itemConstructor.newInstance(itemStack)
        }
        
        companion object {
            
            fun newInstance(armorStand: ArmorStand): MiniatureArmorStand? {
                val id = Miniature.getTypeId(armorStand)
                return if (id != null) getById(id).newInstance(armorStand) else null
            }
            
            fun newInstance(itemStack: ItemStack): MiniatureItem? {
                val id = Miniature.getTypeId(itemStack.itemMeta!!)
                return if (id != null) getById(id).newInstance(itemStack) else null
            }
            
            private fun getById(id: Byte) = values().first { it.id == id }
            
        }
        
    }
    
    private fun Array<Entity>.filterValidCoordinates() = filter { it.location.y < 500 }
    
}