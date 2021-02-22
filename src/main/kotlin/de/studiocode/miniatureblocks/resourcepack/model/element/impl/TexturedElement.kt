package de.studiocode.miniatureblocks.resourcepack.model.element.impl

import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture

open class TexturedElement(fromPos: DoubleArray, toPos: DoubleArray, blockTexture: BlockTexture) :
    Element(
        fromPos,
        toPos,
        Texture(blockTexture.textureFront),
        Texture(blockTexture.textureRight),
        Texture(blockTexture.textureBack),
        Texture(blockTexture.textureLeft),
        Texture(blockTexture.textureTop),
        Texture(blockTexture.textureBottom)
    )