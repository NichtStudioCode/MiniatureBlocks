package de.studiocode.miniatureblocks.resourcepack.model.part.impl

import de.studiocode.miniatureblocks.build.concurrent.AsyncSwitch
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.element.Element
import de.studiocode.miniatureblocks.resourcepack.model.part.Part
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import org.bukkit.Material
import org.bukkit.block.data.FaceAttachable

class SwitchPart(data: AsyncSwitch) : Part() {
    
    private val blockTexture = BlockTexture.of(data.material)
    override val elements = ArrayList<Element>()
    
    init {
        elements += SerializedPart.getModelElements(blockTexture.models!![0], blockTexture.textures)
        elements.removeIf { (data.state && it.name.equals("0")) || (!data.state && it.name.equals("1")) }
        
        if (data.attachedFace == FaceAttachable.AttachedFace.CEILING) rotate(2, 2)
        else if (data.attachedFace == FaceAttachable.AttachedFace.WALL) {
            if (data.material == Material.LEVER) rotate(0, 2)
            rotate(1, 2)
        }
        
        rotate(Direction.of(data.facing))
    }
    
}