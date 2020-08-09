package de.studiocode.miniatureblocks.miniature.armorstand

import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.miniature.Miniature
import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStandManager.MiniatureType
import de.studiocode.miniatureblocks.resourcepack.model.MainModelData.CustomModel
import de.studiocode.miniatureblocks.utils.ReflectionUtils
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.persistence.PersistentDataType.BYTE

abstract class MiniatureArmorStand(val armorStand: ArmorStand) : Miniature(armorStand) {

    companion object {

        private val PLUGIN = MiniatureBlocks.INSTANCE
        val ROTATION_KEY = NamespacedKey(PLUGIN, "rotation")
        val NO_ROTATE_KEY = NamespacedKey(PLUGIN, "noRotate")

        fun spawnArmorStandSilently(location: Location, itemStack: ItemStack, miniatureType: MiniatureType,
                                    setData: (PersistentDataContainer) -> Unit): ArmorStand {
            location.add(0.5, 0.0, 0.5)
            val world = location.world!!

            // create EntityArmorStand
            val nmsArmorStand = ReflectionUtils.createNMSEntity(world, location, EntityType.ARMOR_STAND)

            // set head item silently
            ReflectionUtils.setArmorStandArmorItems(nmsArmorStand, 3, ReflectionUtils.createNMSItemStackCopy(itemStack))

            // get CraftArmorStand
            val armorStand = ReflectionUtils.getBukkitEntityFromNMSEntity(nmsArmorStand) as ArmorStand

            // set other properties & nbt tags
            armorStand.isVisible = false
            armorStand.isCollidable = false
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
    private var degreesPerTick = 0f
    private var noRotate = false

    init {
        loadData()
    }

    private fun loadData() {
        degreesPerTick = dataContainer.get(ROTATION_KEY, PersistentDataType.FLOAT) ?: 0.0f
        noRotate = (dataContainer.get(NO_ROTATE_KEY, BYTE) ?: 0.toByte()) == 1.toByte()

        for (commandType in CommandType.values()) {
            val command = dataContainer.get(commandType.namespacedKey, PersistentDataType.STRING)
            if (command != null) commands[commandType] = command
        }
    }
    
    open fun handleTick() {
        handleAutoRotation()
    }

    private fun handleAutoRotation() {
        if (degreesPerTick != 0f) {
            val location = armorStand.location
            location.yaw += degreesPerTick
            armorStand.teleport(location)
        }
    }

    fun handleEntityInteract(event: PlayerInteractAtEntityEvent) {
        event.isCancelled = true

        val commandType = CommandType.getCommandType(event)
        val command = commands[commandType]
        if (command != null) {
            event.player.chat(command)
        } else if (degreesPerTick == 0f && !noRotate) {
            rotate(45f, true)
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
    
    fun resetAutoRotate() {
        if (degreesPerTick != 0f) rotate(0f)
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

            fun getCommandType(event: PlayerInteractAtEntityEvent): CommandType {
                return if (event.player.isSneaking) SHIFT_RIGHT else RIGHT
            }

        }

    }
    
}