package de.studiocode.miniatureblocks.builderworld

import de.studiocode.miniatureblocks.resourcepack.model.BuildDataModelParser.BlockFace
import de.studiocode.miniatureblocks.utils.isSeeTrough
import org.bukkit.Chunk
import org.bukkit.Material

class BuildData(chunk: Chunk) {

    val data = ArrayList<BlockData>()

    init {
        for (x in 0..15) {
            for (y in 1..16) {
                for (z in 0..15) {
                    val type = chunk.getBlock(x, y, z).type
                    if (type != Material.AIR) {
                        val blockUp = if (y + 1 != 17) !chunk.getBlock(x, y + 1, z).type.isSeeTrough() else false
                        val blockDown = if (y - 1 != 0) !chunk.getBlock(x, y - 1, z).type.isSeeTrough() else false

                        val blockSouth = if (z + 1 != 16) !chunk.getBlock(x, y, z + 1).type.isSeeTrough() else false
                        val blockNorth = if (z - 1 != -1) !chunk.getBlock(x, y, z - 1).type.isSeeTrough() else false

                        val blockEast = if (x + 1 != 16) !chunk.getBlock(x + 1, y, z).type.isSeeTrough() else false
                        val blockWest = if (x - 1 != -1) !chunk.getBlock(x - 1, y, z).type.isSeeTrough() else false

                        val blockData = BlockData(x, y, z, type, blockUp, blockDown, blockSouth, blockNorth, blockEast, blockWest)
                        if (!blockData.isSurroundedByBlocks()) data.add(blockData)
                    }
                }
            }
        }
    }

    class BlockData(val x: Int, val y: Int, val z: Int, val material: Material,
                    val blockUp: Boolean = false, val blockDown: Boolean = false, val blockSouth: Boolean = false,
                    val blockNorth: Boolean = false, val blockEast: Boolean = false, val blockWest: Boolean = false) {

        fun isSurroundedByBlocks(): Boolean = blockUp && blockDown && blockSouth && blockNorth && blockEast && blockWest

        fun hasBlock(blockFace: BlockFace): Boolean = when (blockFace) {
            BlockFace.UP -> blockUp
            BlockFace.DOWN -> blockDown
            BlockFace.SOUTH -> blockSouth
            BlockFace.NORTH -> blockNorth
            BlockFace.EAST -> blockEast
            BlockFace.WEST -> blockWest
        }

    }
}