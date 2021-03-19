package de.studiocode.miniatureblocks.resourcepack.model.element

import com.google.common.base.Preconditions
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.util.point.Point2D
import de.studiocode.miniatureblocks.util.point.Point3D
import de.studiocode.miniatureblocks.util.shift
import org.bukkit.Axis
import java.util.*
import kotlin.math.max
import kotlin.math.min

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
    
    fun rotatePosAroundYAxis(rotation: Int, origin: DoubleArray = doubleArrayOf(0.5, 0.5, 0.5)) {
        if (rotation < 1) return
        val fromPos = this.fromPos.mapToOrigin(origin)
        val toPos = this.toPos.mapToOrigin(origin)
        
        val start = fromPos.to2D(Axis.Y)
        val end = toPos.to2D(Axis.Y)
        
        repeat(rotation) {
            start.rotateClockwise()
            end.rotateClockwise()
        }
        
        fromPos.z = min(start.x, end.x)
        fromPos.x = min(start.y, end.y)
        toPos.z = max(start.x, end.x)
        toPos.x = max(start.y, end.y)
        
        this.fromPos = fromPos.mapFromOrigin(origin)
        this.toPos = toPos.mapFromOrigin(origin)
        
        rotationData?.rotateAroundYAxis(rotation)
    }
    
    fun rotatePosAroundXAxis(rotation: Int, origin: DoubleArray = doubleArrayOf(0.5, 0.5, 0.5)) {
        if (rotation < 1) return
        val fromPos = this.fromPos.mapToOrigin(origin)
        val toPos = this.toPos.mapToOrigin(origin)
        
        val start = fromPos.to2D(Axis.X)
        val end = toPos.to2D(Axis.X)
        
        repeat(rotation) {
            start.rotateClockwise()
            end.rotateClockwise()
        }
        
        fromPos.z = min(start.x, end.x)
        fromPos.y = min(start.y, end.y)
        toPos.z = max(start.x, end.x)
        toPos.y = max(start.y, end.y)
        
        this.fromPos = fromPos.mapFromOrigin(origin)
        this.toPos = toPos.mapFromOrigin(origin)
    }
    
    fun rotatePosAroundZAxis(rotation: Int, origin: DoubleArray = doubleArrayOf(0.5, 0.5, 0.5)) {
        if (rotation < 1) return
        val fromPos = this.fromPos.mapToOrigin(origin)
        val toPos = this.toPos.mapToOrigin(origin)
        
        val start = fromPos.to2D(Axis.Z)
        val end = fromPos.to2D(Axis.Z)
        
        repeat(rotation) {
            start.rotateClockwise()
            end.rotateClockwise()
        }
        
        fromPos.x = min(start.x, end.x)
        fromPos.y = min(start.y, end.y)
        toPos.x = max(start.x, end.x)
        toPos.y = max(start.y, end.y)
        
        this.fromPos = fromPos.mapFromOrigin(origin)
        this.toPos = toPos.mapFromOrigin(origin)
    }
    
    private fun Point3D.mapToOrigin(origin: DoubleArray) =
        Point3D(x - origin[0], y - origin[1], z - origin[2])
    
    private fun Point3D.mapFromOrigin(origin: DoubleArray) =
        Point3D(x + origin[0], y + origin[1], z + origin[2])
    
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
            val origin = rotationData.origin
            val point = Point3D(
                origin.x * stepSize + x * stepSize,
                origin.y * stepSize + y * stepSize,
                origin.z * stepSize + z * stepSize
            )
            return rotationData.copy(origin = point)
        }
        return null
    }
    
    fun hasTextures() = textures.any { (_, texture) -> texture.textureLocation.isNotBlank() }
    
    public override fun clone(): Element {
        return (super.clone() as Element).apply {
            fromPos = fromPos.copy()
            toPos = toPos.copy()
            rotationData = rotationData?.copy()
            textures = EnumMap(textures.map { (direction, texture) -> direction to texture.clone() }.toMap())
        }
    }
    
}

data class RotationData(var angle: Float, var axis: Axis, var origin: Point3D, var rescale: Boolean) {
    
    fun rotateAroundYAxis(rotation: Int) {
        val rotZ = if (axis == Axis.Z) angle else 0f
        val rotX = if (axis == Axis.X) angle else 0f
        val rotPoint = Point2D(rotZ.toDouble(), rotX.toDouble())
        repeat(rotation) { rotPoint.rotateClockwise() }
        if (rotPoint.x != 0.0) {
            axis = Axis.Z
            angle = rotPoint.x.toFloat()
        } else {
            axis = Axis.X
            angle = rotPoint.y.toFloat()
        }
    }
    
    fun rotateAroundXAxis(rotation: Int) {
        TODO("Not implemented yet")
    }
    
}