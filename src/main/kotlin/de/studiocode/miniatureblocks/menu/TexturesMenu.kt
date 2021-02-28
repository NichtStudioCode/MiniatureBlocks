package de.studiocode.miniatureblocks.menu

import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.GUIType
import de.studiocode.invui.item.Item
import de.studiocode.invui.item.ItemBuilder
import de.studiocode.invui.item.impl.BaseItem
import de.studiocode.invui.item.impl.SimpleItem
import de.studiocode.invui.item.impl.SupplierItem
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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TexturesMenu(val player: Player) {
    
    private val materialMenu = MaterialMenu()
    private val directionMenu = DirectionMenu()
    private val texturesMenu = ChooseTextureMenu()
    
    private var material = Material.STONE
    private var directions = listOf(NORTH)
    
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
    
    private fun handleDirectionChoose(directions: List<Direction>) {
        this.directions = directions
        currentMenu = 2
        texturesMenu.openWindow()
    }
    
    private fun handleTextureChoose(model: CustomModel) {
        currentMenu = 1
        BlockTexture.overrideTextureLocations(material) {
            it.copyOfChange(directions, MiniatureBlocks.INSTANCE.resourcePack.textureModelData.getTextureLocation(model))
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
        
        private val directionItems = ArrayList<Item>()
        
        private val gui = GUIBuilder(GUIType.NORMAL, 9, 5)
            .setStructure("" +
                "b - - - - - - - 2" +
                "| # # # # e # x |" +
                "| # m # n d s u |" +
                "| # # # # w # * |" +
                "3 - - - - - - - 4")
            .addIngredient('b', BackItem())
            .addIngredient('m', CurrentMaterialItem())
            .addIngredient('n', DirectionItem(NORTH).also(directionItems::add))
            .addIngredient('e', DirectionItem(EAST).also(directionItems::add))
            .addIngredient('s', DirectionItem(SOUTH).also(directionItems::add))
            .addIngredient('w', DirectionItem(WEST).also(directionItems::add))
            .addIngredient('u', DirectionItem(UP).also(directionItems::add))
            .addIngredient('d', DirectionItem(DOWN).also(directionItems::add))
            .addIngredient('*', AllDirectionsItem())
            .addIngredient('x', ClearOverridesItem())
            .build()
        
        fun openWindow() {
            SimpleWindow(player, "Choose side", gui, true, true).show()
        }
        
        inner class CurrentMaterialItem : SupplierItem({
            ItemBuilder(material).setDisplayName("§7Current material")
        })
        
        inner class ClearOverridesItem : BaseItem() {
            
            override fun getItemBuilder(): ItemBuilder =
                if (BlockTexture.hasOverrides(material)) Icon.X.itemBuilder.setDisplayName("§7Clear overrides")
                else Icon.BACKGROUND.itemBuilder
            
            override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
                if (clickType == ClickType.LEFT && BlockTexture.hasOverrides(material)) {
                    BlockTexture.removeTextureLocationOverride(material)
                    directionItems.forEach(Item::notifyWindows)
                    notifyWindows()
                }
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
                if (clickType == ClickType.LEFT) handleDirectionChoose(listOf(direction))
            }
        }
        
        inner class AllDirectionsItem : SimpleItem(Icon.HORIZONTAL_DOTS.itemBuilder.setDisplayName("§7All sides")) {
            override fun handleClick(clickType: ClickType?, player: Player?, event: InventoryClickEvent?) {
                if (clickType == ClickType.LEFT) handleDirectionChoose(Direction.values().toList())
            }
        }
        
    }
    
    inner class ChooseTextureMenu : SearchMenu(player, "Choose Texture", false, { gui, main ->
        if (main) gui.setItem(0, BackItem())
        gui.background = Icon.BACKGROUND.itemBuilder
    }) {
        
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