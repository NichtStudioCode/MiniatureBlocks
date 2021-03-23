package de.studiocode.miniatureblocks.build.concurrent

import com.mojang.authlib.GameProfile
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.util.*
import org.bukkit.Axis
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

interface AsyncData {
    val material: Material
}

interface AsyncTwoState {
    val state: Boolean
}

interface AsyncDirectional {
    val facing: BlockFace
}

interface AsyncRotatable {
    val rotation: BlockFace
}

interface AsyncOrientable {
    val axis: Axis
}

interface AsyncBisected : AsyncTwoState {
    val half: Half
}

interface AsyncMultipleFacing {
    val faces: HashSet<BlockFace>
}

interface AsyncLightable : AsyncTwoState {
    val lit: Boolean
}

open class AsyncBlockData(override val material: Material) : AsyncData

class AsyncDirectionalBlockData(material: Material, blockData: Directional) : AsyncBlockData(material), AsyncDirectional {
    override val facing = blockData.facing
}

class AsyncRotatableBlockData(material: Material, blockData: Rotatable) : AsyncBlockData(material), AsyncRotatable {
    override val rotation = blockData.rotation
}

class AsyncOrientableBlockData(material: Material, blockData: Orientable) : AsyncBlockData(material), AsyncOrientable {
    override val axis = blockData.axis
}

class AsyncBisectedBlockData(material: Material, blockData: Bisected) : AsyncBlockData(material), AsyncBisected {
    override val half = blockData.half
    override val state = half == Half.TOP
}

class AsyncMultipleFacingBlockData(material: Material, blockData: MultipleFacing) : AsyncBlockData(material), AsyncMultipleFacing {
    override val faces = HashSet(blockData.faces)
}

class AsyncLightableBlockData(material: Material, lightable: Lightable) : AsyncBlockData(material), AsyncTwoState {
    override val state = lightable.isLit
    val lit = state
}

class AsyncSlab(material: Material, blockData: Slab) : AsyncBlockData(material) {
    val top = blockData.type == Type.TOP
}

class AsyncStairs(material: Material, blockData: Stairs) : AsyncBlockData(material), AsyncDirectional, AsyncBisected {
    override val half = blockData.half
    override val facing = blockData.facing
    override val state = blockData.half == Half.TOP
    val top = half == Half.TOP
    val shape = blockData.shape
}

class AsyncTrapDoor(material: Material, blockData: TrapDoor) : AsyncBlockData(material), AsyncDirectional, AsyncBisected {
    override val facing = blockData.facing
    override val half = blockData.half
    override val state = half == Half.TOP
    val top = state
    val open = blockData.isOpen
}

class AsyncDoor(material: Material, blockData: Door) : AsyncBlockData(material), AsyncDirectional, AsyncBisected {
    override val facing = blockData.facing
    override val half = blockData.half
    override val state = half == Half.TOP
    val top = state
    val open = blockData.isOpen
    val hinge = blockData.hinge
}

class AsyncGate(material: Material, blockData: Gate) : AsyncBlockData(material), AsyncDirectional {
    override val facing = blockData.facing
    val inWall = blockData.isInWall
    val open = blockData.isOpen
}

class AsyncDaylightDetector(material: Material, blockData: DaylightDetector) : AsyncBlockData(material), AsyncTwoState {
    override val state = blockData.isInverted
    val inverted = state
}

class AsyncSnow(material: Material, blockData: Snow) : AsyncBlockData(material) {
    val layers = blockData.layers
    val maximumLayers = blockData.maximumLayers
}

class AsyncSnowable(material: Material, blockData: Snowable) : AsyncBlockData(material), AsyncTwoState {
    val snowy = blockData.isSnowy
    override val state = snowy
}

class AsyncSwitch(material: Material, blockData: Switch) : AsyncBlockData(material), AsyncDirectional, AsyncTwoState {
    override val facing = blockData.facing
    override val state = blockData.isPowered
    val attachedFace = blockData.attachedFace
    val powered = blockData.isPowered
}

