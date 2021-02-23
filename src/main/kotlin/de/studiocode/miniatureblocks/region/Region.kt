package de.studiocode.miniatureblocks.region

import de.studiocode.miniatureblocks.util.getBoxOutline
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

class Region(player: Player) {
    
    private val uuid = player.uniqueId
    private var lineLocations: HashMap<Location, Boolean>? = null
    
    private var pos1: Location? = null
    private var pos2: Location? = null
    
    private var cubePos1: Location? = null
    private var cubePos2: Location? = null
    
    fun drawOutline() {
        if (isValid()) {
            val player = Bukkit.getPlayer(uuid)
            if (player != null) {
                if (lineLocations == null) {
                    lineLocations = HashMap()
                    pos1!!.getBoxOutline(pos2!!, true).forEach { lineLocations!![it] = false }
                    cubePos1!!.getBoxOutline(cubePos2!!, false).forEach { lineLocations!![it] = true }
                }
                
                lineLocations?.forEach { (location, green) ->
                    ParticleBuilder(ParticleEffect.REDSTONE, location)
                        .setColor(if (green) Color.GREEN else Color.RED)
                        .display(player)
                }
            }
        }
    }
    
    fun isValid(): Boolean {
        return pos1 != null
            && pos2 != null
            && pos1!!.world == pos2!!.world
            && pos1!!.distance(pos2!!) < 1000
    }
    
    private fun makeCube() {
        if (isValid()) {
            val pos1 = this.pos1!!
            val pos2 = this.pos2!!
            
            val minX = min(pos1.x, pos2.x)
            val minY = min(pos1.y, pos2.y)
            val minZ = min(pos1.z, pos2.z)
            val maxX = max(pos1.x, pos2.x) + 1
            val maxY = max(pos1.y, pos2.y) + 1
            val maxZ = max(pos1.z, pos2.z) + 1
            
            val size = doubleArrayOf(maxX - minX, maxY - minY, maxZ - minZ).sortedDescending()[0]
            
            this.cubePos1 = Location(pos1.world, minX, minY, minZ)
            this.cubePos2 = cubePos1!!.clone().add(size, size, size)
        }
    }
    
    fun setFirst(location: Location) {
        this.pos1 = location
        lineLocations = null
        if (isValid()) makeCube()
    }
    
    fun setSecond(location: Location) {
        this.pos2 = location
        lineLocations = null
        if (isValid()) makeCube()
    }
    
    fun getFirst() = cubePos1
    
    fun getSecond() = cubePos2
    
}