package de.studiocode.miniatureblocks.resourcepack.model.parser

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import de.studiocode.miniatureblocks.builderworld.BuildData
import de.studiocode.miniatureblocks.builderworld.BuildData.BuildBlockData
import de.studiocode.miniatureblocks.resourcepack.model.ModelData
import de.studiocode.miniatureblocks.resourcepack.model.parser.part.Part
import de.studiocode.miniatureblocks.resourcepack.model.parser.part.PartManager
import de.studiocode.miniatureblocks.utils.addAll

class MiniatureModel(data: BuildData) {
    
    private val stepSize = data.size / 16
    private val dataList = data.data
    
    private val mainObj = JsonObject()
    private val elementArray = JsonArray()
    private val texturesObj = JsonObject()
    
    private val parts = HashMap<BuildBlockData, Part>()
    private val textureMap = HashMap<String, Int>()
    
    val modelData: ModelData
    
    init {
        mainObj.addProperty("parent", "item/miniatureblocksmain")
        mainObj.add("textures", texturesObj)
        mainObj.add("elements", elementArray)
        
        createConstructs()
        parseTextures()
        parseElements()
        
        modelData = ModelData(mainObj)
    }
    
    private fun createConstructs() {
        for (data in dataList) {
            val part = PartManager.getConstructOf(data.block)
            part.applyRotation()
            
            parts[data] = part
        }
    }
    
    private fun parseTextures() {
        // create texture map
        var id = 0
        parts.values
            .flatMap { it.elements }
            .flatMap { it.textures.values }
            .map { it.textureLocation }
            .forEach {
                if (!textureMap.containsKey(it)) {
                    textureMap[it] = id
                    id++
                }
            }
        
        // put them into the textures JsonObject
        textureMap.forEach { (texture, id) ->
            texturesObj.addProperty(id.toString(), texture)
        }
    }
    
    private fun parseElements() {
        for ((blockData, part) in parts) {
            for (element in part.elements) {
                val elementObj = JsonObject()
                
                // Position
                val from = JsonArray()
                from.addAll(element.getFromPosInMiniature(blockData.x, blockData.y, blockData.z, stepSize))
                elementObj.add("from", from)
                
                val to = JsonArray()
                to.addAll(element.getToPosInMiniature(blockData.x, blockData.y, blockData.z, stepSize))
                elementObj.add("to", to)
                
                // Faces
                val faces = JsonObject()
                for (direction in Direction.values()) {
                    if (!blockData.isSideVisible(direction)) continue
                    
                    val texture = element.textures[direction]!!
                    
                    val face = JsonObject()
                    face.add("uv", JsonArray().apply { addAll(texture.uv) })
                    face.addProperty("texture", "#" + textureMap[texture.textureLocation]!!.toString())
                    face.addProperty("rotation", texture.rotation * 90)
                    
                    faces.add(direction.modelDataName, face)
                }
                elementObj.add("faces", faces)
                
                elementArray.add(elementObj)
            }
        }
    }
    
}