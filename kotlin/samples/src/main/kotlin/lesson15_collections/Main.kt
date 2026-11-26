package lesson15_collections

/**
 * Collections
 *
 * In Kotlin, collections can be
 *
 * 1) Mutable or immutable
 * 3) Nullable or not nullable (can or cannot have null elements)
 *
 * Note that collections passed from Java have a special PLATFORM TYPE.
 *
 * There are three sample collections:
 *
 * 1) Set
 * 2) List
 * 3) Map
 *
 * Collections can be created by the following methods:
 *
 *          |        read only  |   read and write
 * -----------------------------------------------------------------------------------
 * List     |       listOf      |   mutableListOf, arrayListOf
 * Set      |       setOf       |   mutableSetOf, hashSetOf, linkedSetOf, sortedSetOf
 * Map      |       mapOf       |   mutableMapOf, hashMapOf, linkedMapOf, sortedMapOf
 * -----------------------------------------------------------------------------------
 */

fun main() {

    //////////////////////////////////////////////////////
    // Combine elements from several list collections in single one
    //
    // flatten()
    //////////////////////////////////////////////////////

    val list = listOf(listOf(1, 2, 3, 4), listOf(1, 2, 3, 4))

    val newList = list.flatten()

    // The result is a new collection of all elements from list collections
    println(newList)

    //////////////////////////////////////////////////////
    // Transform and combine elements from several list collections in single one
    //
    // flatMap()
    //////////////////////////////////////////////////////

    val list2 = listOf(Person("John", listOf("252-233-23256", "3736-3839-3939")),
        Person("John", listOf("40-24-433-24o43256", "400-4949-5059")))

    val newList2 = list2.flatMap { it.phones }

    println(newList2)

    //////////////////////////////////////////////////////
    // Transform and combine elements from several list collections in single one
    //
    // mapIndexed()
    //////////////////////////////////////////////////////

    val list3 = listOf(1,2,3,4)

    val newList3 = list3.mapIndexed { index, element -> println("$index $element") }

    //////////////////////////////////////////////////////
    // Convert to a map
    //
    // associateBy()
    //////////////////////////////////////////////////////

    val list4 = listOf(Person("John", listOf("3")),
        Person("Andrey", listOf("1")),
        Person("Andrey", listOf("2'")),
        Person("Taras", listOf("2")))

    val map = list4.associateBy { it.name }

    print(map.keys)
    println(map.values)
}

class Person(val name: String, val phones: List<String>)