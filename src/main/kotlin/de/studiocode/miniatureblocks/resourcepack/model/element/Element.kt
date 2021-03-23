package de.studiocode.miniatureblocks.resourcepack.model.element

import com.google.common.base.Preconditions
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.util.point.Point2D
import de.studiocode.miniatureblocks.util.point.Point3D
import de.studiocode.miniatureblocks.util.shift
import org.bukkit.Axis
import java.util.*

open class Element(var fromPos: Point3D, var toPos: Point3D, vararg textures: Texture) : Cloneable {
    
    var shade: Boolean = true
    var textures: MutableMap<Direction, Texture> = EnumMap(Direction::class.java)
    var rotationData: RotationData? = null
    
    init {
        Preconditions.checkArgument(textures.size == 6 || textures.size == 1, "textures size has to be 6 or 1")
        
        if (textures.size == 6) {
            this.textures[Direction.NORTH] = textures[0]
            this.textures[Direction.EAST] = textures[1]
            this.textures[Direction.SOUTH] = textures[2]
            this.textures[Direction.WEST] = textures[3]
            this.textures[Direction.UP] = textures[4]
            this.textures[Direction.DOWN] = textures[5]
        } else {
            Direction.values().forEach { this.textures[it] = textures[0] }
        }
    }
    
    fun freezeUV(vararg directions: Direction = Direction.values()) {
        directions.forEach { textures[it]!!.freezeDynamicUV(this, it) }
    }
    
    fun scaleCentred(scale: Double) {
        if (scale == 1.0) return
        
        val sizeX = toPos.x - fromPos.x
        val sizeY = toPos.y - fromPos.y
        val sizeZ = toPos.z - fromPos.z
        
        val takeX = sizeX - (sizeX * scale)
        val takeY = sizeY - (sizeY * scale)
        val takeZ = sizeZ - (sizeZ * scale)
        
        fromPos.x = fromPos.x + (takeX / 2)
        fromPos.y = fromPos.y + (takeY / 2)
        fromPos.z = fromPos.z + (takeZ / 2)
        
        toPos.x = toPos.x - (takeX / 2)
        toPos.y = toPos.y - (takeY / 2)
        toPos.z = toPos.z - (takeZ / 2)
    }
    
    fun move(x: Double, y: Double, z: Double) {
        if (x == 0.0 && y == 0.0 && z == 0.0) return
        
        fromPos.x += x
        fromPos.y += y
        fromPos.z += z
        
        toPos.x += x
        toPos.y += y
        toPos.z += z
    }
    
    fun rotatePosAroundYAxis(rotation: Int, origin: Point3D = Point3D(0.5, 0.5, 0.5)) {
        if (rotation < 1) return
        fromPos.rotateAroundYAxis(rotation, origin)
        toPos.rotateAroundYAxis(rotation, origin)
        
        val sorted = Point3D.sort(fromPos, toPos)
        fromPos = sorted.first
        toPos = sorted.second
        
        rotationData?.rotateAroundYAxis(rotation, origin)
    }
    
    fun rotatePosAroundXAxis(rotation: Int, origin: Point3D = Point3D(0.5, 0.5, 0.5)) {
        if (rotation < 1) return
        fromPos.rotateAroundXAxis(rotation, origin)
        toPos.rotateAroundXAxis(rotation, origin)
        
        val sorted = Point3D.sort(fromPos, toPos)
        fromPos = sorted.first
        toPos = sorted.second
        
        rotationData?.rotateAroundXAxis(rotation, origin)
    }
    
    fun addTextureRotation(rotation: Int, vararg directions: Direction) {
        directions.forEach { textures[it]!!.rotation += rotation }
    }
    
