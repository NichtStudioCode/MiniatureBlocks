package de.studiocode.miniatureblocks.build.concurrent

import org.bukkit.Material
import org.bukkit.block.data.*
import org.bukkit.block.data.Bisected.Half
import org.bukkit.block.data.type.*
import org.bukkit.block.data.type.Slab.Type

open class ThreadSafeBlockData(val material: Material)

open class DirectionalBlockData(material: Material, blockData: Directional) : ThreadSafeBlockData(material) {
    val facing = blockData.facing
}

class OrientableBlockData(material: Material, blockData: Orientable) : ThreadSafeBlockData(material) {
    val axis = blockData.axis
}

open class MultipleFacingBlockData(material: Material, blockData: MultipleFacing) : ThreadSafeBlockData(material) {
    val faces = HashSet(blockData.faces)
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

class FenceBlockData(material: Material, blockData: Fence) : MultipleFacingBlockData(material, blockData)

class GateBlockData(material: Material, blockData: Gate) : DirectionalBlockData(material, blockData) {
    val inWall = blockData.isInWall
    val open = blockData.isOpen
}

class DaylightDetectorBlockData(material: Material, blockData: DaylightDetector) : ThreadSafeBlockData(material) {
    val inverted = blockData.isInverted
}

class SnowBlockData(material: Material, blockData: Snow) : ThreadSafeBlockData(material) {
    val layers = blockData.layers
    val maximumLayers = blockData.maximumLayers
}

class SnowableBlockData(material: Material, blockData: Snowable) : ThreadSafeBlockData(material) {
    val snowy = blockData.isSnowy
}

class SwitchBlockData(material: Material, blockData: Switch) : DirectionalBlockData(material, blockData) {
    val attachedFace = blockData.attachedFace
}

fun BlockData.toThreadSafeBlockData(material: Material) =
    when {
        isSlab() -> SlabBlockData(material, this as Slab)
        isSnow() -> SnowBlockData(material, this as Snow)
        this is Stairs -> StairBlockData(material, this)
        this is TrapDoor -> TrapdoorBlockData(material, this)
        this is Door -> DoorBlockData(material, this)
        this is Fence -> FenceBlockData(material, this)
        this is Gate -> GateBlockData(material, this)
        this is Switch -> SwitchBlockData(material, this)
        this is DaylightDetector -> DaylightDetectorBlockData(material, this)
        this is Snowable -> SnowableBlockData(material, this)
        this is Directional -> DirectionalBlockData(material, this)
        this is Orientable -> OrientableBlockData(material, this)
        this is MultipleFacing -> MultipleFacingBlockData(material, this)
        else -> ThreadSafeBlockData(material)
    }

fun BlockData.isSlab() = this is Slab && this.type != Type.DOUBLE

fun BlockData.isSnow() = this is Snow && this.layers < this.maximumLayers