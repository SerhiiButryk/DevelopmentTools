package lesson18_high_order_function

import log
import samples.Person
import kotlin.collections.forEach
import kotlin.collections.remove

/**
 * High order functions
 *
 * 1. Lambda function with a receiver
 * 2. Lambda function without a receiver
 * 3. Anonymous function
 * 4. Non-local return from lambda function
 * 5. Local return from lambda function
 */

fun main() {

    /**
     * 1. Lambda function with a receiver
     */
    val repeatFunc: String.(Int) -> String = {
        // this is a ref to String object
        this.repeat(it)
    }

    /**
     * 2. Lambda function without a receiver
     */
    val repeatFunction2: (String, Int) -> String = repeatFunc

    /**
     * 3. Anonymous function
     */
    val repeatFunction3 = fun(a: String, b: Int): String = a.repeat(b)

    // Invocation
    println("hello".repeatFunc(2))
    println(repeatFunction2("hello", 2))
    println(repeatFunction3("hello", 2))

    /**
     * 4. Local return
     */
    val withAlice = mutableListOf(Person("John"), Person("Alice"), Person("Serhii"))
    findAlice(withAlice)

    /**
     * 5. Non-local return
     */
    findAndRemove(withAlice, "Alice")

    /**
     * Sometimes we can't inline lambda function
     */
    val newClass = ClassSavesLambdaInAFiled()
    newClass.calculate { println("Hello") }
}

// Cannot inline lambda cos it is saved in a member field
class ClassSavesLambdaInAFiled {

    var action: (() -> Unit)? = null

    // We had to add 'noinline' here, otherwise this will not compile
    inline fun calculate(noinline action: () -> Unit) {
        this.action = action
    }
}

// Local return
fun findAlice(list: List<Person>) {
    list.forEach {
        if (it.name == "Alice") {
            log("Alice is found")
            return
        }
        log("Alice not found")
    }
}

// Non-local return
fun findAndRemove(list: MutableList<Person>, name: String) {
    var found: Person? = null
    list.forEach exit@{
        if (it.name == name) {
            found = it
            return@exit
        }
    }
    list.remove(found)
}

// Or
fun findAndRemove2(list: MutableList<Person>, name: String) {
    var found: Person? = null
    list.forEach {
        if (it.name == name) {
            found = it
            return@forEach
        }
    }
    list.remove(found)
}