package de.studiocode.miniatureblocks.resourcepack.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import de.studiocode.miniatureblocks.build.BuildData
import de.studiocode.miniatureblocks.util.addAll

class MiniatureModel(buildData: BuildData) {
    
    private val stepSize = 16.0 / buildData.size
    
    val modelDataObj = JsonObject()
    private val elementArray = JsonArray()
    private val texturesObj = JsonObject()
    
    private val parts = buildData.data
    private val textureMap = HashMap<String, Int>()
    
    init {
        modelDataObj.addProperty("parent", "item/miniatureblocksmain")
        modelDataObj.add("textures", texturesObj)
        modelDataObj.add("elements", elementArray)
        
        parseTextures()
        parseElements()
    }
    
    private fun parseTextures() {
        // create texture map
        var id = 0
        parts.values
            .flatMap { it.elements }
            .flatMap { it.textures.values }
            .map { it.textureLocation }
            .filter { it.isNotBlank() }
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
        parts.forEach { (point, part) ->
            part.elements
                .filter { it.hasTextures() }
                .forEach { element ->
                    val elementObj = JsonObject()
                    
                    // Position
                    val from = JsonArray()
                    from.addAll(element.getFromPosInMiniature(point.x.toInt(), point.y.toInt(), point.z.toInt(), stepSize))
                    elementObj.add("from", from)
                    
                    val to = JsonArray()
                    to.addAll(element.getToPosInMiniature(point.x.toInt(), point.y.toInt(), point.z.toInt(), stepSize))
                    elementObj.add("to", to)
                    
                    // Rotation
                    val rotationData = element.getRotationData(point.x.toInt(), point.y.toInt(), point.z.toInt(), stepSize)
                    if (rotationData != null) {
                        val rotation = JsonObject()
                        rotation.addProperty("angle", rotationData.first)
                        rotation.addProperty("axis", rotationData.second)
                        rotation.add("origin", JsonArray().apply { addAll(rotationData.third) })
                        elementObj.add("rotation", rotation)
                    }
                    
                    // Faces
                    val faces = JsonObject()
                    Direction.values().forEach { direction ->
                        val texture = element.textures[direction]!!
                        if (texture.textureLocation.isNotBlank()) {
                            val face = JsonObject()
                            face.add("uv", JsonArray().apply { addAll(texture.getUvInMiniature(element, direction)) })
                            face.addProperty("texture", "#" + textureMap[texture.textureLocation]!!.toString())
                            face.addProperty("rotation", texture.rotation * 90)
                            
                            faces.add(direction.modelDataName, face)
                        }
                    }
                    elementObj.add("faces", faces)
                    
                    elementArray.add(elementObj)
                }
        }
    }
    
}