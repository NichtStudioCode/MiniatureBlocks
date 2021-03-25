package de.studiocode.miniatureblocks.build.concurrent

import de.studiocode.miniatureblocks.util.runTask
import java.util.concurrent.ConcurrentHashMap

class SyncTaskExecutor {
    
    private val tasks = ConcurrentHashMap.newKeySet<() -> Unit>()
    
    fun submit(task: () -> Unit) {
        tasks.add(task)
        runTask {
            try {
                task()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            tasks.remove(task)
        }
    }
    
    fun awaitCompletion() {
        while (tasks.isNotEmpty()) {
            Thread.sleep(50)
        }
    }
    
}