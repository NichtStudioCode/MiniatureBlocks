package de.studiocode.miniatureblocks.build.concurrent

import com.mojang.authlib.GameProfile
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.util.*
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.*
import org.bukkit.block.data.Bisected.Half
import org.bukkit.block.data.type.*
import org.bukkit.block.data.type.Slab.Type
import org.bukkit.block.data.type.Wall.Height
import org.bukkit.inventory.meta.SkullMeta
import java.util.*
import kotlin.collections.HashSet

open class AsyncBlockData(val material: Material)

open class DirectionalBlockData(material: Material, blockData: Directional) : AsyncBlockData(material) {
    val facing = blockData.facing
}

class OrientableBlockData(material: Material, blockData: Orientable) : AsyncBlockData(material) {
    val axis = blockData.axis
}

class BisectedBlockData(material: Material, blockData: Bisected) : AsyncBlockData(material) {
    val half = blockData.half
}

open class MultipleFacingBlockData(material: Material, blockData: MultipleFacing) : AsyncBlockData(material) {
    val faces = HashSet(blockData.faces)
}

class SlabBlockData(material: Material, blockData: Slab) : AsyncBlockData(material) {
    val top = blockData.type == Type.TOP
}

class StairBlockData(material: Material, blockData: Stairs) : DirectionalBlockData(material, blockData) {
    val top = blockData.half == Half.TOP
    val shape = blockData.shape
}

class TrapdoorBlockData(material: Material, blockData: TrapDoor) : DirectionalBlockData(material, blockData) {
    val top = blockData.half == Half.TOP
    val open = blockData.isOpen
}

class DoorBlockData(material: Material, blockData: Door) : DirectionalBlockData(material, blockData) {
    val top = blockData.half == Half.TOP
    val open = blockData.isOpen
    val hinge = blockData.hinge
}

class GateBlockData(material: Material, blockData: Gate) : DirectionalBlockData(material, blockData) {
    val inWall = blockData.isInWall
    val open = blockData.isOpen
}

class DaylightDetectorBlockData(material: Material, blockData: DaylightDetector) : AsyncBlockData(material) {
    val inverted = blockData.isInverted
}

class SnowBlockData(material: Material, blockData: Snow) : AsyncBlockData(material) {
    val layers = blockData.layers
    val maximumLayers = blockData.maximumLayers
}

class SnowableBlockData(material: Material, blockData: Snowable) : AsyncBlockData(material) {
    val snowy = blockData.isSnowy
}

class SwitchBlockData(material: Material, blockData: Switch) : DirectionalBlockData(material, blockData) {
    val attachedFace = blockData.attachedFace
}

class WallBlockData(material: Material, blockData: BlockData) : AsyncBlockData(material) {
    
    val up: Boolean
    val faces: Map<BlockFace, Boolean>
    
    init {
        faces = EnumMap(BlockFace::class.java)
        if (VersionUtils.isVersionOrHigher("1.16.0")) {
            val wall = blockData as Wall
            up = wall.isUp
            Direction.cardinalPoints
                .map { it.blockFace }
                .forEach {
                    val height = wall.getHeight(it)
                    if (height != Height.NONE) faces[it] = height == Height.TALL
                }
        } else {
            val fence = blockData as Fence
            up = true
            fence.faces.forEach { faces[it] = false }
        }
    }
    
}

class ChestBlockData(material: Material, blockData: Chest) : DirectionalBlockData(material, blockData) {
    val type = blockData.type
}

class CampfireBlockData(material: Material, blockData: Campfire) : DirectionalBlockData(material, blockData) {
    val lit = blockData.isLit
}

class HeadBlockData(material: Material, block: Block) : AsyncBlockData(material) {
    
    val wall: Boolean
    val facing: BlockFace
    val gameProfile: GameProfile?
    
    init {
        val blockData = block.blockData
        if (blockData is Directional) {
            wall = true
            facing = blockData.facing
        } else {
            wall = false
            facing = (blockData as Rotatable).rotation
        }
        
        gameProfile = if (material == Material.PLAYER_HEAD) {
            val item = block.drops.firstOrNull()
            if (item != null && item.hasItemMeta() && item.itemMeta is SkullMeta) {
                ReflectionRegistry.CB_CRAFT_META_SKULL_PROFILE_FIELD.get(item.itemMeta) as GameProfile
            } else null
        } else null
    }
    
}

class FluidBlockData(material: Material, block: Block) : AsyncBlockData(material) {
    
    val level = (block.blockData as Levelled).level
    val direction: Direction?
    
    init {
        val blockLocation = block.location
        val blockAbove = blockLocation.clone().advance(Direction.UP).block
        if (blockAbove.blockData !is Levelled) {
            val flowDirections = HashSet<Direction>()
            for (direction in Direction.cardinalPoints) {
                val neighborBlock = blockLocation.clone().advance(direction).block
                val neighborData = neighborBlock.blockData
                if (neighborData is Levelled) {
                    if (neighborData.level > level) {
                        flowDirections += direction
                    } else if (neighborData.level < level) {
                        flowDirections += direction.opposite
                    }
                }
            }
            
            direction = if (flowDirections.size == 1) flowDirections.first() else null
        } else direction = null
    }
    
}

fun Block.toAsyncBlockData(): AsyncBlockData {
    val material = type
    val blockData = blockData
    return when {
        material.isFluid() -> FluidBlockData(material, this)
        material.isHead() -> HeadBlockData(material, this)
        material.isWall() -> WallBlockData(material, blockData)
        blockData.isSlab() -> SlabBlockData(material, blockData as Slab)
        blockData.isSnow() -> SnowBlockData(material, blockData as Snow)
        blockData is Stairs -> StairBlockData(material, blockData)
        blockData is TrapDoor -> TrapdoorBlockData(material, blockData)
        blockData is Door -> DoorBlockData(material, blockData)
        blockData is Gate -> GateBlockData(material, blockData)
        blockData is Switch -> SwitchBlockData(material, blockData)
        blockData is DaylightDetector -> DaylightDetectorBlockData(material, blockData)
        blockData is Snowable -> SnowableBlockData(material, blockData)
        blockData is Chest -> ChestBlockData(material, blockData)
        blockData is Campfire -> CampfireBlockData(material, blockData)
        blockData is Directional -> DirectionalBlockData(material, blockData)
        blockData is Orientable -> OrientableBlockData(material, blockData)
        blockData is MultipleFacing -> MultipleFacingBlockData(material, blockData)
        blockData is Bisected -> BisectedBlockData(material, blockData)
        else -> AsyncBlockData(material)
    }
}

private fun BlockData.isSlab() = this is Slab && this.type != Type.DOUBLE

private fun BlockData.isSnow() = this is Snow && this.layers < this.maximumLayers
