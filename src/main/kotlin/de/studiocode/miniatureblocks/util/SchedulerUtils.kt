package de.studiocode.miniatureblocks.util

import de.studiocode.miniatureblocks.MiniatureBlocks
import org.bukkit.Bukkit

fun runTaskLater(delay: Long, run: () -> Unit) =
    Bukkit.getScheduler().runTaskLater(MiniatureBlocks.INSTANCE, run, delay)

fun runTask(run: () -> Unit) =
    Bukkit.getScheduler().runTask(MiniatureBlocks.INSTANCE, run)

fun runTaskTimer(delay: Long, period: Long, run: () -> Unit) =
    Bukkit.getScheduler().runTaskTimer(MiniatureBlocks.INSTANCE, run, delay, period)

