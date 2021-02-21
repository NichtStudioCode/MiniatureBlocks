package de.studiocode.miniatureblocks.util

import de.studiocode.miniatureblocks.miniature.armorstand.MiniatureArmorStand
import de.studiocode.miniatureblocks.miniature.armorstand.getMiniature
import org.bukkit.Sound
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

const val PREFIX = "§8[§bMiniatureBlocks§8]§r "

fun Player.getTargetEntity(maxDistance: Double, stepSize: Double = 0.25): Entity? {
    return eyeLocation.getEntityLookingAt(maxDistance, this, stepSize = stepSize)
}

fun Player.getTargetMiniature(): MiniatureArmorStand? {
    val entity = getTargetEntity(8.0)
    if (entity is ArmorStand)
        return entity.getMiniature()
    return null
}

fun Player.sendPrefixedMessage(message: String) {
    sendMessage(PREFIX + message)
}

fun Player.kickPlayerPrefix(message: String) {
    kickPlayer(PREFIX + message)
}

fun Player.playSound(sound: Sound, volume: Float, pitch: Float) {
    playSound(location, sound, volume, pitch)
}

fun Player.playBurpSound() {
    playSound(Sound.ENTITY_PLAYER_BURP, 5f, 10f)
}

fun Player.playClickSound() {
    playSound(Sound.UI_BUTTON_CLICK, 5f, 10f)
}