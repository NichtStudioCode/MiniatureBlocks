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
import org.bukkit.block.data.FaceAttachable.AttachedFace
import org.bukkit.block.data.type.*
import org.bukkit.block.data.type.RedstoneWire.Connection
import org.bukkit.block.data.type.Slab.Type
import org.bukkit.block.data.type.Wall.Height
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

interface AsyncData {
    val material: Material
}

interface AsyncDirectional : AsyncData {
    val facing: BlockFace
}

interface AsyncRotatable : AsyncData {
    val rotation: BlockFace
}

interface AsyncOrientable : AsyncData {
    val axis: Axis
}

interface AsyncBisected {
    val half: Half
    val state: Boolean
}

interface AsyncMultipleFacing : AsyncData {
    val faces: HashSet<BlockFace>
}

interface AsyncLightable {
    val lit: Boolean
}

interface AsyncFaceAttachable : AsyncData {
    val attachedFace: AttachedFace
}

interface AsyncLevelled : AsyncData {
    val level: Int
}

interface AsyncMultiModel : AsyncData {
    val model: Int
}

interface AsyncMultiTexture : AsyncData {
    val texture: Int
}

interface AsyncMultiCopyable : AsyncData {
    val faces: HashSet<BlockFace>
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

class AsyncBisectedBlockData(material: Material, blockData: Bisected) : AsyncBlockData(material), AsyncBisected, AsyncMultiTexture {
    override val half = blockData.half
    override val state = half == Half.TOP
    override val texture = state.intValue
}

class AsyncMultipleFacingBlockData(material: Material, blockData: MultipleFacing) : AsyncBlockData(material), AsyncMultipleFacing {
    override val faces = HashSet(blockData.faces)
}

class AsyncLightableBlockData(material: Material, lightable: Lightable) : AsyncBlockData(material), AsyncMultiTexture {
    override val texture = lightable.isLit.intValue
}

class AsyncLevelledBlockData(material: Material, blockData: Levelled) : AsyncBlockData(material), AsyncLevelled, AsyncMultiModel {
    override val level = blockData.level
    override val model = level
}

class AsyncMultiCopyableBlockData(material: Material, blockData: MultipleFacing): AsyncBlockData(material), AsyncMultiCopyable {
    override val faces = HashSet(blockData.faces)
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

class AsyncDaylightDetector(material: Material, blockData: DaylightDetector) : AsyncBlockData(material), AsyncMultiTexture {
    override val texture = blockData.isInverted.intValue
}

class AsyncSnow(material: Material, blockData: Snow) : AsyncBlockData(material) {
    val layers = blockData.layers
    val maximumLayers = blockData.maximumLayers
}

class AsyncSnowable(material: Material, blockData: Snowable) : AsyncBlockData(material), AsyncMultiTexture {
    override val texture = blockData.isSnowy.intValue
}

class AsyncSwitch(material: Material, blockData: Switch) : AsyncBlockData(material), AsyncFaceAttachable, AsyncDirectional, AsyncMultiModel {
    override val facing = blockData.facing
    override val model = blockData.isPowered.intValue
    override val attachedFace = blockData.attachedFace
}

class AsyncChest(material: Material, blockData: Chest) : AsyncBlockData(material), AsyncDirectional {
    override val facing = blockData.facing
    val type = blockData.type
}

class AsyncCampfire(material: Material, blockData: Campfire) : AsyncBlockData(material), AsyncDirectional, AsyncLightable, AsyncMultiModel {
    override val facing = blockData.facing
    override val lit = blockData.isLit
    override val model = lit.intValue
}

class AsyncRedstoneWallTorch(material: Material, blockData: RedstoneWallTorch) : AsyncBlockData(material), AsyncDirectional, AsyncLightable, AsyncMultiTexture {
    override val facing = blockData.facing
    override val lit = blockData.isLit
    override val texture = lit.intValue
}

class AsyncDropperDispenser(material: Material, blockData: BlockData) : AsyncBlockData(material), AsyncDirectional, AsyncMultiTexture {
    override val facing = (blockData as Directional).facing
    override val texture = (facing == BlockFace.UP || facing == BlockFace.DOWN).intValue
}

class AsyncRail(material: Material, blockData: Rail) : AsyncBlockData(material) {
    val shape = blockData.shape
    val powered = if (blockData is RedstoneRail) blockData.isPowered else false
}

class AsyncScaffolding(material: Material, blockData: Scaffolding) : AsyncBlockData(material), AsyncMultiModel {
    override val model = blockData.isBottom.intValue
}

class AsyncFire(material: Material, blockData: Fire) : AsyncBlockData(material), AsyncMultipleFacing {
    override val faces = HashSet(blockData.faces)
}

class AsyncFarmland(material: Material, blockData: Farmland) : AsyncBlockData(material), AsyncMultiTexture {
    override val texture = (blockData.moisture > 0).intValue
}

class AsyncBrewingStand(material: Material, blockData: BrewingStand) : AsyncBlockData(material) {
    val bottles = HashSet(blockData.bottles)
}

class AsyncAgeable(material: Material, blockData: Ageable) : AsyncBlockData(material) {
    val age = blockData.age
}

class AsyncTurtleEgg(material: Material, blockData: TurtleEgg) : AsyncBlockData(material), AsyncMultiModel, AsyncMultiTexture {
    override val model = blockData.eggs - 1
    override val texture = blockData.hatch
}

class AsyncHopper(material: Material, blockData: Hopper) : AsyncBlockData(material), AsyncMultiModel, AsyncDirectional {
    override val facing = blockData.facing.let { if (it == BlockFace.DOWN) BlockFace.NORTH else it }
    override val model = (blockData.facing != BlockFace.DOWN).intValue
}

class AsyncEndPortalFrame(material: Material, blockData: EndPortalFrame) : AsyncBlockData(material), AsyncMultiModel, AsyncDirectional {
    override val facing = blockData.facing
    override val model = blockData.hasEye().intValue
}

class AsyncCake(material: Material, blockData: Cake) : AsyncBlockData(material), AsyncMultiModel {
    override val model = blockData.bites
}

class AsyncSeaPickle(material: Material, blockData: SeaPickle) : AsyncBlockData(material), AsyncMultiModel {
    override val model = blockData.pickles - 1 + if (blockData.isWaterlogged) 0 else 4
}

class AsyncBed(material: Material, blockData: Bed): AsyncBlockData(material), AsyncMultiModel, AsyncDirectional {
    override val model = blockData.part.ordinal
    override val facing = blockData.facing
}

class AsyncLightningRod(material: Material, blockData: LightningRod): AsyncBlockData(material), AsyncMultiTexture, AsyncDirectional {
    override val texture = blockData.isPowered.intValue
    override val facing = blockData.facing
}

class AsyncSculkSensor(material: Material, blockData: SculkSensor): AsyncBlockData(material), AsyncMultiTexture {
    override val texture = if (blockData.phase == SculkSensor.Phase.ACTIVE) 1 else 0
}

class AsyncDripstone(material: Material, blockData: PointedDripstone): AsyncBlockData(material), AsyncMultiTexture {
    override val texture = blockData.thickness.ordinal + if (blockData.verticalDirection == BlockFace.DOWN) 5 else 0
}

class AsyncCandle(material: Material, blockData: Candle): AsyncBlockData(material), AsyncMultiModel, AsyncMultiTexture {
    override val model = blockData.candles - 1
    override val texture = blockData.isLit.intValue
}

class AsyncCaveVinesPlant(material: Material, blockData: CaveVinesPlant): AsyncBlockData(material), AsyncMultiTexture  {
    override val texture = blockData.isBerries.intValue
}

class AsyncDripleaf(material: Material, blockData: Dripleaf): AsyncBlockData(material), AsyncMultiModel, AsyncDirectional {
    
