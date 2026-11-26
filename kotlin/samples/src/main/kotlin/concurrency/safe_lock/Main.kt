package concurrency.safe_lock

import concurrency.ThreadSafe
import log
import java.util.concurrent.locks.ReentrantLock

/**
 * Protection against deadlocks
 *
 * The idea is to check ahead of time if we can enter a deadlock state.
 * If we can then we should release locks and update our state if applicable
 * and retry (ideally with exponential backoff policy) or just stop an operation.
 *
 * The sample demonstrates the simplest variant of such technic.
 */

@ThreadSafe
class Friend(private val name: String) {

    private val myLock = ReentrantLock()

    fun helloTo(friend: Friend) {
        log("helloTo(\"${friend.name}\") IN")
        Thread.sleep(10) // Simulate a long-running operation
        if (canHello(friend)) {
            try {
                log("helloTo(\"${friend.name}\") ready for hello")
                friend.helloBack(friend)
            } finally {
                myLock.unlock()
                friend.myLock.unlock()
            }
        } else {
            log("helloTo(\"${friend.name}\") cannot acquire lock, return")
        }
        log("helloTo(\"${friend.name}\") OUT")
    }

    fun helloBack(friend: Friend) {
        log("helloBack \"${friend.name}\" from ${name}")
    }

    private fun canHello(friend: Friend): Boolean {

        var canGetMyLock = false
        var canGetYourLock = false

        try {

            canGetMyLock = myLock.tryLock()
            canGetYourLock = friend.myLock.tryLock()

        } finally {
            if (!(canGetMyLock && canGetYourLock)) {

                if (canGetMyLock) {
                    myLock.unlock()
                }

                if (canGetYourLock) {
                    friend.myLock.unlock()
                }
            }

        }

        return canGetMyLock && canGetYourLock
    }
}

fun main() {
    log("samples.main() IN")

    val alice = Friend("Alice")
    val john = Friend("John")

    val thread1 = Thread {
        log("Thread 1 is started")
        alice.helloTo(john)
        log("Thread 1 is finished")
    }

    thread1.start()

    val thread2 = Thread() {
        log("Thread 2 is started")
        john.helloTo(alice)
        log("Thread 2 is finished")
    }

    thread2.start()

    log("samples.main() OUT")
}

fun logTreadState(thread: Thread) {
    log(
        "tid = ${thread.id} " +
                "isAlive = ${thread.isAlive} " +
                "state = ${thread.state} " +
                "isDaemon = ${thread.isDaemon} " +
                "priority = ${thread.priority}"
    )
}