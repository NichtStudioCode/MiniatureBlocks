package de.studiocode.miniatureblocks.resourcepack.model.part

import de.studiocode.miniatureblocks.build.BuildContext
import de.studiocode.miniatureblocks.build.concurrent.*
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.RotationValue
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.impl.*
import de.studiocode.miniatureblocks.util.isFence
import de.studiocode.miniatureblocks.util.isFlat
import de.studiocode.miniatureblocks.util.isGlassPane
import de.studiocode.miniatureblocks.util.isPot
import de.studiocode.miniatureblocks.util.point.Point3D

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
            it.move(moveX, moveY, moveZ)
            it.rotateTexturesAroundXAxis(texRotX)
            it.rotateTexturesAroundYAxis(texRotY)
            it.rotatePosAroundXAxis(posRotX)
            it.rotatePosAroundYAxis(posRotY)
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
        
        fun createPart(data: AsyncBlockData, context: BuildContext, point: Point3D): Part =
            when {
                data is AsyncStairs -> StairPart(data)
                data is AsyncSlab -> SlabPart(data)
                data is AsyncTrapDoor -> TrapdoorPart(data)
                data is AsyncDoor -> DoorPart(data)
                data is AsyncGate -> GatePart(data)
                data is AsyncSwitch -> SwitchPart(data)
                data is AsyncSnow -> SnowPart(data)
                data is AsyncWall -> WallPart(data)
                data is AsyncChest -> ChestPart(data)
                data is AsyncCampfire -> CampfirePart(data)
                data is AsyncFluid -> FluidPart(data)
                data is AsyncHead -> HeadPart(data)
                data is AsyncBeacon -> BeaconPart(data, context, point)
                data.material.isFence() -> FencePart(data as AsyncMultipleFacingBlockData)
                data.material.isGlassPane() -> GlassPanePart(data as AsyncMultipleFacingBlockData)
                data.material.isPot() -> PotPart(data)
                data.material.isFlat() -> FlatPart(data)
                else -> DefaultPart(data)
            }
        
    }
    
}