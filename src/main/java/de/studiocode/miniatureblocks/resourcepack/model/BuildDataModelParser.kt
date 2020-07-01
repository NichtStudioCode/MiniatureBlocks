package de.studiocode.miniatureblocks.resourcepack.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import de.studiocode.miniatureblocks.builderworld.BuildData
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bukkit.Material

class BuildDataModelParser(buildData: BuildData) {

    companion object {
        private val FULL_UV = JsonArray()
        private val FIRST_UV = JsonArray()

        init {
            intArrayOf(0, 0, 16, 16).forEach { FULL_UV.add(it) }
            intArrayOf(0, 0, 1, 1).forEach { FIRST_UV.add(it) }
        }
    }

    private val mainObj = JsonObject()
    private val textures = JsonObject()
    private val elements = JsonArray()
    private val data = buildData.data
    private val textureIds = HashMap<String, Int>()

    init {
        mainObj.addProperty("parent", "item/miniatureblocksmain")
        mainObj.add("textures", textures)
        mainObj.add("elements", elements)
    }

    fun parse(parseType: ParseType): ModelData {
        createTextureIds()
        parseModelData(parseType)

        return ModelData(mainObj)
    }

    private fun createTextureIds() {
        // get all used materials
        val usedMaterials = ArrayList<Material>()
        for (material in data.map { it.material }) {
            if (!usedMaterials.contains(material)) {
                usedMaterials.add(material)
            }
        }

        //get all textures for used materials and register them in the textureIds HashMap
        var id = 0
        for (material in usedMaterials) {
            println("Used material: $material")
            if (BlockTexture.hasMaterial(material)) {
                val blockTexture = BlockTexture.getFromMaterial(material)
                for (texture in blockTexture.getAllTextures()) {
                    if (!textureIds.containsKey(texture)) {
                        textureIds[texture] = id
                        id++
                    }
                }
            } else println("No BlockTexture for $material")
        }

        //also write them to json model
        textureIds.forEach { (texture, id) -> textures.addProperty(id.toString(), texture) }
    }

    private fun parseModelData(parseType: ParseType) {
        data.forEach {
            val material = it.material
            if (BlockTexture.hasMaterial(material)) {
                val blockTexture = BlockTexture.getFromMaterial(material)
                val element = JsonObject()

                val from = JsonArray()
                from.add(it.x)
                from.add(it.y)
                from.add(it.z)
                element.add("from", from)

                val to = JsonArray()
                to.add(it.x + 1)
                to.add(it.y + 1)
                to.add(it.z + 1)
                element.add("to", to)

                val faces = JsonObject()
                for (blockFace in BlockFace.values()) {
                    if (!it.hasBlock(blockFace)) {
                        val faceObj = JsonObject()

                        val uv = when (parseType) {
                            ParseType.FULL -> FULL_UV
                            ParseType.FIRST -> FIRST_UV
                            ParseType.RIGHT -> createRightUv(it, blockFace)
                        }

                        //TODO: apply texture depending on blocks rotation
                        
                        faceObj.add("uv", uv)
                        val textureId = textureIds[blockTexture.getTextureForBlockFace(blockFace)]
                        faceObj.add("texture", JsonPrimitive("#$textureId"))

                        faces.add(blockFace.modelName, faceObj)
                    }
                }
                element.add("faces", faces)

                elements.add(element)
            } else println("Material $material is invalid, skipping parsing")
        }
    }

    // fixme: I don't think this is working correctly.
    private fun createRightUv(blockData: BuildData.BlockData, blockFace: BlockFace): JsonArray {
        val uv = JsonArray()

        when (blockFace) {

            BlockFace.UP, BlockFace.DOWN -> {
                uv.add(blockData.x)
                uv.add(blockData.z)
                uv.add(blockData.x + 1)
                uv.add(blockData.z + 1)
            }

            BlockFace.NORTH, BlockFace.SOUTH -> {
                uv.add(blockData.x)
                uv.add(blockData.y)
                uv.add(blockData.x + 1)
                uv.add(blockData.y + 1)
            }

            BlockFace.EAST, BlockFace.WEST -> {
                uv.add(blockData.z)
                uv.add(blockData.y)
                uv.add(blockData.z + 1)
                uv.add(blockData.y + 1)
            }

        }

        return uv
    }

    enum class BlockFace(val modelName: String) {

        NORTH("north"),
        EAST("east"),
        SOUTH("south"),
        WEST("west"),
        UP("up"),
        DOWN("down");

    }

    enum class ParseType {

        //I don't know if I want to keep anything besides 'FULL'
        
        FULL,
        FIRST,
        RIGHT;

    }

}