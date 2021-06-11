package de.studiocode.miniatureblocks.util

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.RootCommandNode
import de.studiocode.miniatureblocks.util.ReflectionUtils.getCB
import de.studiocode.miniatureblocks.util.ReflectionUtils.getCBClass
import de.studiocode.miniatureblocks.util.ReflectionUtils.getField
import de.studiocode.miniatureblocks.util.ReflectionUtils.getMethod
import de.studiocode.miniatureblocks.util.ReflectionUtils.getNMS
import de.studiocode.miniatureblocks.util.ReflectionUtils.getNMSClass
import de.studiocode.miniatureblocks.util.VersionUtils.isVersionOrHigher
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Consumer

@Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")
object ReflectionRegistry {
    
    // NMS & CB paths
    val NMS_PACKAGE_PATH = getNMS()
    val CB_PACKAGE_PATH = getCB()
    
    // NMS classes
    val NMS_MINECRAFT_SERVER_CLASS = if (isVersionOrHigher("1.17.0")) getNMSClass("server.MinecraftServer") else getNMSClass("MinecraftServer")
    val NMS_COMMAND_DISPATCHER_CLASS = if (isVersionOrHigher("1.17.0")) getNMSClass("commands.CommandDispatcher") else getNMSClass("CommandDispatcher")
    val NMS_COMMAND_LISTENER_WRAPPER_CLASS = if (isVersionOrHigher("1.17.0")) getNMSClass("commands.CommandListenerWrapper") else getNMSClass("CommandListenerWrapper")
    val NMS_ENTITY_CLASS = if (isVersionOrHigher("1.17.0")) getNMSClass("world.entity.Entity") else getNMSClass("Entity")
    val NMS_ENTITY_ARMOR_STAND_CLASS = if (isVersionOrHigher("1.17.0")) getNMSClass("world.entity.decoration.EntityArmorStand") else getNMSClass("EntityArmorStand")
    
    // CB classes
    val CB_CRAFT_SERVER_CLASS = getCBClass("CraftServer")
    val CB_CRAFT_ENTITY_CLASS = getCBClass("entity.CraftEntity")
    val CB_CRAFT_WORLD_CLASS = getCBClass("CraftWorld")
    val CB_CRAFT_ITEM_STACK_CLASS = getCBClass("inventory.CraftItemStack")
    val CB_CRAFT_META_SKULL_CLASS = getCBClass("inventory.CraftMetaSkull")
    
    // NMS methods
    val NMS_COMMAND_DISPATCHER_GET_BRIGADIER_COMMAND_DISPATCHER_METHOD = getMethod(NMS_COMMAND_DISPATCHER_CLASS, false, "a")
    val NMS_COMMAND_LISTENER_WRAPPER_GET_ENTITY_METHOD = getMethod(NMS_COMMAND_LISTENER_WRAPPER_CLASS, false, "getEntity")
    val NMS_ENTITY_GET_BUKKIT_ENTITY_METHOD = getMethod(NMS_ENTITY_CLASS, false, "getBukkitEntity")
    
    // CB methods
    val CB_CRAFT_SERVER_GET_SERVER_METHOD = getMethod(CB_CRAFT_SERVER_CLASS, false, "getServer")
    val CB_CRAFT_SERVER_SYNC_COMMANDS_METHOD = getMethod(CB_CRAFT_SERVER_CLASS, true, "syncCommands")
    val CB_CRAFT_ENTITY_GET_HANDLE_METHOD = getMethod(CB_CRAFT_ENTITY_CLASS, false, "getHandle")
    val CB_CRAFT_WORLD_CREATE_ENTITY_METHOD = getMethod(CB_CRAFT_WORLD_CLASS, false, "createEntity", Location::class.java, Class::class.java)
    val CB_CRAFT_WORLD_ADD_ENTITY_METHOD = getMethod(CB_CRAFT_WORLD_CLASS, false, "addEntity", NMS_ENTITY_CLASS, SpawnReason::class.java, Consumer::class.java)
    val CB_CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD = getMethod(CB_CRAFT_ITEM_STACK_CLASS, false, "asNMSCopy", ItemStack::class.java)
    
    // NMS fields
    val NMS_MINECRAFT_SERVER_VANILLA_COMMAND_DISPATCHER_FIELD = getField(NMS_MINECRAFT_SERVER_CLASS, true, "vanillaCommandDispatcher")
    val NMS_ENTITY_ARMOR_STAND_ARMOR_ITEMS_FIELD = if (isVersionOrHigher("1.17.0")) getField(NMS_ENTITY_ARMOR_STAND_CLASS, true, "cd") else getField(NMS_ENTITY_ARMOR_STAND_CLASS, true, "armorItems")
    
    // CB fields
    val CB_CRAFT_META_SKULL_PROFILE_FIELD = getField(CB_CRAFT_META_SKULL_CLASS, true, "profile")
    
    // other fields
    val COMMAND_DISPATCHER_ROOT_FIELD = getField(CommandDispatcher::class.java, true, "root")
    val COMMAND_NODE_CHILDREN_FIELD = getField(CommandNode::class.java, true, "children")
    val COMMAND_NODE_LITERALS_FIELD = getField(CommandNode::class.java, true, "literals")
    val COMMAND_NODE_ARGUMENTS_FIELD = getField(CommandNode::class.java, true, "arguments")
    
    // objects
    val NMS_DEDICATED_SERVER = CB_CRAFT_SERVER_GET_SERVER_METHOD.invoke(Bukkit.getServer())!!
    val NMS_COMMAND_DISPATCHER = NMS_MINECRAFT_SERVER_VANILLA_COMMAND_DISPATCHER_FIELD.get(NMS_DEDICATED_SERVER)!!
    val COMMAND_DISPATCHER = NMS_COMMAND_DISPATCHER_GET_BRIGADIER_COMMAND_DISPATCHER_METHOD.invoke(NMS_COMMAND_DISPATCHER)!! as CommandDispatcher<Any>
    val COMMAND_DISPATCHER_ROOT_NODE = COMMAND_DISPATCHER_ROOT_FIELD.get(COMMAND_DISPATCHER)!! as RootCommandNode<Any>
    
}
