package de.studiocode.miniatureblocks.resourcepack.model.element.impl

import de.studiocode.miniatureblocks.resourcepack.model.Direction.*
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

open class TexturedElement(fromPos: DoubleArray, toPos: DoubleArray, blockTexture: BlockTexture) :
    Element(
        fromPos,
        toPos,
        Texture(blockTexture.getTexture(NORTH)),
        Texture(blockTexture.getTexture(EAST)),
        Texture(blockTexture.getTexture(SOUTH)),
        Texture(blockTexture.getTexture(WEST)),
        Texture(blockTexture.getTexture(UP)),
        Texture(blockTexture.getTexture(DOWN))
    )