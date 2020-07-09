package de.studiocode.miniatureblocks.resourcepack.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import de.studiocode.miniatureblocks.builderworld.BuildData
import de.studiocode.miniatureblocks.builderworld.BuildData.BuildBlockData
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bukkit.Material

class BuildDataModelParser(buildData: BuildData) {

    companion object {
        private val UV = JsonArray()

        init {
            intArrayOf(0, 0, 16, 16).forEach { UV.add(it) }
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

    fun parse(): ModelData {
        createTextureIds()
        parseModelData()

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

    private fun parseModelData() {
        data.forEach {
            val material = it.material
            if (BlockTexture.hasMaterial(material)) {
                val element = JsonObject()

                val blockTexture = BlockTexture.getFromMaterial(it.material)
                val cube = Cube(blockTexture)

                val facing = it.facing
                if (facing != null) cube.rotate(Cube.Direction.fromBlockFace(facing))

                val axis = it.axis
                if (axis != null) cube.rotate(Cube.Direction.fromAxis(axis))

                addVoxelPos(element, it)
                addVoxelTextures(element, it, cube)

                elements.add(element)
            } else println("Material $material is invalid, skipping parsing")
        }
    }

    private fun addVoxelPos(element: JsonObject, buildBlockData: BuildBlockData) {
        val from = JsonArray()
        from.add(buildBlockData.x)
        from.add(buildBlockData.y)
        from.add(buildBlockData.z)
        element.add("from", from)

        val to = JsonArray()
        to.add(buildBlockData.x + 1)
        to.add(buildBlockData.y + 1)
        to.add(buildBlockData.z + 1)
        element.add("to", to)
    }

    private fun addVoxelTextures(element: JsonObject, buildBlockData: BuildBlockData, cube: Cube) {
        val faces = JsonObject()

        for ((side, texture) in cube.textures) {
            if (!buildBlockData.hasBlock(side)) { // don't add a texture when I can't bee seen
                val faceObj = JsonObject()
                faceObj.add("uv", UV)

                val textureId = textureIds[texture.texture]
                faceObj.addProperty("texture", "#$textureId")
                faceObj.addProperty("rotation", 90 * texture.rotation)

                faces.add(side.modelDataName, faceObj)
            }
        }
        element.add("faces", faces)
    }
}