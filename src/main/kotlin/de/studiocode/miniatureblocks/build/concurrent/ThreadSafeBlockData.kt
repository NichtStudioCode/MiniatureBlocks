package de.studiocode.miniatureblocks.build.concurrent

import org.bukkit.Material
import org.bukkit.block.data.Bisected.Half
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Orientable
import org.bukkit.block.data.type.Slab
import org.bukkit.block.data.type.Slab.Type
import org.bukkit.block.data.type.Stairs

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

fun BlockData.toThreadSafeBlockData(material: Material) =
    when (this) {
        is Stairs -> StairBlockData(material, this)
        is Slab -> SlabBlockData(material, this)
        is Directional -> DirectionalBlockData(material, this)
        is Orientable -> OrientableBlockData(material, this)
        else -> ThreadSafeBlockData(material)
    }
