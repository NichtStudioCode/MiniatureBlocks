package de.studiocode.miniatureblocks.miniature.armorstand.impl

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStand
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureManager.MiniatureType.ANIMATED
import de.studiocode.miniatureblocks.miniature.data.types.AnimatedMiniatureDataType
import de.studiocode.miniatureblocks.miniature.item.impl.AnimatedMiniatureItem
import de.studiocode.miniatureblocks.resourcepack.file.ModelFile.CustomModel
import de.studiocode.miniatureblocks.util.ReflectionUtils
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.ArmorStand

class AnimatedMiniatureArmorStand(armorStand: ArmorStand) : MiniatureArmorStand(armorStand) {
    
    companion object {
        
        private val PLUGIN = MiniatureBlocks.INSTANCE
        private val DATA_KEY = NamespacedKey(PLUGIN, "animatedMiniatureData")
        
        fun spawn(location: Location, item: AnimatedMiniatureItem): AnimatedMiniatureArmorStand {
            if (item.isValid()) {
                val armorStand = spawnArmorStandSilently(location, item.itemStack, ANIMATED)
                { it.set(DATA_KEY, AnimatedMiniatureDataType, item.data!!) }
                
                return AnimatedMiniatureArmorStand(armorStand)
            } else throw IllegalArgumentException("Invalid miniature data")
        }
        
    }
    
    val tickDelay: Int
    val models: Array<out CustomModel>?
    private val nmsItemStacks = ArrayList<Any>()
    private val nmsArmorStand: Any = ReflectionUtils.getNMSEntity(armorStand)
    
    private var ticksPassed = 0
    private var currentIndex = 0
    
    init {
        val data = dataContainer.get(DATA_KEY, AnimatedMiniatureDataType)
        if (data != null && data.isValid()) {
            tickDelay = data.tickDelay
            models = data.models
            
            generateItemStacks()
        } else {
            tickDelay = -1
            models = null
        }
    }
    
    private fun generateItemStacks() {
        for (model in models!!)
            nmsItemStacks.add(ReflectionUtils.createNMSItemStackCopy(model.createItemBuilder().build()))
    }
    
    override fun handleTick() {
        super.handleTick()
        
        ticksPassed++
        if (ticksPassed == tickDelay) {
            ticksPassed = 0
            if (nmsItemStacks.size == currentIndex) currentIndex = 0
            ReflectionUtils.setArmorStandArmorItems(nmsArmorStand, 3, nmsItemStacks[currentIndex])
            currentIndex++
        }
    }
    
    fun resetAnimationState() {
        ticksPassed = 0
        currentIndex = 0
    }
    
    override fun isValid() = super.isValid() && tickDelay > 0 && models != null
    
    override fun containsModel(model: CustomModel) = models?.contains(model) ?: false
    
}