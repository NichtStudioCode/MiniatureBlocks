package de.studiocode.miniatureblocks.util

import org.bukkit.Sound
import org.bukkit.entity.Player

const val PREFIX = "§8[§bMiniatureBlocks§8]§r "

fun Player.getTargetMiniature() = eyeLocation.getMiniatureLookingAt(8.0)

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