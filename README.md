[![Codacy Badge](https://app.codacy.com/project/badge/Grade/d429311eb29747dfa00505202b3d69ab)](https://www.codacy.com/manual/NichtStudioCode/MiniatureBlocks?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=NichtStudioCode/MiniatureBlocks&amp;utm_campaign=Badge_Grade)
[![Rating](https://img.shields.io/spiget/rating/81295?color=brightcreen)](https://www.spigotmc.org/resources/miniatureblocks.81295/reviews)
[![Downloads](https://img.shields.io/spiget/downloads/81295)](https://www.spigotmc.org/resources/miniatureblocks.81295/)
[![TestedVersions](https://img.shields.io/spiget/tested-versions/81295)](https://www.spigotmc.org/resources/miniatureblocks.81295/)

# MiniatureBlocks

A [spigot plugin](https://www.spigotmc.org/resources/miniatureblocks.81295/) which allows in-game creation of custom
models

## How to use

### 1. Teleport to the miniature building world

The miniature building world is the place where you can build the miniatures. Teleport there by executing the command **
/miniatureworld** or **/mw**

### 2. Create a miniature

After building something, you can use the command **/miniature create <name>** to create a new miniature. The resource
pack will automatically get generated and you will have to accept the custom resource pack request.

### 3. Place the miniature

Once you are back in the overworld, use **/miniatures** or **/ms** to open an inventory with all available miniatures.
After placing it, you can also rotate the miniature in 45° steps by right-clicking.

### 4. (optional) Turn on auto-rotation

If you want your miniature to automatically rotate all the time, you can use the command **/miniature
autorotate <degrees per tick>** to do that. (20 ticks are 1 second)

### 5. (optional) Add a command

You can add a command to the miniature which will be executed when a player right or shift-right clicks it. Use **
/miniature command set <right/shift-right> <command>** to set it and **/miniature command remove <right/shift-right>**
to remove it.
