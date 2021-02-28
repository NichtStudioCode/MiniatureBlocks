package de.studiocode.miniatureblocks.resourcepack.model.element.impl

import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

class SlabElement(blockTexture: BlockTexture) :
    TexturedElement(Point3D(0.0, 0.0, 0.0), Point3D(1.0, 0.5, 1.0), blockTexture)