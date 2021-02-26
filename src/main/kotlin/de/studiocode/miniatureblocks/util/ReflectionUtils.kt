package de.studiocode.miniatureblocks.util

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.studiocode.miniatureblocks.util.ReflectionRegistry.CB_CRAFT_ENTITY_GET_HANDLE_METHOD
import de.studiocode.miniatureblocks.util.ReflectionRegistry.CB_CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD
import de.studiocode.miniatureblocks.util.ReflectionRegistry.CB_CRAFT_SERVER_SYNC_COMMANDS_METHOD
import de.studiocode.miniatureblocks.util.ReflectionRegistry.CB_CRAFT_WORLD_ADD_ENTITY_METHOD
import de.studiocode.miniatureblocks.util.ReflectionRegistry.CB_CRAFT_WORLD_CREATE_ENTITY_METHOD
import de.studiocode.miniatureblocks.util.ReflectionRegistry.CB_PACKAGE_PATH
import de.studiocode.miniatureblocks.util.ReflectionRegistry.COMMAND_DISPATCHER
import de.studiocode.miniatureblocks.util.ReflectionRegistry.COMMAND_DISPATCHER_ROOT_NODE
import de.studiocode.miniatureblocks.util.ReflectionRegistry.COMMAND_NODE_ARGUMENTS_FIELD
import de.studiocode.miniatureblocks.util.ReflectionRegistry.COMMAND_NODE_CHILDREN_FIELD
import de.studiocode.miniatureblocks.util.ReflectionRegistry.COMMAND_NODE_LITERALS_FIELD
import de.studiocode.miniatureblocks.util.ReflectionRegistry.NMS_COMMAND_LISTENER_WRAPPER_GET_ENTITY_METHOD
import de.studiocode.miniatureblocks.util.ReflectionRegistry.NMS_DEDICATED_SERVER
import de.studiocode.miniatureblocks.util.ReflectionRegistry.NMS_ENTITY_ARMOR_STAND_ARMOR_ITEMS_FIELD
import de.studiocode.miniatureblocks.util.ReflectionRegistry.NMS_ENTITY_GET_BUKKIT_ENTITY_METHOD
import de.studiocode.miniatureblocks.util.ReflectionRegistry.NMS_MINECRAFT_SERVER_GET_PLAYER_LIST_METHOD
import de.studiocode.miniatureblocks.util.ReflectionRegistry.NMS_PACKAGE_PATH
import de.studiocode.miniatureblocks.util.ReflectionRegistry.NMS_PLAYER_LIST_UPDATE_PERMISSION_LEVEL_METHOD
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

@Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")
object ReflectionUtils {
    
    fun getNMS(): String {
        val path = Bukkit.getServer().javaClass.getPackage().name
        val version = path.substring(path.lastIndexOf(".") + 1)
        return "net.minecraft.server.$version."
    }
    
    fun getCB(): String {
        val path = Bukkit.getServer().javaClass.getPackage().name
        val version = path.substring(path.lastIndexOf(".") + 1)
        return "org.bukkit.craftbukkit.$version."
    }
    
    fun getNMS(name: String): String {
        return NMS_PACKAGE_PATH + name
    }
    
    fun getCB(name: String): String {
        return CB_PACKAGE_PATH + name
    }
    
    fun getNMSClass(name: String): Class<*> {
        return Class.forName(getNMS(name))
    }
    
    fun getCBClass(name: String): Class<*> {
        return Class.forName(getCB(name))
    }
    
    fun getMethod(clazz: Class<*>, declared: Boolean, methodName: String, vararg args: Class<*>): Method {
        return if (declared) clazz.getDeclaredMethod(methodName, *args) else clazz.getMethod(methodName, *args)
    }
    
    fun getConstructor(clazz: Class<*>, declared: Boolean, vararg args: Class<*>): Constructor<*> {
        return if (declared) clazz.getDeclaredConstructor(*args) else clazz.getConstructor(*args)
    }
    
