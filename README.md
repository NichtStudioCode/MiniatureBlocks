[![Banner](https://i.imgur.com/4loR9s4.png)](https://www.spigotmc.org/resources/81295/)

<p align="center">
  <a href="https://www.codacy.com/manual/NichtStudioCode/MiniatureBlocks?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=NichtStudioCode/MiniatureBlocks&amp;utm_campaign=Badge_Grade">
    <img src="https://app.codacy.com/project/badge/Grade/d429311eb29747dfa00505202b3d69ab">
  </a>
  <a href="https://www.spigotmc.org/resources/81295/reviews">
    <img src="https://img.shields.io/spiget/rating/81295?color=brightcreen"> 
  </a>
  <a href="https://www.spigotmc.org/resources/81295/">
    <img src="https://img.shields.io/spiget/downloads/81295"> 
  </a>
  <a href="https://www.spigotmc.org/resources/81295/">
    <img src="https://img.shields.io/spiget/tested-versions/81295"> 
  </a>
</p>

# MiniatureBlocks

A [spigot plugin](https://www.spigotmc.org/resources/miniatureblocks.81295/) which allows in-game creation of custom
models

## How to use

### 1. Create a selection

Just like in WorldEdit, you’ll need to mark two positions as the start and the end point. You can do that by either
left- /right-clicking with the marker which you can receive via `/miniature selection marker` or by using the
command `/miniature selection <pos1/pos2>`

### 2. Create a miniature

To create a miniature just type `/miniature create <name>`. The resource pack will automatically get generated and you
will have to accept the custom resource pack request. If you need to create many miniatures without having to download
the resource pack every time, you can also use `/miniature create <name> silent`. In order to properly see the miniature
later you will have to rejoin.

### 3. Place the miniature

Use `/miniatures` or `/ms` to open an inventory with all available miniatures. After placing it, you can also rotate the
miniature in 30° steps by right-clicking. For more precise control over the rotation,
use `/miniature rotation <degrees>` and `/miniature norotate <on/off>` to prevent rotation by right-clicking.

### Animated miniatures

To create an animated miniature open the animation menu using `/miniature animation`. Then, add the miniatures you want
to use as "frames" in the animation; change the tick delay between the animation frames if you want to and create the
animation using the "Create Animation" button. If you want to edit an existing animation, just hold the animated item in
your hand and run the command as usual.

### Texture overriding

You are also able to change the individual textures displayed in the miniatures.
`/miniature textures` opens a GUI where you can individually change the textures for each side of every block type. To
upload your own textures use `/miniature textures add <name> <url>`
/ `/miniature textures addanimated <name> <frametime> <url>` and `/miniature textures remove <name>` to delete it.

### Auto-rotation

If you want your miniature to automatically rotate all the time, you can use the
command `/miniature autorotate <degrees per tick>` to do that. (20 ticks are 1 second)

### Add a command

You can add a command to the miniature which will be executed when a player right or shift-right clicks it.
Use `/miniature command set <right/shift-right> <command>` to set it and `/miniature command remove <right/shift-right>`
to remove it.
