package concurrency.deadlock

import concurrency.DeadlockBug
import log

/**
 * Deadlock.
 *
 * Deadlock is a special case of a liveness issue. Threads are blocked while waiting for a resource
 * held by another thread, preventing the system or subsystem from making progress.
 * Recovery is difficult; the program usually needs to be redesigned or restarted.
 *
 * Deadlock can happen:
 *  1) When threads acquire multiple locks in conflicting orders
 *  2) When a thread fails to release an explicitly acquired lock
 *  3) When a thread recursively enters a non-reentrant lock
 *  4) When a thread waits indefinitely for a condition that is never met
 *  5) When a thread holds a lock while waiting for another thread to complete
 *  6) When there is a circular dependencies on some resource, so each thead waits for each other.
 *
 * The sample demonstrates the '(3)' case.
 */

@DeadlockBug
class Friend(private val name: String) {

    @Synchronized
    fun helloTo(friend: Friend) {
        log("helloTo(\"${friend.name}\") IN")
        Thread.sleep(100)
        friend.helloBack(friend)
        log("helloTo(\"${friend.name}\") OUT")
    }

    @Synchronized
    fun helloBack(friend: Friend) {
        log("helloBack \"${friend.name}\" from ${name}")
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

    val thread2 = Thread {
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