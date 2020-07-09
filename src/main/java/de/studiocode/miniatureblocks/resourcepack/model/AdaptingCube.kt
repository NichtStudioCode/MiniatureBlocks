package de.studiocode.miniatureblocks.resourcepack.model

import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

class AdaptingCube(blockTexture: BlockTexture) : Cube(blockTexture) {

    init {
        val defaultRotation = blockTexture.defaultRotation
        rotateAroundXAxis(-defaultRotation.xRot)
        rotateAroundYAxis(-defaultRotation.yRot)
    }

}