package de.studiocode.miniatureblocks.menu

import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.GUIType
import de.studiocode.invui.item.Item
import de.studiocode.invui.item.ItemBuilder
import de.studiocode.invui.item.impl.BaseItem
import de.studiocode.invui.item.impl.SimpleItem
import de.studiocode.invui.resourcepack.Icon
import de.studiocode.invui.window.impl.single.SimpleWindow
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.model.Direction.*
import de.studiocode.miniatureblocks.resourcepack.model.ModelData.CustomModel
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.searchFor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import java.util.*
import kotlin.collections.HashMap

class TexturesMenu(val player: Player) {
    
    private val materialMenu = MaterialMenu()
    private val directionMenu = DirectionMenu()
    private val texturesMenu = ChooseTextureMenu()
    
    private var material = Material.STONE
    private var direction = NORTH
    
    private var currentMenu = 0
    
    private val modelNames: SortedSet<String> =
        BlockTexture.textureLocations.map { it.replace("block/", "").replace("/", "") }.toSortedSet()
    private val textureModels = HashMap<String, CustomModel>()
    
    init {
        modelNames.forEach {
            textureModels[it] = MiniatureBlocks.INSTANCE.resourcePack.textureModelData.getCustomModelFromName(it)!!
        }
    }
    
    fun openWindow() {
        when (currentMenu) {
            
            0 -> materialMenu.openWindow()
            1 -> directionMenu.openWindow()
            2 -> texturesMenu.openWindow()
            
        }
    }
    
    private fun handleMaterialChoose(material: Material) {
        this.material = material
        currentMenu = 1
        directionMenu.openWindow()
    }
    
    private fun handleDirectionChoose(direction: Direction) {
        this.direction = direction
        currentMenu = 2
        texturesMenu.openWindow()
    }
    
    private fun handleTextureChoose(model: CustomModel) {
        currentMenu = 1
        BlockTexture.changeTextures(material) {
            it.copyOfChange(direction, MiniatureBlocks.INSTANCE.resourcePack.textureModelData.getTextureLocation(model))
        }
        directionMenu.openWindow()
    }
    
    private fun handleBackRequest() {
        if (currentMenu == 1) {
            materialMenu.openWindow()
            currentMenu--
        } else if (currentMenu == 2) {
            directionMenu.openWindow()
            currentMenu--
        }
    }
    
    inner class BackItem : SimpleItem(Icon.ARROW_1_LEFT.itemBuilder.setDisplayName("§7Back")) {
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            handleBackRequest()
        }
    }
    
    inner class MaterialMenu : SearchMenu(player, "Choose block type", false) {
        
        override fun getItems(preview: Boolean, filter: String): List<Item> {
            return BlockTexture.supportedMaterials
                .searchFor(filter) { it.name }
                .map { MaterialItem(it) }
        }
        
        inner class MaterialItem(private val material: Material) : SimpleItem(ItemBuilder(material)) {
            override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
                if (clickType == ClickType.LEFT) handleMaterialChoose(material)
            }
        }
        
    }
    
    inner class DirectionMenu {
        
        private val gui = GUIBuilder(GUIType.NORMAL, 9, 5)
            .setStructure("" +
                "b - - - - - - - 2" +
                "| # # # # e # # |" +
                "| # m # n d s u |" +
                "| # # # # w # # |" +
                "3 - - - - - - - 4")
            .addIngredient('b', BackItem())
            .addIngredient('m', CurrentMaterialItem())
            .addIngredient('n', DirectionItem(NORTH))
            .addIngredient('e', DirectionItem(EAST))
            .addIngredient('s', DirectionItem(SOUTH))
            .addIngredient('w', DirectionItem(WEST))
            .addIngredient('u', DirectionItem(UP))
            .addIngredient('d', DirectionItem(DOWN))
            .build()
        
        fun openWindow() {
            SimpleWindow(player, "Choose side", gui, true, true).show()
        }
        
        inner class CurrentMaterialItem : BaseItem() {
            
            override fun getItemBuilder(): ItemBuilder {
                return ItemBuilder(material).setDisplayName("§7Current material")
            }
            
            override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
                // empty
            }
            
        }
        
        inner class DirectionItem(private val direction: Direction) : BaseItem() {
            
            override fun getItemBuilder(): ItemBuilder {
                val textureLocation = BlockTexture.of(material).getTexture(direction)
                val model = MiniatureBlocks.INSTANCE.resourcePack.textureModelData.getModelByTextureLocation(textureLocation)
                return (model?.createItemBuilder()
                    ?: Icon.CHECKBOX.itemBuilder).setDisplayName("§7Texture: ${direction.name}")
            }
            
            override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
                if (clickType == ClickType.LEFT) handleDirectionChoose(direction)
            }
        }
        
    }
    
    inner class ChooseTextureMenu : SearchMenu(player, "Choose Texture", false, { it.setItem(0, BackItem()) }) {
        
        var material: Material = Material.STONE
        
        override fun getItems(preview: Boolean, filter: String): List<Item> {
            return modelNames
                .searchFor(filter)
                .map { TextureItem(it, textureModels[it]!!) }
        }
        
        inner class TextureItem(name: String, private val model: CustomModel) :
            SimpleItem(model.createItemBuilder().setDisplayName("§7${name}")) {
            override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
                if (clickType == ClickType.LEFT) handleTextureChoose(model)
            }
        }
    }
    
}