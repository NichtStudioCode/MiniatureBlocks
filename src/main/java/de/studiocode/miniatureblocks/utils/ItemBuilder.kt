package de.studiocode.miniatureblocks.utils

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

class ItemBuilder(var material: Material, var amount: Int = 1, var damage: Int = 0, var customModelData: Int? = null,
                  var name: String? = null, var lore: ArrayList<String> = ArrayList(),
                  var enchantments: HashMap<Enchantment, Pair<Int, Boolean>> = HashMap(),
                  var itemFlags: ArrayList<ItemFlag> = ArrayList()) {

    fun addLoreLine(line: String): ItemBuilder {
        lore.add(line)
        return this
    }

    fun addEnchantment(i: Int, b: Boolean, vararg enchantments: Enchantment) {
        enchantments.forEach { this.enchantments[it] = i to b }
    }

    fun addItemFlag(vararg itemFlags: ItemFlag) {
        this.itemFlags.addAll(itemFlags)
    }

    fun build(): ItemStack {
        val itemStack = ItemStack(material, amount)
        val itemMeta = itemStack.itemMeta!!
        if (name != null) itemMeta.setDisplayName(name)
        if (itemMeta is Damageable) (itemMeta as Damageable).damage = damage
        if (customModelData != null) itemMeta.setCustomModelData(customModelData)
        itemMeta.lore = lore
        enchantments.forEach { (enchantment, pair) -> itemMeta.addEnchant(enchantment, pair.first, pair.second) }
        itemMeta.addItemFlags(*itemFlags.toTypedArray())

        itemStack.itemMeta = itemMeta
        
        return itemStack
    }

}
