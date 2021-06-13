package de.studiocode.miniatureblocks.resourcepack.model.part

import de.studiocode.miniatureblocks.build.BuildContext
import de.studiocode.miniatureblocks.build.concurrent.*
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.impl.*
import de.studiocode.miniatureblocks.util.isFence
import de.studiocode.miniatureblocks.util.point.Point3D
import org.bukkit.Material

abstract class Part {
    
    abstract val elements: List<Element>
    
    fun move(x: Double, y: Double, z: Double) {
        elements.forEach { it.move(x, y, z) }
    }
    
    fun rotate(direction: Direction) {
        rotate(direction.xRot, direction.yRot)
    }
    
    fun rotate(x: Int, y: Int) {
        rotatePos(x, y)
        rotateTextures(x, y)
    }
    
    fun rotatePos(direction: Direction) {
        rotatePos(direction.xRot, direction.yRot)
    }
    
    fun rotatePos(x: Int, y: Int) {
        elements.forEach {
            it.rotatePosAroundXAxis(x)
            it.rotatePosAroundYAxis(y)
        }
    }
    
    fun rotateTextures(direction: Direction) {
        rotateTextures(direction.xRot, direction.yRot)
    }
    
    fun rotateTextures(x: Int, y: Int) {
        elements.forEach {
            it.rotateTexturesAroundXAxis(x)
            it.rotateTexturesAroundYAxis(y)
        }
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
                data is AsyncFluid -> FluidPart(data)
                data is AsyncHead -> HeadPart(data)
                data is AsyncBeacon -> BeaconPart(data, context, point)
                data is AsyncRail -> RailPart(data)
                data is AsyncFire -> FirePart(data)
                data is AsyncRedstoneWire -> RedstonePart(data)
                data is AsyncBrewingStand -> BrewingStandPart(data)
                data.material.isFence() -> FencePart(data as AsyncMultipleFacingBlockData)
                data.material == Material.IRON_BARS -> IronBarsPart(data as AsyncMultipleFacing)
                data is AsyncMultipleFacing -> MultipleFacingPart(data)
                else -> DefaultPart(data)
            }
        
    }
    
}