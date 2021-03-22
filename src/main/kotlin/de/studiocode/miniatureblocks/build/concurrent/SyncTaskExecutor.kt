package de.studiocode.miniatureblocks.build.concurrent

import de.studiocode.miniatureblocks.util.runTask
import java.util.concurrent.CopyOnWriteArrayList

class SyncTaskExecutor {
    
    private val tasks = CopyOnWriteArrayList<Int>()
    private var open = true
    
    fun submit(task: () -> Unit) {
        if (!open) throw IllegalStateException("SyncTaskExecutor is not open")
        
        tasks.add(task.hashCode())
        runTask {
            try {
                task()
                tasks.remove(task.hashCode())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun awaitCompletion() {
        open = false
        while (tasks.isNotEmpty()) {
            Thread.sleep(50)
        }
    }
    
}