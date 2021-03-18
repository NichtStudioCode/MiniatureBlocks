package de.studiocode.miniatureblocks.build.concurrent

import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.util.VersionUtils
import de.studiocode.miniatureblocks.util.isWall
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.*
import org.bukkit.block.data.Bisected.Half
import org.bukkit.block.data.type.*
import org.bukkit.block.data.type.Slab.Type
import org.bukkit.block.data.type.Wall.Height

open class AsyncBlockData(val material: Material)

open class DirectionalBlockData(material: Material, blockData: Directional) : AsyncBlockData(material) {
    val facing = blockData.facing
}

class OrientableBlockData(material: Material, blockData: Orientable) : AsyncBlockData(material) {
    val axis = blockData.axis
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

class WallBlockData(material: Material, val up: Boolean, val faces: HashMap<BlockFace, Boolean>) : AsyncBlockData(material)

class ChestBlockData(material: Material, blockData: Chest) : DirectionalBlockData(material, blockData) {
    val type = blockData.type
}

fun BlockData.toAsyncBlockData(material: Material) =
    when {
        material.isWall() -> createWallBlockData(material, this)
        isSlab() -> SlabBlockData(material, this as Slab)
        isSnow() -> SnowBlockData(material, this as Snow)
        this is Stairs -> StairBlockData(material, this)
        this is TrapDoor -> TrapdoorBlockData(material, this)
        this is Door -> DoorBlockData(material, this)
        this is Gate -> GateBlockData(material, this)
        this is Switch -> SwitchBlockData(material, this)
        this is DaylightDetector -> DaylightDetectorBlockData(material, this)
        this is Snowable -> SnowableBlockData(material, this)
        this is Chest -> ChestBlockData(material, this)
        this is Directional -> DirectionalBlockData(material, this)
        this is Orientable -> OrientableBlockData(material, this)
        this is MultipleFacing -> MultipleFacingBlockData(material, this)
        else -> AsyncBlockData(material)
    }

private fun BlockData.isSlab() = this is Slab && this.type != Type.DOUBLE

private fun BlockData.isSnow() = this is Snow && this.layers < this.maximumLayers

private fun createWallBlockData(material: Material, blockData: BlockData): WallBlockData {
    val up: Boolean
    val faces = HashMap<BlockFace, Boolean>()
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
    
    return WallBlockData(material, up, faces)
}
