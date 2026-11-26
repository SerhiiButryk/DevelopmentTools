package concurrency.conditional_variable

import concurrency.ThreadSafe
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock
import kotlin.random.Random

/**
 * Condition locks.
 *
 * Demonstrates a thread-safe bounded queue using ReentrantLock conditions.
 *
 * Operations:
 *      - put(data): caller waits if the buffer is full.
 *      - getHeadAndRemove(): caller waits if the buffer is empty.
 *
 */

@ThreadSafe
class Queue(size: Int) {

    private val lock = ReentrantLock()

    private val signalBufferIsEmpty = lock.newCondition()
    private val signalBufferIsFull = lock.newCondition()

    private val buffer = Array(size) { 0 }
    private var lastElementPointer: Int = 0

    fun put(data: Int) {
        lock.withLock {
            println("put() size = ${buffer.size}, elems = $lastElementPointer IN")

            while (lastElementPointer == buffer.size) {
                // Need to wait
                println("put() full, waiting...")
                signalBufferIsFull.await()
            }

            println("put() proceed")

            buffer[lastElementPointer++] = data

            signalBufferIsEmpty.signal()

            println("put() size = ${buffer.size}, elems = $lastElementPointer OUT")
        }
    }

    fun getHeadAndRemove(): Int {
        lock.withLock {

            println("getHeadAndRemove() size = ${buffer.size}, elems = $lastElementPointer IN")

            while (lastElementPointer == 0) {
                // Need to wait
                println("getHeadAndRemove() no elements, waiting...")
                signalBufferIsEmpty.await()
            }

            println("getHeadAndRemove() proceed")

            val first = buffer[0]

            lastElementPointer -= 1

            signalBufferIsFull.signal()

            println("getHeadAndRemove() size = ${buffer.size}, elems = $lastElementPointer OUT")

            return first
        }

    }

}

fun main() {

    val queue = Queue(10)

    for (i in 0..5) {

        thread {
            queue.put(Random(19999999).nextInt(20))
        }

        thread {
            // Wait some time for other threads to put some data
            Thread.sleep(10)
            queue.getHeadAndRemove()
        }
    }

}