    fun rotateTexturesAroundYAxis(rotation: Int) {
        if (rotation < 1) return
        
        // shift all sides that aren't on the Y axis
        shiftSides(rotation, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
        
        // rotate top and down texture accordingly
        textures[Direction.UP]!!.rotation += rotation
        textures[Direction.DOWN]!!.rotation -= rotation // down texture rotates in the opposite direction
    }
    
    fun rotateTexturesAroundXAxis(rotation: Int) {
        if (rotation < 1) return
        
        // rotate current front texture -180°
        textures[Direction.NORTH]!!.rotation -= 2
        
        // shift all sides that aren't on the X axis
        shiftSides(rotation, Direction.NORTH, Direction.UP, Direction.SOUTH, Direction.DOWN)
        
        // set rotations of X axis textures accordingly
        textures[Direction.WEST]!!.rotation += rotation
        textures[Direction.EAST]!!.rotation -= rotation // east texture rotates in the opposite direction
        
        // rotate new front texture +180°
        textures[Direction.NORTH]!!.rotation += 2
    }
    
    private fun shiftSides(shiftAmount: Int, vararg directions: Direction) {
        // create textures copy and shift sides
        val sideTextures = directions.map { textures[it] }.toMutableList()
        sideTextures.shift(shiftAmount)
        
        // put new shifted values in textures map
        for ((index, side) in directions.withIndex()) {
            textures[side] = sideTextures[index]!!
        }
    }
    
    fun getFromPosInMiniature(x: Int, y: Int, z: Int, stepSize: Double) = getPosInMiniature(fromPos, x, y, z, stepSize)
    
    fun getToPosInMiniature(x: Int, y: Int, z: Int, stepSize: Double) = getPosInMiniature(toPos, x, y, z, stepSize)
    
    private fun getPosInMiniature(pos: Point3D, x: Int, y: Int, z: Int, stepSize: Double): DoubleArray {
        val posInMiniature = DoubleArray(3)
        posInMiniature[0] = pos.x * stepSize + x * stepSize
        posInMiniature[1] = pos.y * stepSize + y * stepSize
        posInMiniature[2] = pos.z * stepSize + z * stepSize
        return posInMiniature
    }
    
    fun getRotationInMiniature(x: Int, y: Int, z: Int, stepSize: Double): RotationData? {
        val rotationData = this.rotationData
        if (rotationData != null) {
            val origin = rotationData.pivotPoint
            val point = Point3D(
                origin.x * stepSize + x * stepSize,
                origin.y * stepSize + y * stepSize,
                origin.z * stepSize + z * stepSize
            )
            return rotationData.clone().also { it.pivotPoint = point }
        }
        return null
    }
    
    fun hasTextures() = textures.any { (_, texture) -> texture.textureLocation.isNotBlank() }
    
    public override fun clone(): Element {
        return (super.clone() as Element).apply {
            fromPos = fromPos.copy()
            toPos = toPos.copy()
            rotationData = rotationData?.clone()
            textures = EnumMap(textures.map { (direction, texture) -> direction to texture.clone() }.toMap())
        }
    }
    
}

class RotationData(var angle: Float, var axis: Axis, var pivotPoint: Point3D, var rescale: Boolean) : Cloneable {
    
    fun rotateAroundYAxis(rotation: Int, origin: Point3D) {
        if (rotation == 0 || axis == Axis.Y) return
        
        val rotZ = if (axis == Axis.Z) angle else 0f
        val rotX = if (axis == Axis.X) angle else 0f
        
        val angle2D = Point2D(rotZ.toDouble(), rotX.toDouble())
        repeat(rotation) { angle2D.rotateClockwise() }
        
        if (angle2D.x != 0.0) {
            axis = Axis.Z
            angle = angle2D.x.toFloat()
        } else {
            axis = Axis.X
            angle = angle2D.y.toFloat()
        }
        
        pivotPoint.rotateAroundYAxis(rotation, origin)
    }
    
    fun rotateAroundXAxis(rotation: Int, origin: Point3D) {
        if (rotation == 0 || axis == Axis.X) return
        
        val rotY = if (axis == Axis.Y) angle else 0f
        val rotZ = if (axis == Axis.Z) angle else 0f
        
        val angle2D = Point2D(rotY.toDouble(), rotZ.toDouble())
        repeat(rotation) { angle2D.rotateClockwise() }
        
        if (angle2D.x != 0.0) {
            axis = Axis.Y
            angle = angle2D.x.toFloat()
        } else {
            axis = Axis.Z
            angle = angle2D.y.toFloat()
        }
        
        pivotPoint.rotateAroundXAxis(rotation, origin)
    }
    
    public override fun clone(): RotationData {
        return (super.clone() as RotationData).apply {
            pivotPoint = pivotPoint.copy()
        }
    }
    
}