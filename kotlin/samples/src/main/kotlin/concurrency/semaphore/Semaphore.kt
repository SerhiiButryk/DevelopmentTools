package concurrency.semaphore

import log
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread

/**
 * Semaphore
 *
 * Semaphore limits the number of threads which can simultaneously access a resource.
 *
 * The sample demonstrates the simplest usage of Semaphore.
 */
class Resource {

    private val threadAccessGuard = Semaphore(1)

    fun getResource() {
        log("accessResource()")
        threadAccessGuard.acquire()
        log("accessResource() got a resource")
    }

    fun returnResource() {
        log("returnResource()")
        threadAccessGuard.release()
        log("returnResource() returned a resource")
    }
}

fun main() {
    // Simulate creation of some resource
    val resource = Resource()
    // Launch threads and try to access a resource concurrently
    for (i in 1..10) {
        thread {
            resource.getResource()
            Thread.sleep(100)
            resource.returnResource()
        }
    }
}