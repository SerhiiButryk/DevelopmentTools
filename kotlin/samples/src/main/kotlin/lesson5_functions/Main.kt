package lesson5_functions

import java.lang.IllegalArgumentException
import kotlin.text.StringBuilder

/**
 * Kotlin has many brilliant features when working with functions.
 *
 * 1) Named arguments
 *
 *   You can assign names to function arguments during invocation.
 *   This helps make code more readable and avoid mistakes.
 *   For example, typical mistakes occur when the parameter order has changed but the argument order hasn't.
 *   This feature is very helpful when a function has default arguments.
 *
 * 2) Default arguments
 *
 *   You can specify default argument for function parameters.
 *   However, you should explicitly provide all arguments to function when calling it from Java code.
 *   To avoid this you can use @JvmOverloads annotation.
 *
 *  3) Extension functions
 *
 *   These are functions that can be invoked like class member functions, even though they're declared outside a class.
 *   Imagine when you have a lot of Java code that can't be rewritten to Kotlin.
 *   If you want to use Kotlin's power in Java code, extension functions are a way to do this.
 *
 *   Rules:
 *      1) Extension functions has access only to public member of a class (can't call protected or private methods)
 *
 *      2) Extension functions can be used in Java and Kotlin classes
 *
 *      3) Extension functions can't be overridden. You can create an extension function in base and subclasses with
 *      the same name and parameters. However, if you try to call it, the function will be invoked
 *      depending on a static type of variable, but not on a dynamic type.
 *
 *   Extension function should be imported. Also function name can be changed.
 *   For example, suppose we have 'lastChar' extension function. It conflicts with already existed name in a file.
 *   To resolve this we can do the next:
 *
 *   import strings.lastChar as last - Changing extension function name
 *
 *   4) Property-extension
 *
 *   You can create property extension. It looks like extension functions.
 *   You must define read method. Additionally, you can add a write method.
 *
 *   5) varargs
 *
 *   Kotlin has 'varargs' keyword as Java has. However, it has a little more features.
 *   It has automation repack feature support for arrays.
 *
 *   6) Infix functions
 *
 *   Basically, this is syntax sugar. It is enhanced method invocation.
 *   To use this, you need to declare function using 'infix' keyword.
 *
 *   For example, consider this code:
 *   val map = mapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
 *
 *   'to' is not a keyword. It's an infix function call.
 *   It is the same as:
 *   1.to("one")
 *
 *   See the declaration of "to" method:
 *   public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)
 *
 *   7) Local functions
 *
 *   You can define a function inside a function. This can help to avoid code duplication
 *   and improve code structure.
 */

/**
 * The function changes default output of toString() method for collections
 *
 * First implementation (not an extension function).
 */
fun <T> joinToString(
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {

    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

/**
 * The function changes default output of toString() method for collections
 *
 * Second implementation (an extension function).
 */
fun Collection<String>.join(
    separator: String = ", ", prefix: String = "",
    postfix: String = ""
) = joinToString(this, separator, prefix, postfix)

/**
 * Simple extension function for String class
 * String is receiver object
 */
fun String.myLastIndex(): Char = get(length - 1)

/**
 * Simple property extension for StringBuilder
 */
var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value) {
        this.setCharAt(length - 1, value)
    }

/**
 * Simple vararg function
 */
fun countItems(vararg elements: String) {
    /* Here elements are Java array*/
    println("countItems() class: ${elements.javaClass}")
    // Print values
    for (elem in elements) {
        println("Element: $elem")
    }
}

/**
 * Local function example
 *
 * Suppose we have user. We need to save it to database.
 * Let's see how it could be implemented.
 *
 * Note how it looks beautifully.
 */

class User(val id: Int, val name: String, val address: String)

// Extension function
fun User.validateBeforeSave() {
    // Local function
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException("Can't save user $id, empty $fieldName")
        }
    }
    // Check arguments
    validate(name, "Name")
    validate(address, "Address")
}

fun saveUser(user: User) {
    // Need to validate input arguments
    user.validateBeforeSave()
    // Suppose here we are done
    println("User is saved to database")
}

// Main function
fun main() {
    val list = listOf(1, 2, 3)

    // Call with default arguments
    println(joinToString(list))

    // Or with named arguments
    println(joinToString(list, separator = ";", prefix = "[", postfix = "]"))

    // Use extension function
    val lastChar = "hello".myLastIndex()
    println(lastChar)

    val list2 = listOf("lemon", "apple", "cherry")

    // Use extension function
    println(list2.join())

    // Use property extension
    val sb = StringBuilder("Kotlin?")
    sb.lastChar = '!'
    println(sb)

    // Call vararg function. Note how array can be passed as argument.
    val otherElements: Array<String> = arrayOf("elem2", "elem3")
    countItems("elem1", *otherElements)
}