class AsyncChest(material: Material, blockData: Chest) : AsyncBlockData(material), AsyncDirectional {
    override val facing = blockData.facing
    val type = blockData.type
}

class AsyncCampfire(material: Material, blockData: Campfire) : AsyncBlockData(material), AsyncDirectional, AsyncLightable {
    override val facing = blockData.facing
    override val lit = blockData.isLit
    override val state = lit
}

class AsyncRedstoneWallTorch(material: Material, blockData: RedstoneWallTorch) : AsyncBlockData(material), AsyncDirectional, AsyncLightable {
    override val facing = blockData.facing
    override val lit = blockData.isLit
    override val state = lit
}

class AsyncDropperDispenser(material: Material, blockData: BlockData) : AsyncBlockData(material), AsyncDirectional, AsyncTwoState {
    override val facing = (blockData as Directional).facing
    override val state = facing == BlockFace.UP || facing == BlockFace.DOWN
}

class AsyncWall(material: Material, blockData: BlockData) : AsyncBlockData(material) {
    
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

class AsyncHead(material: Material, block: Block) : AsyncBlockData(material) {
    
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

class AsyncFluid(material: Material, block: Block) : AsyncBlockData(material) {
    
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

class AsyncBeacon(block: Block) : AsyncBlockData(Material.BEACON) {
    
    val active: Boolean
    
    init {
        val location = block.location
        val start = location.clone().subtract(1.0, 1.0, 1.0)
        val end = location.clone().add(1.0, -1.0, 1.0)
        
        active = if ((start..end).all { it.block.type.isBeaconBase() }) {
            val l = location.clone()
            var obstructed = false
            repeat(255 - location.blockY) {
                l.add(0.0, 1.0, 0.0)
                val material = l.block.type
                if (!material.isTranslucent() && material.isSolid) {
                    obstructed = true
                    return@repeat
                }
            }
            !obstructed
        } else false
    }
    
}

fun Block.toAsyncBlockData(): AsyncBlockData {
    val material = type
    val blockData = blockData
    return when {
        material == Material.BEACON -> AsyncBeacon(this)
        
        material.isFluid() -> AsyncFluid(material, this)
        material.isHead() -> AsyncHead(material, this)
        material.isWall() -> AsyncWall(material, blockData)
        material.isDropperDispenser() -> AsyncDropperDispenser(material, blockData)
        
        blockData.isSlab() -> AsyncSlab(material, blockData as Slab)
        blockData.isSnow() -> AsyncSnow(material, blockData as Snow)
        
        blockData is Stairs -> AsyncStairs(material, blockData)
        blockData is TrapDoor -> AsyncTrapDoor(material, blockData)
        blockData is Door -> AsyncDoor(material, blockData)
        blockData is Gate -> AsyncGate(material, blockData)
        blockData is Switch -> AsyncSwitch(material, blockData)
        blockData is DaylightDetector -> AsyncDaylightDetector(material, blockData)
        blockData is Snowable -> AsyncSnowable(material, blockData)
        blockData is Chest -> AsyncChest(material, blockData)
        blockData is Campfire -> AsyncCampfire(material, blockData)
        blockData is RedstoneWallTorch -> AsyncRedstoneWallTorch(material, blockData)
        
        blockData is Directional -> AsyncDirectionalBlockData(material, blockData)
        blockData is Orientable -> AsyncOrientableBlockData(material, blockData)
        blockData is MultipleFacing -> AsyncMultipleFacingBlockData(material, blockData)
        blockData is Bisected -> AsyncBisectedBlockData(material, blockData)
        blockData is Rotatable -> AsyncRotatableBlockData(material, blockData)
        blockData is Lightable -> AsyncLightableBlockData(material, blockData)
        
        else -> AsyncBlockData(material)
    }
}

private fun BlockData.isSlab() = this is Slab && this.type != Type.DOUBLE

private fun BlockData.isSnow() = this is Snow && this.layers < this.maximumLayers

private fun Material.isDropperDispenser() = this == Material.DISPENSER || this == Material.DROPPER
