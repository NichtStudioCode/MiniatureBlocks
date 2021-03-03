package de.studiocode.miniatureblocks.resourcepack.model.part

import de.studiocode.miniatureblocks.build.concurrent.*
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.RotationValue
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.impl.*
import de.studiocode.miniatureblocks.util.isCarpet
import de.studiocode.miniatureblocks.util.isCrossMaterial
import de.studiocode.miniatureblocks.util.isPressurePlate

abstract class Part {
    
    abstract val elements: List<Element>
    abstract val rotatable: Boolean
    
    private var posRotX by RotationValue()
    private var posRotY by RotationValue()
    private var texRotX by RotationValue()
    private var texRotY by RotationValue()
    
    fun addRotation(direction: Direction) {
        addRotation(direction.xRot, direction.yRot)
    }
    
    fun addRotation(x: Int, y: Int) {
        addPosRotation(x, y)
        addTextureRotation(x, y)
    }
    
    fun addPosRotation(direction: Direction) {
        addPosRotation(direction.xRot, direction.yRot)
    }
    
    fun addPosRotation(x: Int, y: Int) {
        posRotX += x
        posRotY += y
    }
    
    fun addTextureRotation(direction: Direction) {
        addTextureRotation(direction.xRot, direction.yRot)
    }
    
    fun addTextureRotation(x: Int, y: Int) {
        texRotX += x
        texRotY += y
    }
    
    fun applyRotation() {
        if (posRotX == 0 && posRotY == 0 && texRotX == 0 && texRotY == 0) return
        
        elements.forEach {
            it.rotateTexturesAroundXAxis(texRotX)
            it.rotateTexturesAroundYAxis(texRotY)
            
            if (rotatable) {
                it.rotatePosAroundXAxis(posRotX)
                it.rotatePosAroundYAxis(posRotY)
            }
        }
        
        posRotX = 0
        posRotY = 0
        texRotX = 0
        texRotY = 0
    }
    
    companion object {
        
        fun createPart(data: ThreadSafeBlockData): Part =
            when {
                data is StairBlockData -> StairPart(data)
                data is SlabBlockData -> SlabPart(data)
                data is TrapdoorBlockData -> TrapdoorPart(data)
                data is DoorBlockData -> DoorPart(data)
                data.material.isCrossMaterial() -> CrossPart(data)
                data.material.isCarpet() -> CarpetPart(data)
                data.material.isPressurePlate() -> PressurePlatePart(data)
                else -> CubePart(data)
            }
        
    }
    
}