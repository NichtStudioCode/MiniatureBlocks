package de.studiocode.miniatureblocks.resourcepack.model.element.impl

import de.studiocode.miniatureblocks.resourcepack.model.Direction.*
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.element.Texture
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.point.Point3D

open class TexturedElement(fromPos: Point3D, toPos: Point3D, blockTexture: BlockTexture) :
    Element(
        fromPos, toPos,
        Texture(blockTexture.getTexture(NORTH)),
        Texture(blockTexture.getTexture(EAST)),
        Texture(blockTexture.getTexture(SOUTH)),
        Texture(blockTexture.getTexture(WEST)),
        Texture(blockTexture.getTexture(UP)),
        Texture(blockTexture.getTexture(DOWN))
    )

open class SameTextureElement(fromPos: Point3D, toPos: Point3D, texture: String) :
    Element(
        fromPos, toPos,
        Texture(texture),
        Texture(texture),
        Texture(texture),
        Texture(texture),
        Texture(texture),
        Texture(texture),
    )