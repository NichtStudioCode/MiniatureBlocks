package de.studiocode.miniatureblocks.menu

import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.GUIType
import de.studiocode.invui.gui.impl.SimplePagedItemsGUI
import de.studiocode.invui.item.Item
import de.studiocode.invui.item.ItemBuilder
import de.studiocode.invui.item.impl.BaseItem
import de.studiocode.invui.item.impl.SimpleItem
import de.studiocode.invui.item.impl.SuppliedItem
import de.studiocode.invui.resourcepack.Icon
import de.studiocode.invui.window.impl.single.SimpleWindow
import de.studiocode.miniatureblocks.MiniatureBlocks
import de.studiocode.miniatureblocks.resourcepack.file.ModelFile.CustomModel
import de.studiocode.miniatureblocks.resourcepack.model.Direction
import de.studiocode.miniatureblocks.resourcepack.texture.BlockTexture
import de.studiocode.miniatureblocks.util.hasSixTextures
import de.studiocode.miniatureblocks.util.searchFor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent

class TexturesMenu(val player: Player) {
    
    private var material = Material.STONE
    private var textureIndices = listOf(0)
    
    private val materialMenu = MaterialMenu()
    private val directionMenu = DirectionMenu()
    private val texturesMenu = ChooseTextureMenu()
    
    private var currentMenu = 0
    
    private val textureModels = HashMap<String, CustomModel>()
    
    init {
        BlockTexture.sortedTextureLocations.forEach {
            textureModels[it] = MiniatureBlocks.INSTANCE.resourcePack.textureModelData.getModelByTextureLocation(it)!!
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
    
    private fun handleDirectionChoose(indices: List<Int>) {
        this.textureIndices = indices
        currentMenu = 2
        texturesMenu.openWindow()
    }
    
    private fun handleTextureChoose(model: CustomModel) {
        currentMenu = 1
        BlockTexture.overrideTextureLocations(material) {
            it.copyOfChange(textureIndices, MiniatureBlocks.INSTANCE.resourcePack.textureModelData.getTextureLocation(model))
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
        
        inner class MaterialItem(private val material: Material) : SimpleItem(MATERIALS[material]!!) {
            override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
                if (clickType == ClickType.LEFT) handleMaterialChoose(material)
            }
        }
        
    }
    
    inner class DirectionMenu {
        
        private val directionItems = ArrayList<Item>()
        private var listItems: List<Item> = ArrayList()
        
        private val cubeGui = GUIBuilder(GUIType.NORMAL, 9, 5)
            .setStructure("" +
                "b - - - - - - - 2" +
                "| # # # # e # x |" +
                "| # m # n d s u |" +
                "| # # # # w # * |" +
                "3 - - - - - - - 4")
            .addIngredient('b', BackItem())
            .addIngredient('m', CurrentMaterialItem())
            .addIngredient('n', TextureItem(0).also(directionItems::add))
            .addIngredient('e', TextureItem(1).also(directionItems::add))
            .addIngredient('s', TextureItem(2).also(directionItems::add))
            .addIngredient('w', TextureItem(3).also(directionItems::add))
            .addIngredient('u', TextureItem(4).also(directionItems::add))
            .addIngredient('d', TextureItem(5).also(directionItems::add))
            .addIngredient('*', AllDirectionsItem())
            .addIngredient('x', ClearOverridesItem())
            .build()
        
        private val miscGui = (GUIBuilder(GUIType.PAGED_ITEMS, 9, 5)
            .setStructure("" +
                "b - - - - - - - 2" +
                "| # # # x x x x |" +
                "| # m # x x x x |" +
                "| # # # < c > * |" +
                "3 - - - - - - - 4")
            .addIngredient('b', BackItem())
            .addIngredient('m', CurrentMaterialItem())
            .addIngredient('*', AllDirectionsItem())
            .addIngredient('c', ClearOverridesItem())
            .build() as SimplePagedItemsGUI)
            .apply { background = Icon.BACKGROUND.itemBuilder }
        
        fun openWindow() {
            if (material.hasSixTextures()) {
                SimpleWindow(player, "Choose side", cubeGui, true, true).show()
            } else {
                listItems = BlockTexture.of(material).textures.indices.map { TextureItem(it) }
                miscGui.setItems(listItems)
                SimpleWindow(player, "Choose texture", miscGui, true, true).show()
            }
        }
        
        inner class CurrentMaterialItem : SuppliedItem({
            MATERIALS[material]!!.clone().setDisplayName("§7Current material")
        }, null)
        
        inner class ClearOverridesItem : BaseItem() {
            
            override fun getItemBuilder(): ItemBuilder =
                if (BlockTexture.hasOverrides(material)) Icon.X.itemBuilder.setDisplayName("§7Clear overrides")
                else Icon.BACKGROUND.itemBuilder
            
            override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
                if (clickType == ClickType.LEFT && BlockTexture.hasOverrides(material)) {
                    BlockTexture.removeTextureLocationOverride(material)
                    (directionItems + listItems).forEach(Item::notifyWindows)
                    notifyWindows()
                }
            }
            
        }
        
        inner class TextureItem(private val index: Int) : BaseItem() {
            
            override fun getItemBuilder(): ItemBuilder {
                val textureLocation = BlockTexture.of(material).textures[index]
                val model = MiniatureBlocks.INSTANCE.resourcePack.textureModelData.getModelByTextureLocation(textureLocation)
                val name = if (material.hasSixTextures()) "§7Texture ${Direction.values()[index].name}" else "§7Texture ${1 + index}"
                return (model?.createItemBuilder() ?: Icon.CHECKBOX.itemBuilder).setDisplayName(name)
            }
            
            override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
                if (clickType == ClickType.LEFT) handleDirectionChoose(listOf(index))
            }
        }
        
        inner class AllDirectionsItem : SimpleItem(Icon.HORIZONTAL_DOTS.itemBuilder.setDisplayName("§7All sides")) {
            override fun handleClick(clickType: ClickType?, player: Player?, event: InventoryClickEvent?) {
                if (clickType == ClickType.LEFT) handleDirectionChoose((BlockTexture.of(material).textures.indices).toList())
            }
        }
        
    }
    
    inner class ChooseTextureMenu : SearchMenu(player, "Choose Texture", false, { gui, main ->
        if (main) gui.setItem(0, BackItem())
        gui.background = Icon.BACKGROUND.itemBuilder
    }) {
        
        var material: Material = Material.STONE
        
        override fun getItems(preview: Boolean, filter: String): List<Item> {
            return BlockTexture.sortedTextureLocations
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
    
    companion object {
        
        private val MATERIALS: Map<Material, ItemBuilder>
        
        init {
            val materials = HashMap<Material, ItemBuilder>()
            
            val materialModelData = MiniatureBlocks.INSTANCE.resourcePack.materialModelData
            BlockTexture.supportedMaterials
                .forEach { material ->
                    if (material.isItem) {
                        materials[material] = ItemBuilder(material)
                    } else {
                        materials[material] = materialModelData.getModelByMaterial(material)!!.createItemBuilder()
                    }
                }
            
            MATERIALS = materials
        }
        
    }
    
}