package de.studiocode.miniatureblocks.builderworld

import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.utils.WorldUtils
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
            val gen = WorldCreator(worldName).generator(BuilderWorldGenerator())
            world = gen.createWorld()!!
            world.time = 12 * 1000
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
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
                    player.sendMessage("§cThis block is not supported in a miniature version. Only full blocks can be placed here.")
                }
            } else {
                event.isCancelled = true
                player.sendMessage("§cYou can't place blocks here.")
            }
        }
    }

    @EventHandler
    fun handleBlockBreak(event: BlockBreakEvent) {
        val location = event.block.location
        if (location.isBuildWorld(this) && !location.isValidBuildArea(this)) {
            event.isCancelled = true
            event.player.sendMessage("§cYou can't break blocks here.")
        }
    }

    @EventHandler
    fun handleMove(event: PlayerMoveEvent) {
        val from = event.from
        val to = event.to!!

        val player = event.player
        if (!from.isValidBuildArea(this) && to.isValidBuildArea(this)) {
            player.sendTitle("Entering build area", to.getBuildAreaId(this), 5, 15, 5)
        } else if (from.isValidBuildArea(this) && !to.isValidBuildArea(this)) {
            player.sendTitle("Leaving build area", from.getBuildAreaId(this), 5, 15, 5)
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

        return world == builderWorld.world && chunk.x % 2 == 0 && chunk.z % 2 == 0 && y > 0 && y < 17
    }

    private fun Location.isBuildWorld(builderWorld: BuilderWorld): Boolean {
        return world == builderWorld.world
    }

    private fun Location.getBuildAreaId(builderWorld: BuilderWorld): String {
        if (isValidBuildArea(builderWorld)) {
            val chunk = this.chunk
            return StringBuilder().append(chunk.x).append(" / ").append(chunk.z).toString()
        }
        return "invalid"
    }

}