package de.studiocode.miniatureblocks.miniature

import de.studiocode.miniatureblocks.MiniatureBlocks
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType.BYTE

abstract class Miniature(dataHolder: PersistentDataHolder) {
    
    companion object {
        
        private val PLUGIN = MiniatureBlocks.INSTANCE
        val TYPE_ID_KEY = NamespacedKey(PLUGIN, "miniatureTypeID")
        
        fun getTypeId(dataHolder: PersistentDataHolder): Byte? {
            val dataContainer = dataHolder.persistentDataContainer
            return dataContainer.get(TYPE_ID_KEY, BYTE)
        }
        
        fun hasTypeId(dataHolder: PersistentDataHolder): Boolean = getTypeId(dataHolder) != null
        
    }
    
    val dataContainer = dataHolder.persistentDataContainer
    
    fun getTypeId() = dataContainer.get(TYPE_ID_KEY, BYTE)
    
    abstract fun isValid(): Boolean
    
}