package lesson4_exceptions

import java.io.BufferedReader
import java.io.StringReader
import java.lang.NumberFormatException

/**
 * Exceptions
 *
 * Unlike Java, Kotlin doesn't distinguish between checked and unchecked exceptions.
 * All exceptions are unchecked. You can handle or skip them.
 * Also, 'throw' clause is a statement in Kotlin, which can return value.
 * Pretty much like 'if-else' statement.
 *
 */
fun main() {

    // Throw exception
    val percentage = 100
    if (percentage !in 1..100) {
        throw IllegalArgumentException("A percentage value must be between 0 and 100")
    }

    // Different way
    val inputParam = 100
    // Assign value or throw exception
    val result = if (inputParam in 1..100) inputParam
    else throw IllegalArgumentException("A percentage value must be between 0 and 100")

    // Call method which catches exceptions inside
    readInt(BufferedReader(StringReader("hello"))) // Got exception
    readInt(BufferedReader(StringReader("1"))) // Succeeded
}

// Handle exception in method
fun readInt(bufferReader: BufferedReader) {
    val numb = try {
        println("Try block is executed")
        Integer.parseInt(bufferReader.readLine())
    } catch (e: NumberFormatException) {
        println("Got exception: $e")
        return // Failed to get number
    } finally {
        println("Final block is executed")
    }
    println(numb)
}