package de.studiocode.miniatureblocks.miniature.data.types

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.nio.ByteBuffer

object FloatArrayDataType : PersistentDataType<ByteArray, FloatArray> {
    
    override fun getPrimitiveType() = ByteArray::class.java
    
    override fun getComplexType() = FloatArray::class.java
    
    override fun toPrimitive(complex: FloatArray, context: PersistentDataAdapterContext): ByteArray {
        val buf = ByteBuffer.allocate(4 * complex.size)
        for (float in complex) buf.putFloat(float)
        return buf.array()
    }
    
    override fun fromPrimitive(primitive: ByteArray, context: PersistentDataAdapterContext): FloatArray {
        val floatList = ArrayList<Float>()
        val buffer = ByteBuffer.wrap(primitive)
        for (index in 0 until primitive.size / 4) {
            floatList.add(buffer.getFloat(index * 4))
        }
        return floatList.toFloatArray()
    }
    
}