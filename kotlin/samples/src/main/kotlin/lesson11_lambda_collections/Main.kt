package lesson11_lambda_collections

import lesson10_lambda_expressions.Person

/**
 * Lambda functions and collections
 *
 */

fun main() {

    // 1. Map - modifying every element in collection
    val collection1 = listOf(1, 2, 3, 4, 5)
    // Change collection
    val newCollection = collection1.map { element -> element * element }
    println("Old collection: $collection1")
    println("New collection: $newCollection")

    // 2. Filter - remove element in collection if true and add element to new collection if false
    val collection2 = listOf(Person("Tom", 12), Person("John", 23), Person("Mike", 19))

    // Find Persons which are older than 18 years
    val fullYearsPersons = collection2.filter { person -> person.age >= 18 }
    println("Full years person: $fullYearsPersons")

    // 3. all, any, count
    val collection3 = listOf(Person("Serhii", 19), Person("Oleg", 22), Person("Taras", 29), Person("Mykola", 22))

    // true
    println(collection3.all{ it.age < 30 })
    // true
    println(collection3.any{ it.age > 20 })
    // 2
    println(collection3.count{ it.age == 22 })

    // 4. Fold, reduce
    val collection4 = listOf(1,3,5,7,8)
    val collection5 = listOf("1","3","5","7","8")

    // Sum of all numbers
    val sum = collection4.fold(0) { acc, element -> acc + element }
    println(sum)

    val string = collection5.fold("") { acc, element -> acc + element }
    println(string)

    // Reduce doesn't have initial value
    println(collection4.reduce{ acc, element -> acc + element })

    // 5. MaxBy
    println(collection3.maxBy { it.age })

}