package coroutines.sequence

/**
 * A Kotlin sequence generates values on demand or lazily.
 * Sequences are prefect for sources of data whose size might be big or infinite
 * and elements might be heavy, so we want to calculate or read them on demand, lazily.
 */
fun main() {

    // It is essential to know that sequence terminal operations (like forEach) are not suspending,
    // so any suspension (thread sleep) inside a sequence builder means blocking the
    // thread that waits for the value.
    // This is why, in the scope of a sequence builder, you cannot use any suspending function
    // except for those called on the SequenceScope receiver (yield and yieldAll)
    val list = sequence {
        println("Generating one")
        yield(1)
        println("Generating two")
        yield(2)
        println("Generating three")
        yield(3)
    }

    // Values are generated on demand
    for (i in list) {
        println(i)
    }

}