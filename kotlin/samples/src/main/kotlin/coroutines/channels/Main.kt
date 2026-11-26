package coroutines.channels

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log

/**
 * Channels
 *
 * There are 4 types of channels:
 * 1. Buffered
 * 2. Unlimited
 * 3. Rendezvous
 * 4. Conflated
 */

@OptIn(ExperimentalCoroutinesApi::class)
fun main() = runBlocking {

    /**
     * Creating a channel
     */

    // has a buffer of 10 elements
    val buffered = Channel<Int>(10)
    // a rendezvous channel, one of send() or receive() calls is always suspended
    val rendezvous = Channel<Int>()
    // has indefinite buffer size
    val unlimited = Channel<Int>(Channel.UNLIMITED)
    // has a buffer size of 1 and every send() call overrides the previous element
    val conflated = Channel<Int>(Channel.CONFLATED)

    /**
     * Channel builder
     */

    val newChannel = produce {
        send(1)
    }

    val channel = Channel<String>(2)

    launch(CoroutineName("Produce1")) {
        log("Sending A1...")
        channel.send("A1")
        log("Sending A2...")
        channel.send("A2")
        log("A done")
    }

    launch(CoroutineName("Produce3")) {
        log("Sending B2...")
        channel.send("B2")
        log("B2 done")
        // Closing channel
        channel.close()
    }

    launch(CoroutineName("Consumer1")) {
        // Thread safe
        for (element in channel) {
            log("Consumed $element")
        }
// Or not thread safe
//            channel.consumeEach { log("Consumed $it") }
    }

    // Produce example

    //The produce function closes the channel whenever the builder coroutine ends in any
    // way (finished, stopped, cancelled). Thanks to this, we will never forget to call close.

    val newChannel1 = genNums()
    val newChannel2 = genNums()

    val mergedChannel = fanIn(listOf(newChannel1, newChannel2))

    mergedChannel.consumeEach { log("New num: $it") }

    log("All done !!!")

}

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.genNums(): ReceiveChannel<Int> = produce {
    var i = 0
    repeat(4) {
        send(i++)
    }
    close()
    log("Channel closed!!!")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> CoroutineScope.fanIn(channels: List<ReceiveChannel<T>>): ReceiveChannel<T> = produce {
    for (channel in channels) {
        launch {
            for (element in channel) {
                send(element)
            }
        }
    }
}