    override val facing = blockData.facing
    override val model: Int = when (blockData) {
        is BigDripleaf -> blockData.tilt.ordinal
        is SmallDripleaf -> blockData.half.ordinal
        else -> 0
    }
    
}

class AsyncRedstoneWire(material: Material, blockData: RedstoneWire) : AsyncBlockData(material) {
    
    val faces = HashMap<BlockFace, Connection>()
    val power = blockData.power
    
    init {
        Direction.cardinalPoints
            .map { it.blockFace }
            .forEach { faces[it] = blockData.getFace(it) }
    }
    
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

class AsyncFluid(material: Material, block: Block) : AsyncBlockData(material), AsyncLevelled {
    
    override val level = (block.blockData as Levelled).level
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
        material.isMultiCopyable() -> AsyncMultiCopyableBlockData(material, blockData as MultipleFacing)
        
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
        blockData is Rail -> AsyncRail(material, blockData)
        blockData is Scaffolding -> AsyncScaffolding(material, blockData)
        blockData is Fire -> AsyncFire(material, blockData)
        blockData is RedstoneWire -> AsyncRedstoneWire(material, blockData)
        blockData is Farmland -> AsyncFarmland(material, blockData)
        blockData is BrewingStand -> AsyncBrewingStand(material, blockData)
        blockData is TurtleEgg -> AsyncTurtleEgg(material, blockData)
        blockData is Hopper -> AsyncHopper(material, blockData)
        blockData is EndPortalFrame -> AsyncEndPortalFrame(material, blockData)
        blockData is Cake -> AsyncCake(material, blockData)
        blockData is SeaPickle -> AsyncSeaPickle(material, blockData)
        blockData is Bed -> AsyncBed(material, blockData)
        blockData is LightningRod -> AsyncLightningRod(material, blockData)
        blockData is SculkSensor -> AsyncSculkSensor(material, blockData)
        blockData is PointedDripstone -> AsyncDripstone(material, blockData)
        blockData is Dripleaf -> AsyncDripleaf(material, blockData)
        blockData is Candle -> AsyncCandle(material, blockData)
        blockData is CaveVinesPlant -> AsyncCaveVinesPlant(material, blockData)
        
        blockData is Ageable -> AsyncAgeable(material, blockData)
        blockData is Directional -> AsyncDirectionalBlockData(material, blockData)
        blockData is Orientable -> AsyncOrientableBlockData(material, blockData)
        blockData is MultipleFacing -> AsyncMultipleFacingBlockData(material, blockData)
        blockData is Bisected -> AsyncBisectedBlockData(material, blockData)
        blockData is Rotatable -> AsyncRotatableBlockData(material, blockData)
        blockData is Lightable -> AsyncLightableBlockData(material, blockData)
        blockData is Levelled -> AsyncLevelledBlockData(material, blockData)
        
        else -> AsyncBlockData(material)
    }
}

private fun BlockData.isSlab() = this is Slab && this.type != Type.DOUBLE

private fun BlockData.isSnow() = this is Snow && this.layers < this.maximumLayers

private fun Material.isDropperDispenser() = this == Material.DISPENSER || this == Material.DROPPER

private fun Material.isMultiCopyable() = this == Material.VINE || this == Material.GLOW_LICHEN