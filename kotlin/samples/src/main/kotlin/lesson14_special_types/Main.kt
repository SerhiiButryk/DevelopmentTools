package lesson14_special_types

/**
 * Special types
 *
 * There are 4 special types:
 *
 * 1. Nothing
 * 2. Unit
 * 3. Any
 * 4. Arrays
 *
 * Unlike Java, arrays are classes in Kotlin. They can be compiled in Java wrapper classes (Integer[] ...)
 * or in primitive array types (int[] ...).
 *
 */

fun main() {

    // Arrays in Kotlin
    // This will create array wrapper which will compile in java Integer[] class
    val intArray = arrayOf(1, 2, 3);
    // Create array from 5 elements and initialize them with 1 value
    val intArray2 = Array(5) { i -> 1}

    printArray(intArray)
    printArray(intArray2)

    // Convert Kotlin arrays to java arrays
    val javaArray = intArray.toIntArray()
    println(javaArray)

}

fun <T: Any> printArray(array: Array<T>) {
    println("---")
    for (i in array.indices) {
        println("$i : ${array[i]}")
    }
    println("---")
}