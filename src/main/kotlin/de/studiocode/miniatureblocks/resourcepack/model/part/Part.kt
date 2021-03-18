package de.studiocode.miniatureblocks.resourcepack.model.part

import de.studiocode.miniatureblocks.build.concurrent.*
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.RotationValue
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.impl.*
import de.studiocode.miniatureblocks.util.*

abstract class Part {
    
    abstract val elements: List<Element>
    
    private var posRotX by RotationValue()
    private var posRotY by RotationValue()
    private var texRotX by RotationValue()
    private var texRotY by RotationValue()
    private var moveX = 0.0
    private var moveY = 0.0
    private var moveZ = 0.0
    
    fun addMove(x: Double, y: Double, z: Double) {
        moveX += x
        moveY += y
        moveZ += z
    }
    
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
    
    fun applyModifications() {
        elements.forEach {
            it.rotateTexturesAroundXAxis(texRotX)
            it.rotateTexturesAroundYAxis(texRotY)
            it.rotatePosAroundXAxis(posRotX)
            it.rotatePosAroundYAxis(posRotY)
            it.move(moveX, moveY, moveZ)
        }
        
        posRotX = 0
        posRotY = 0
        texRotX = 0
        texRotY = 0
        moveX = 0.0
        moveY = 0.0
        moveZ = 0.0
    }
    
    companion object {
        
        fun createPart(data: AsyncBlockData): Part =
            when {
                data is StairBlockData -> StairPart(data)
                data is SlabBlockData -> SlabPart(data)
                data is TrapdoorBlockData -> TrapdoorPart(data)
                data is DoorBlockData -> DoorPart(data)
                data is GateBlockData -> GatePart(data)
                data is SwitchBlockData -> SwitchPart(data)
                data is DaylightDetectorBlockData -> DaylightDetectorPart(data)
                data is SnowBlockData -> SnowPart(data)
                data is WallBlockData -> WallPart(data)
                data is ChestBlockData -> ChestPart(data)
                data.material.isFence() -> FencePart(data as MultipleFacingBlockData)
                data.material.isGlassPane() -> GlassPanePart(data as MultipleFacingBlockData)
                data.material.isCrossMaterial() -> CrossPart(data)
                data.material.isCarpet() -> CarpetPart(data)
                data.material.isPressurePlate() -> PressurePlatePart(data)
                data.material.isPot() -> PotPart(data)
                data.material.isFlat() -> FlatPart(data)
                data.material.isMiscMaterial() -> MiscPart(data)
                else -> DefaultPart(data)
            }
        
    }
    
}