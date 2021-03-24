package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncRedstoneWire
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.block.data.type.RedstoneWire.Connection

private const val SPACE = 0.25 / 16.0

class RedstonePart(data: AsyncRedstoneWire) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    override val elements = ArrayList<Element>()
    
    init {
        // load model
        val allTextures = blockTexture.textures
        val textures = if (data.power > 0)
            allTextures.copyOfRange(allTextures.size / 2, allTextures.size)
        else allTextures.copyOfRange(0, allTextures.size / 2)
        elements += SerializedPart.getModelElements("model/redstone_dust", textures)
        
        // remove all sides with no connection
        data.faces
            .filter { (_, connection) -> connection == Connection.NONE }
            .forEach { (face, _) -> elements.removeIf { element -> element.name.equals(face.name, true) } }
        
        // remove middle dot if line
        val connectedFaces = data.faces.filter { (_, connection) -> connection != Connection.NONE }
        if (connectedFaces.size == 2) {
            val faces = connectedFaces.keys.toList()
            if (Direction.of(faces[0]) == Direction.of(faces[1]).opposite) {
                elements.removeIf { it.name.equals("middle", true) }
            }
        }
        
        // add up connections
        data.faces
            .filter { (_, connection) -> connection == Connection.UP }
            .forEach { (face, _) ->
                val direction = Direction.of(face)
                SerializedPart.getModelElements("model/redstone_dust", textures)
                    .filter { it.name == "north" || it.name == "south" }
                    .forEach {
                        it.rotateTexturesAroundXAxis(3)
                        it.rotatePosAroundXAxis(3, Point3D(0.0, SPACE, SPACE))
                        
                        it.rotateTexturesAroundYAxis(direction.yRot)
                        it.rotatePosAroundYAxis(direction.yRot)
                        
                        elements += it
                    }
            }
    }
    
}