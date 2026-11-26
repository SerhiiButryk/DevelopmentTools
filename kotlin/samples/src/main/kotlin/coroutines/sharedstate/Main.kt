package coroutines.sharedstate

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * In order to protect access to the same resource from multiple coroutines running
 * concurrently we have several options:
 *
 * 1. Regular lock object, but it blocks the threads
 * 2. Special Mutex class from coroutine library. It doesn't block the thread but suspends the coroutine
 * 3. Restrict coroutine dispatcher of the critical section
 * 4. Add Semaphore if we want to limit the number of coroutine accessing the resource at the same time
 * 5. Operations with atomic variables
 */

val sharedResource = mutableListOf<String>()
val sharedResource2 = mutableListOf<String>()
val sharedResource3 = mutableListOf<String>()

suspend fun main() = coroutineScope {
    notThreadSafe()
    threadSafe1()
    threadSafe2()

    println("Size = ${sharedResource.size} != 100 000")
    println("Size = ${sharedResource2.size} == 100 000")
    println("Size = ${sharedResource3.size} == 100 000")
}

suspend fun notThreadSafe() = coroutineScope {
    ///////////////////////////
    // Not thread safe !!!
    ////////////////////////////
    repeat(100_000) {
        launch {
            sharedResource.add("New string")
        }
    }
}


suspend fun threadSafe1() = coroutineScope {

    val mutex = Mutex()

    repeat(100_000) {
        launch {
            mutex.withLock {
                sharedResource2.add("New string")
            }
        }
    }
}

val dispatcher = Dispatchers.IO.limitedParallelism(1)

suspend fun threadSafe2() = coroutineScope {
    repeat(100_000) {
        launch {
            // Now this code runs on a single thread and we don't have races
            withContext(dispatcher) {
                sharedResource3.add("New string")
            }
        }
    }
}