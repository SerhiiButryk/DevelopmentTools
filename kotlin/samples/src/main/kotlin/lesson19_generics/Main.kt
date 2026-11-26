package lesson19_generics

import java.lang.IllegalArgumentException

/**
 * Generics
 *
 * 1. Type names cam be used in classes, methods, interfaces and extensions function & properties
 * 2. You can't create a generic property which isn't an extension
 * 3. Reified keyword can be used only with inline functions.
 *
 * All generic classes are invariant by default.
 * This means that a Box<String> is not a subtype of Box<Any>.
 *
 * 1) Covariance (out)
 *
 * If Cat is a subtype of Animal, then Producer<Cat> becomes a valid subtype of Producer<Animal>.
 * So we can pass Provider<Cat> as an argument to a function which expect Consumer<Animal>
 *
 * 2) Contravariance (in)
 *
 * If String is a subtype of Any, then Consumer<Any> becomes a subtype of Consumer<String>
 * So we can pass Consumer<Any> as an argument to a function which expect Consumer<String>
 *
 * 3) Invariant
 *
 * No type and subtype relationships. The passed generic class should match strictly.
 *
 */

fun main() {

    // 1. Generic type is inferred by compiler
    val listA = listOf(1, 2, 4)
    printSum(listA)

    // 2. Type checking
    println(listA.isType<String>())
    println(listA.isType<Int>())

    printClass<String>()

}

fun printSum(c: Collection<*>) {
    // This is impossible to know at runtime what the actual type of elements in the
    // list because of type eraser , that's why this type of check are unsafe
    val list = c as? List<Int> ?: throw IllegalArgumentException("List is expected")
    println("Sum: ${list.sum()}")
}

/**
 * You can check value type!
 */
inline fun <reified T> List<*>.isType(): Boolean {
    for (element in this) {
        return element is T
    }
    return false
}

/**
 * This code doesn't compile
 */
//fun <T> List<*>.isType2(): Boolean {
//    for (element in this) {
//        if (element is T) {
//            return true
//        }
//    }
//    return false
//}

/**
 * Another example
 */
inline fun <reified T> printClass() {
    println(T::class.java.canonicalName)
}