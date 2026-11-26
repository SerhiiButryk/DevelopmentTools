package samples.delegation_example

import log
import kotlin.reflect.KProperty

/**
 * Delegation example.
 */
fun main(args: Array<String>) {
    val person = Person("John", 25, 100)
    // get values, calls getValue()
    person.print()

    // set values, calls setValue()
    person.age = 33
    person.salary = 200
}

abstract class Observer {
    abstract fun <T: Any> onChange(fieldName: String, oldValue: T, newValue: T)
}

class Person(val name: String, age: Int, salary: Int) {

    var salary: Int by ObservableProperty<Person, Int>(salary, PersonObserver<Int>())

    var age: Int by ObservableProperty<Person, Int>(age, PersonObserver<Int>())

    fun print() {
        log("$name , $salary , $age")
    }
}

class PersonObserver<T> : Observer() {
    override fun <T : Any> onChange(fieldName: String, oldValue: T, newValue: T) {
        log("PersonObserver.onChange(), $fieldName, $oldValue, $newValue")
    }
}

class ObservableProperty<T: Any, V: Any>(private var propertyValue: V, private val observer: Observer) {

    operator fun getValue(observableObject: T, property: KProperty<*>): V {
        log("ObservableProperty.getValue()")
        return propertyValue
    }

    operator fun setValue(observableObject: T, property: KProperty<*>, newValue: V) {
        log("ObservableProperty.setValue()")
        val oldValue = propertyValue
        propertyValue = newValue
        observer.onChange(property.name, oldValue, newValue)
    }

}