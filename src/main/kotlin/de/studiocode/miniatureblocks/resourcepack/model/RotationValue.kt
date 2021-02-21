package de.studiocode.miniatureblocks.resourcepack.model

import kotlin.reflect.KProperty

class RotationValue {
    
    private var rotation = 0
    
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return rotation
    }
    
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        rotation = value % 4
        if (rotation < 0) this.rotation += 4
    }
    
}