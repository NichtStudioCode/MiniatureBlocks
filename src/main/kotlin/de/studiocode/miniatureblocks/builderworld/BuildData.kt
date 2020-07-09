package de.studiocode.miniatureblocks.builderworld

import de.studiocode.miniatureblocks.resourcepack.model.Cube.Direction
import de.studiocode.miniatureblocks.utils.isSeeTrough
import org.bukkit.Axis
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Orientable

class BuildData(chunk: Chunk) {

    val data = ArrayList<BuildBlockData>()

    init {
        for (x in 0..15) {
            for (y in 1..16) {
                for (z in 0..15) {
                    val block = chunk.getBlock(x, y, z)
                    val blockData = block.blockData
                    val type = block.type
                    if (type != Material.AIR) {
                        val blockUp = if (y + 1 != 17) !chunk.getBlock(x, y + 1, z).type.isSeeTrough() else false
                        val blockDown = if (y - 1 != 0) !chunk.getBlock(x, y - 1, z).type.isSeeTrough() else false

                        val blockSouth = if (z + 1 != 16) !chunk.getBlock(x, y, z + 1).type.isSeeTrough() else false
                        val blockNorth = if (z - 1 != -1) !chunk.getBlock(x, y, z - 1).type.isSeeTrough() else false

                        val blockEast = if (x + 1 != 16) !chunk.getBlock(x + 1, y, z).type.isSeeTrough() else false
                        val blockWest = if (x - 1 != -1) !chunk.getBlock(x - 1, y, z).type.isSeeTrough() else false


                        val buildBlockData = BuildBlockData(x, y, z, type, blockUp, blockDown, blockSouth, blockNorth, blockEast, blockWest)

                        if (blockData is Directional) buildBlockData.facing = blockData.facing
                        if (blockData is Orientable) buildBlockData.axis = blockData.axis

                        if (!buildBlockData.isSurroundedByBlocks()) data.add(buildBlockData)
                    }
                }
            }
        }
    }

    class BuildBlockData(val x: Int, val y: Int, val z: Int, val material: Material,
                         private val blockUp: Boolean = false, private val blockDown: Boolean = false,
                         private val blockSouth: Boolean = false, private val blockNorth: Boolean = false,
                         private val blockEast: Boolean = false, private val blockWest: Boolean = false) {

        var facing: BlockFace? = null
        var axis: Axis? = null

        fun isSurroundedByBlocks(): Boolean = blockUp && blockDown && blockSouth && blockNorth && blockEast && blockWest

        fun hasBlock(side: Direction): Boolean = when (side) {
            Direction.NORTH -> blockNorth
            Direction.EAST -> blockEast
            Direction.SOUTH -> blockSouth
            Direction.WEST -> blockWest
            Direction.UP -> blockUp
            Direction.DOWN -> blockDown
        }

    }
}