package de.studiocode.miniatureblocks.builderworld

import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.utils.PREFIX
import de.studiocode.miniatureblocks.utils.WorldUtils
import de.studiocode.miniatureblocks.utils.sendPrefixedMessage
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.player.PlayerMoveEvent

class BuilderWorld : Listener {

    val world: World

    init {
        val worldName = "miniatureBlocks"
        if (WorldUtils.existsWorld(worldName)) {
            world = Bukkit.getWorld(worldName)!!
        } else {
            val gen = WorldCreator(worldName)
            gen.type(WorldType.FLAT)
            gen.generateStructures(false)
            gen.generator(BuilderWorldGenerator())
            
            world = gen.createWorld()!!
            world.time = 12 * 1000
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
            world.difficulty = Difficulty.PEACEFUL
            world.spawnLocation = Location(world, 0.0, 2.0, 0.0)
        }
    }

    @EventHandler
    fun handleEntitySpawn(event: EntitySpawnEvent) {
        if (event.location.world == world) event.isCancelled = true
    }

    @EventHandler
    fun handleBlockPlace(event: BlockPlaceEvent) {
        val location = event.block.location
        if (location.isBuildWorld(this)) {
            val player = event.player
            if (location.isValidBuildArea(this)) {
                val material = event.blockPlaced.type
                if (!BlockTexture.hasMaterial(material)) {
                    event.isCancelled = true
                    player.sendPrefixedMessage("§cThis block is not supported in a miniature version. Only full blocks can be placed here.")
                }
            } else {
                event.isCancelled = true
                player.sendPrefixedMessage("§cYou can't place blocks here.")
            }
        }
    }

    @EventHandler
    fun handleBlockBreak(event: BlockBreakEvent) {
        val location = event.block.location
        if (location.isBuildWorld(this) && !location.isValidBuildArea(this)) {
            event.isCancelled = true
            event.player.sendPrefixedMessage("§cYou can't break blocks here.")
        }
    }

    @EventHandler
    fun handleMove(event: PlayerMoveEvent) {
        val from = event.from
        val to = event.to!!

        val player = event.player
        if (!from.isValidBuildArea(this) && to.isValidBuildArea(this)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("$PREFIX§bEntered build area"))
        } else if (from.isValidBuildArea(this) && !to.isValidBuildArea(this)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("$PREFIX§bLeft build area"))
        }
    }

    fun isPlayerInValidBuildArea(player: Player): Boolean {
        return player.location.isValidBuildArea(this)
    }

    fun getBuildData(player: Player): BuildData {
        return BuildData(player.location.chunk)
    }

    private fun Location.isValidBuildArea(builderWorld: BuilderWorld): Boolean {
        val chunk = this.chunk

        return world == builderWorld.world && chunk.x and 1 == 0 && chunk.z and 1 == 0 && y > 0 && y < 17 // If the least significant bit is set the number is not even.
    }

    private fun Location.isBuildWorld(builderWorld: BuilderWorld): Boolean {
        return world == builderWorld.world
    }

}