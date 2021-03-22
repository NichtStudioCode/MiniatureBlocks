package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.BuildContext
import de.studiocode.miniatureblocks.build.concurrent.AsyncBeacon
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

class BeaconPart(data: AsyncBeacon, context: BuildContext, point: Point3D) : DefaultPart(data) {
    
    private val beam = SerializedPart.getModelElements("model/beacon_beam", BlockTexture.of(data.material).textures)
    
    init {
        if (data.active) {
            val height = context.maxY.toInt() - point.y.toInt()
            repeat(height + 1) {
                elements += beam.map { element -> element.clone().apply { move(0.0, it.toDouble(), 0.0) } }
            }
        }
    }
    
}