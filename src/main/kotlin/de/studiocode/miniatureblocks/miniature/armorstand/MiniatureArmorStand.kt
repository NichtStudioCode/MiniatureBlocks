package de.studiocode.miniatureblocks.miniature.armorstand

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.miniature.Miniature
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStandManager.MiniatureType
import de.studiocode.miniatureblocks.miniature.data.types.FloatArrayDataType
import de.studiocode.miniatureblocks.resourcepack.file.ModelFile.CustomModel
import de.studiocode.miniatureblocks.util.ReflectionUtils
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.persistence.PersistentDataType.BYTE
import kotlin.math.max
import kotlin.math.min

abstract class MiniatureArmorStand(val armorStand: ArmorStand) : Miniature(armorStand) {
    
    companion object {
        
        private val PLUGIN = MiniatureBlocks.INSTANCE
        val ROTATION_KEY = NamespacedKey(PLUGIN, "rotation")
        val NO_ROTATE_KEY = NamespacedKey(PLUGIN, "noRotate")
        val BOUNCE_KEY = NamespacedKey(PLUGIN, "bounce")
        
        fun spawnArmorStandSilently(
            location: Location, itemStack: ItemStack, miniatureType: MiniatureType,
            setData: (PersistentDataContainer) -> Unit
        ): ArmorStand {
            location.add(0.5, 0.0, 0.5)
            val world = location.world!!
            
            // create EntityArmorStand
            val nmsArmorStand = ReflectionUtils.createNMSEntity(world, location, EntityType.ARMOR_STAND)
            
            // set head item silently
            ReflectionUtils.setArmorStandArmorItems(nmsArmorStand, 3, ReflectionUtils.createNMSItemStackCopy(itemStack))
            
            // get CraftArmorStand
            val armorStand = ReflectionUtils.createBukkitEntityFromNMSEntity(nmsArmorStand) as ArmorStand
            
            // set other properties & nbt tags
            armorStand.isVisible = false
            armorStand.isMarker = true
            armorStand.setGravity(false)
            
            // set miniature type
            val dataContainer = armorStand.persistentDataContainer
            dataContainer.set(TYPE_ID_KEY, BYTE, miniatureType.id)
            
            // set other data
            setData.invoke(dataContainer)
            
            // add ArmorStand to world
            ReflectionUtils.addNMSEntityToWorld(world, nmsArmorStand)
            
            return armorStand
        }
    }
    
    private val commands = HashMap<CommandType, String>()
    
    // rotation
    private var noRotate = false
    private var degreesPerTick = 0f
    
    // bounce
    private var defaultHeight = armorStand.location.y.toFloat()
    private var maxHeight = 0f
    private var bounceSpeed = 0f
    private var heightModifier = 0f
    private var bounceUp = true
    
    init {
        degreesPerTick = dataContainer.get(ROTATION_KEY, PersistentDataType.FLOAT) ?: 0.0f
        noRotate = (dataContainer.get(NO_ROTATE_KEY, BYTE) ?: 0.toByte()) == 1.toByte()
        
        val bounceData = dataContainer.get(BOUNCE_KEY, FloatArrayDataType)
        if (bounceData != null) {
            defaultHeight = bounceData[0]
            maxHeight = bounceData[1]
            bounceSpeed = bounceData[2]
        }
        
        for (commandType in CommandType.values()) {
            val command = dataContainer.get(commandType.namespacedKey, PersistentDataType.STRING)
            if (command != null) commands[commandType] = command
        }
    }
    
    open fun handleTick() {
        if (degreesPerTick != 0f || bounceSpeed != 0f) handleAutoMovement()
    }
    
    private fun handleAutoMovement() {
        val location = armorStand.location
        
        location.yaw += if (degreesPerTick != 0f) degreesPerTick else 0f
        
        if (bounceSpeed != 0f && maxHeight != 0f) {
            val distance = min(maxHeight - heightModifier, heightModifier)
            val bounceModifier = max(distance / maxHeight, 0.1f)
            val bounce = bounceModifier * bounceSpeed
            
            if (bounceUp) heightModifier += bounce
            else heightModifier -= bounce
            if ((heightModifier < 0 && !bounceUp) || (heightModifier > maxHeight && bounceUp)) bounceUp = !bounceUp
            
            location.y = (defaultHeight + heightModifier).toDouble()
        }
        
        armorStand.teleport(location)
    }
    
    fun handleEntityInteract(player: Player) {
        val commandType = CommandType.getCommandType(player.isSneaking)
        val command = commands[commandType]
        if (command != null) {
            player.chat(command)
        } else if (degreesPerTick == 0f && !noRotate) {
            rotate(30f, true)
        }
    }
    
    fun rotate(rotation: Float, add: Boolean = false) {
        val location = armorStand.location
        if (add) location.yaw += rotation else location.yaw = rotation
        armorStand.teleport(location)
    }
    
    fun setAutoRotate(rotation: Float) {
        if (rotation != 0f) {
            dataContainer.set(ROTATION_KEY, PersistentDataType.FLOAT, rotation)
            degreesPerTick = rotation
        } else {
            dataContainer.remove(ROTATION_KEY)
            degreesPerTick = 0f
            armorStand.teleport(armorStand.location.also { it.yaw = 0f })
        }
    }
    
    fun resetMovement() {
        if (degreesPerTick != 0f) rotate(0f)
        heightModifier = 0f
    }
    
    fun setBounce(maxHeight: Float, speed: Float) {
        if (maxHeight != 0f && speed != 0f) {
            val bounceData = floatArrayOf(defaultHeight, maxHeight, speed)
            dataContainer.set(BOUNCE_KEY, FloatArrayDataType, bounceData)
            
            this.maxHeight = maxHeight
            bounceSpeed = speed
        } else {
            dataContainer.remove(BOUNCE_KEY)
            this.maxHeight = 0f
            bounceSpeed = 0f
            heightModifier = 0f
            armorStand.teleport(armorStand.location.also { it.y = defaultHeight.toDouble() })
        }
    }
    
    fun setCommand(commandType: CommandType, command: String) {
        dataContainer.set(commandType.namespacedKey, PersistentDataType.STRING, command)
        commands[commandType] = command
    }
    
    fun removeCommand(commandType: CommandType) {
        dataContainer.remove(commandType.namespacedKey)
        commands.remove(commandType)
    }
    
    fun setNoRotate(noRotate: Boolean) {
        val byte = if (noRotate) 1.toByte() else 0.toByte()
        dataContainer.set(NO_ROTATE_KEY, BYTE, byte)
        
        this.noRotate = noRotate
    }
    
    override fun isValid() = getTypeId() != null
    
    abstract fun containsModel(model: CustomModel): Boolean
    
    enum class CommandType(val namespacedKey: NamespacedKey) {
        
        SHIFT_RIGHT(NamespacedKey(MiniatureBlocks.INSTANCE, "shiftRightCommand")),
        RIGHT(NamespacedKey(MiniatureBlocks.INSTANCE, "rightCommand"));
        
        companion object {
            
            fun getCommandType(sneaking: Boolean): CommandType {
                return if (sneaking) SHIFT_RIGHT else RIGHT
            }
            
        }
        
    }
    
}