package coroutines.bacis

import kotlinx.coroutines.*
import log
import logDebug
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

/**
 * Coroutines
 *
 * Key concepts:
 *
 * 1. Coroutine is an instance of a code which can be run, suspended or completed
 * 2. Suspensions happen at suspension points.
 * 2. Coroutine scope is an entity inside which coroutines of the same hierarchy live and exist
 * It also makes structural concurrency possible
 * 3. runBlocking(), launch() and async() - coroutine builder functions to create a coroutine
 * 4. Coroutine context is a container of local data for coroutines
 * 5. Coroutines may run concurrently but not always in parallel with other code
 */
fun main() {
    example1_cancellation()
    example2_yield()
    example3_async()
}

/**
 * Cancellation example
 */
fun example1_cancellation() = runBlocking(CoroutineName("Coroutine#1")) {

    logDebug("example1_cancellation() Started")

    val job2 = launch(CoroutineName("Coroutine#3")) {
        logDebug("Running")
        try {
            delay(5000)
        } catch (e: CancellationException) {
            logDebug("Canceled")
            throw e
        }
        logDebug("End")
    }

    job2.invokeOnCompletion {
        if (it == null)
            logDebug("Job2 completed successfully")
        else
            logDebug("Job2 failed")
    }

    delay(1000)

    // Cancels coroutine 3
    job2.cancel()

    logDebug("End")

}

/**
 * yield() example
 */
fun example2_yield() = runBlocking(CoroutineName("Coroutine#1")) {

    logDebug("Running")

    val job1 = launch(CoroutineName("Coroutine#2")) {
        logDebug("Running")
        yield() // Ask to suspend and execute other tasks
        logDebug("Continue")
        yield() // Ask to suspend and execute other tasks
        logDebug("Done!")
    }

    val job2 = launch(CoroutineName("Coroutine#3")) {
        logDebug("Running")
        yield() // Ask to suspend and execute other tasks
        logDebug("Continue")
        yield() // Ask to suspend and execute other tasks
        logDebug("Done!")
    }

    logDebug("End")

}

/**
 * Async example
 */
fun example3_async() = runBlocking(CoroutineName("Coroutine#1")) {

    logDebug("Running")

    val job: Deferred<Int> = async(CoroutineName("Coroutine#2")) {
        compute()
    }

    // Cancels coroutine 2
    val result = job.await()
    logDebug("Result is $result")

    logDebug("End")

}

suspend fun compute(): Int {
    // Long computation
    delay(1000)
    return 10
}

private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor {
    Thread(it, "Schedule-Thread").apply { isDaemon = true }
}

/**
 * Can be used to bridge coroutine and non coroutine code
 *
 * suspendCancellableCoroutine()
 * suspendCoroutine()
 */

suspend fun callSuspendingFunction() {

    log("Start")

    suspendCancellableCoroutine<Unit> { continuation ->
        executor.schedule({
            log("Resume after 1 second")

            continuation.resume(Unit)

            continuation.invokeOnCancellation { log("Invoked on cancellation is called") }

        }, 1000, TimeUnit.MILLISECONDS)
    }

    log("End")

}
