package lesson16_delegation

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Delegation
 *
 * Properties can be decorated using by keyword. This can be useful when you need
 * to reuse getter/setter logic.
 *
 * The next example demonstrates this feature.
 */

// Suppose we have class Person. We need to add formatting
// to 'name' and 'lastname' fields. We can do this using setter/getter.
// However, logic will be duplicated if we don't use delegation.
// Property delegation resolves this issue.
class Person(name: String, lastname: String) {
    var name: String by FormatDelegate()
    var lastname: String by FormatDelegate()
}

class FormatDelegate : ReadWriteProperty<Any?, String> {
    private var formattedString: String = ""

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return formattedString
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        formattedString = value.lowercase().replaceFirstChar { it.uppercase() }
    }
}

/**
 * Delegation to an object
 *
 * Decorator pattern is supported natively by the Kotlin language.
 * The next example demonstrates how to implement decorator around collection in Kotlin.
 *
 * The next class overrides 2 methods 'add' and 'addAll'. All other calls are delegated to collection object.
 */
class CountingSet<T>(
    val innerSet: MutableCollection<T>
) : MutableCollection<T> by innerSet {

    var objectsAdded = 0

    override fun add(element: T): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        objectsAdded += elements.size
        return innerSet.addAll(elements)
    }

}

fun main() {

}