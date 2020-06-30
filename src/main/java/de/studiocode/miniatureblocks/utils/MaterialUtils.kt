package de.studiocode.miniatureblocks.utils

import org.bukkit.Material

fun Material.isSeeTrough(): Boolean {
    return MaterialUtils.seeTroughMaterials.contains(this)
}

object MaterialUtils {

    val seeTroughMaterials = ArrayList<Material>()

    init {
        seeTroughMaterials.addAll(Material.values().filter { it.toString().contains("glass", true) })
        seeTroughMaterials.add(Material.AIR)
    }


}
