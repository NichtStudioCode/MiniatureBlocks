package de.studiocode.miniatureblocks.miniature.armorstand

import de.studiocode.inventoryaccess.util.VersionUtils
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
import de.studiocode.miniatureblocks.util.getTargetMiniature
import de.studiocode.miniatureblocks.util.runTaskLater
import de.studiocode.miniatureblocks.util.runTaskTimer
import de.studiocode.miniatureblocks.util.sendPrefixedMessage
import org.bukkit.*
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.event.world.ChunkUnloadEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataHolder

fun ArmorStand.getMiniature(): MiniatureArmorStand? = MiniatureBlocks.INSTANCE.miniatureManager.loadedMiniatures[this]

fun PersistentDataHolder.hasMiniatureData() = Miniature.hasTypeId(this)

fun PlayerInteractEvent.isCompletelyDenied() = useInteractedBlock() == Event.Result.DENY && useItemInHand() == Event.Result.DENY

fun Action.isRightClick() =
    this == Action.RIGHT_CLICK_AIR
        || this == Action.RIGHT_CLICK_BLOCK

fun Action.isClick() =
    this == Action.LEFT_CLICK_BLOCK
        || this == Action.LEFT_CLICK_AIR
        || this == Action.RIGHT_CLICK_BLOCK
        || this == Action.RIGHT_CLICK_AIR

class MiniatureManager(plugin: MiniatureBlocks) : Listener {
    
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
    fun handlePrepareCraft(event: PrepareItemCraftEvent) {
        if (event.recipe != null && event.inventory.contents.any { it.itemMeta?.hasMiniatureData() == true })
            event.inventory.result = ItemStack(Material.AIR)
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
    
    @EventHandler(priority = EventPriority.HIGHEST)
    fun handleInteract(event: PlayerInteractEvent) {
        if (!event.isCompletelyDenied() && event.hand != null && event.hand == EquipmentSlot.HAND) {
            val player = event.player
            val action = event.action
            if (action.isClick()) {
                val miniature = player.getTargetMiniature()
                if (miniature != null) {
                    event.isCancelled = true
                    if (action.isRightClick()) miniature.handleEntityInteract(player)
                    else handleMiniatureBreak(miniature, player)
                }
            }
        }
    }
    
    private fun handleMiniatureBreak(miniature: MiniatureArmorStand, player: Player) {
        val entity = miniature.armorStand
        val location = entity.location
        
        // check if player is allowed to break things here
        val breakEvent = BlockBreakEvent(location.block, player)
        Bukkit.getServer().pluginManager.callEvent(breakEvent)
        if (!breakEvent.isCancelled) {
            if (player.gameMode != GameMode.CREATIVE) {
                val item = if (miniature is NormalMiniatureArmorStand) NormalMiniatureItem.create(NormalMiniatureData(miniature))
                else AnimatedMiniatureItem.create(AnimatedMiniatureData(miniature as AnimatedMiniatureArmorStand))
                
                location.world!!.dropItem(location, item.itemStack)
            }
            entity.remove()
        }
    }
    
    @EventHandler
    fun handleChunkLoad(event: ChunkLoadEvent) = handleChunkLoad(event.chunk)
    
    private fun handleChunkLoad(chunk: Chunk) {
        if (VersionUtils.isServerHigherOrEqual("1.17")) {
            // Workaround async entity loading (https://hub.spigotmc.org/jira/browse/SPIGOT-6547)
            runTaskLater(20 * 15) { loadArmorStandsInChunk(chunk) }
        } else loadArmorStandsInChunk(chunk)
    }
    
    private fun loadArmorStandsInChunk(chunk: Chunk) {
        if (chunk.isLoaded) {
            chunk.entities
                .filterValidCoordinates()
                .filterIsInstance<ArmorStand>()
                .filter { it.hasMiniatureData() }
                .forEach { armorStand ->
                    val miniature = MiniatureType.newInstance(armorStand)!!
                    
                    if (miniature.isValid()) {
                        // put it into map
                        loadedMiniatures[armorStand] = miniature
                        armorStand.isMarker = true // set version < 0.10 armor stands to marker
                    } else {
                        // remove armor stand if this miniature model does no longer exist
                        armorStand.remove()
                    }
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