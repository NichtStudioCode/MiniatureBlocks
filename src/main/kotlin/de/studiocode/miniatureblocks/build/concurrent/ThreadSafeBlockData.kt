package de.studiocode.miniatureblocks.build.concurrent

import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Bisected.Half
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Orientable
import org.bukkit.block.data.type.*
import org.bukkit.block.data.type.Slab.Type

open class ThreadSafeBlockData(val material: Material)

open class DirectionalBlockData(material: Material, blockData: Directional) : ThreadSafeBlockData(material) {
    val facing = blockData.facing
}

class OrientableBlockData(material: Material, blockData: Orientable) : ThreadSafeBlockData(material) {
    val axis = blockData.axis
}

class SlabBlockData(material: Material, blockData: Slab) : ThreadSafeBlockData(material) {
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

class FenceBlockData(material: Material, blockData: Fence): ThreadSafeBlockData(material) {
    val faces: Set<BlockFace> = HashSet(blockData.faces)
}

class DaylightDetectorData(material: Material, blockData: DaylightDetector) : ThreadSafeBlockData(material) {
    val inverted = blockData.isInverted
}

fun BlockData.toThreadSafeBlockData(material: Material) =
    when (this) {
        is Stairs -> StairBlockData(material, this)
        is Slab -> SlabBlockData(material, this)
        is TrapDoor -> TrapdoorBlockData(material, this)
        is Door -> DoorBlockData(material, this)
        is Fence -> FenceBlockData(material, this)
        is DaylightDetector -> DaylightDetectorData(material, this)
        is Directional -> DirectionalBlockData(material, this)
        is Orientable -> OrientableBlockData(material, this)
        else -> ThreadSafeBlockData(material)
    }
