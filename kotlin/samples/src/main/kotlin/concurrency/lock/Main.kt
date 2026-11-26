package concurrency.lock

import concurrency.ThreadSafe
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

/**
 * Lock
 *
 * We must use lock to protect our shared resources which are used across multiple threads.
 * We must use granyally or we could have performance issue, or in the worst case we don't
 * benefit from running our program or system on multiple CUP computers
 *
 * There are different types of locks like:
 * 1) Reentrant lock
 * 2) Non-reentrant lock
 * 3) Read-Write lock
 * 4) Spin lock
 * 5) Timed lock
 *
 * This sample demonstrates the '(1)' case
 */

@ThreadSafe
class Person(private val name: String) {

    private val lock = ReentrantLock()

    fun doSomething() {
        println("doSomething() IN")

        lock.lock()

        println("doSomething() got lock, count = " + lock.holdCount)

        try {

            println("doSomething() working...")
            // Simulate some operation
            Thread.sleep(1000)

            process()

        } finally {
            lock.unlock()
            println("doSomething() release lock, count = " + lock.holdCount)
        }
        println("doSomething() OUT")
    }

    private fun process() {
        println("process() IN")
        lock.lock()

        println("process() got lock, count = " + lock.holdCount)

        try {

            println("process() working...")
            // Simulate some operation
            Thread.sleep(1000)

        } finally {
            lock.unlock()

            println("process() released lock, count = " + lock.holdCount)
        }
        println("process() OUT")
    }
}

fun main() {

    val person = Person("John")

    for (i in 0..5) {
        thread {
           println("thread $i started")
           person.doSomething()
            println("thread $i finished")
        }
    }

}