    fun getField(clazz: Class<*>, declared: Boolean, name: String): Field {
        val field = if (declared) clazz.getDeclaredField(name) else clazz.getField(name)
        if (declared) field.isAccessible = true
        return field
    }
    
    fun registerCommand(builder: LiteralArgumentBuilder<Any>) {
        COMMAND_DISPATCHER.register(builder)
    }
    
    fun syncCommands() {
        CB_CRAFT_SERVER_SYNC_COMMANDS_METHOD.invoke(Bukkit.getServer())
    }
    
    fun unregisterCommand(name: String) {
        val children = COMMAND_NODE_CHILDREN_FIELD.get(COMMAND_DISPATCHER_ROOT_NODE) as MutableMap<String, *>
        val literals = COMMAND_NODE_LITERALS_FIELD.get(COMMAND_DISPATCHER_ROOT_NODE) as MutableMap<String, *>
        val arguments = COMMAND_NODE_ARGUMENTS_FIELD.get(COMMAND_DISPATCHER_ROOT_NODE) as MutableMap<String, *>
        
        children.remove(name)
        literals.remove(name)
        arguments.remove(name)
    }
    
    fun getNMSEntity(entity: Entity): Any {
        return CB_CRAFT_ENTITY_GET_HANDLE_METHOD.invoke(entity)
    }
    
    fun getPlayerFromEntityPlayer(entityPlayer: Any): Player? {
        return Bukkit.getOnlinePlayers().find { getNMSEntity(it) == entityPlayer }
    }
    
    fun createPlayerFromEntityPlayer(entityPlayer: Any): Player {
        return createBukkitEntityFromNMSEntity(entityPlayer) as Player
    }
    
    fun getEntityFromCommandListenerWrapper(commandListenerWrapper: Any): Any? =
        NMS_COMMAND_LISTENER_WRAPPER_GET_ENTITY_METHOD.invoke(commandListenerWrapper)
    
    fun createPlayerFromCommandListenerWrapper(commandListenerWrapper: Any): Player? {
        val entity = getEntityFromCommandListenerWrapper(commandListenerWrapper)
        return if (entity != null) createPlayerFromEntityPlayer(entity) else null
    }
    
    fun getPlayerFromCommandListenerWrapper(commandListenerWrapper: Any): Player? {
        val entity = getEntityFromCommandListenerWrapper(commandListenerWrapper)
        return if (entity != null) getPlayerFromEntityPlayer(entity) else null
    }
    
    fun updatePermissionLevel(entityPlayer: Any) {
        val playerList = NMS_MINECRAFT_SERVER_GET_PLAYER_LIST_METHOD.invoke(NMS_DEDICATED_SERVER)
        NMS_PLAYER_LIST_UPDATE_PERMISSION_LEVEL_METHOD.invoke(playerList, entityPlayer)
    }
    
    fun updatePermissionLevelPlayer(player: Player) {
        updatePermissionLevel(getNMSEntity(player))
    }
    
    fun createNMSEntity(world: World, location: Location, entityType: EntityType): Any {
        return CB_CRAFT_WORLD_CREATE_ENTITY_METHOD.invoke(world, location, entityType.entityClass)
    }
    
    fun createBukkitEntityFromNMSEntity(entity: Any): Entity {
        return NMS_ENTITY_GET_BUKKIT_ENTITY_METHOD.invoke(entity) as Entity
    }
    
    fun addNMSEntityToWorld(world: World, entity: Any): Entity {
        return CB_CRAFT_WORLD_ADD_ENTITY_METHOD.invoke(world, entity, CreatureSpawnEvent.SpawnReason.CUSTOM, null) as Entity
    }
    
    fun createNMSItemStackCopy(itemStack: ItemStack): Any {
        return CB_CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD.invoke(null, itemStack)
    }
    
    fun setArmorStandArmorItems(entityArmorStand: Any, index: Int, nmsItemStack: Any) {
        (NMS_ENTITY_ARMOR_STAND_ARMOR_ITEMS_FIELD.get(entityArmorStand) as MutableList<Any>)[index] = nmsItemStack
    }
    
}