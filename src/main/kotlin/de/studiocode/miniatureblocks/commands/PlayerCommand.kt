package de.studiocode.miniatureblocks.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

abstract class PlayerCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val message: String
            message = if (args.isNotEmpty()) {
                val builder = StringBuilder()
                Arrays.stream(args).forEach { arg -> builder.append(arg).append(" ") }
                builder.substring(0, builder.length - 1)
            } else ""

            return handleCommand(sender, message, args)
        } else println("This command can only be executed from within the game")
        return true
    }

    abstract fun handleCommand(player: Player, message: String, args: Array<out String>): Boolean

}