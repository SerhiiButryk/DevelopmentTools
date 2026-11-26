package lesson9_object_declaration

import lesson2_simple_class.Person
import java.io.File

/**
 * 'object' keyword declares or creates an object.
 *
 * It plays 3 important roles:
 *
 * 1. Can be used to implement Singleton pattern
 * 2. Can be used to implement Companion object
 * 3. Can be used to declare Anonymous class
 */

/**
 * 1. A singleton object
 */
object Payroll {
    val allEmployees = arrayListOf<Person>()

    fun calculateSalary() {
        for (person in allEmployees) {
            // Calculate salary
        }
    }
}

/**
 * 2. Singleton object which implements an interface
 */
object CaseInsensitiveFileComparator : Comparator<File> {
    override fun compare(file1: File, file2: File): Int {
        return file1.path.compareTo(file2.path, ignoreCase = true)
    }
}

/**
 * Also object classes can be declared inside classes. In this case, only one object will be created.
 * Here comparator is logically placed inside the class which it compares.
 */
data class Person(val name: String) {
    object NameComparator : Comparator<Person> {
        override fun compare(p1: Person, p2: Person): Int {
            return p1.name.compareTo(p2.name)
        }
    }
}

/**
 * 3. Companion object declaration.
 */
class A {
    companion object {
        fun foobar() {
            println("Companion object is called")
        }
    }
}

/**
 * Declaring Factory methods using companion object
 */
class User private constructor(val nickname: String) {

    companion object {
        // Factory method
        fun newSubscribingUser(email: String) = User(email.substringBefore('@'))

        // Factory method
        fun newFacebookUser(accountId: Int) = User(getFacebookName(accountId))

        // Helper method
        private fun getFacebookName(accountId: Int): String {
            return "facebook user"
        }
    }

    override fun toString(): String {
        return "User(nickname='$nickname')"
    }

}

/**
 * Companion object can implement interfaces, have name and extension functions.
 * If object doesn't have name, the default - Companion - is used.
 * Otherwise object fields and methods can be accessed using its name.
 */
class B {
    companion object MyObject {
        fun foobar() {
            println("Companion object is called")
        }
    }
}

interface JSONFactory<T> {
    fun fromJSON(jsonText: String): T
}

class MyPerson(val name: String) {
    companion object : JSONFactory<MyPerson> {
        override fun fromJSON(jsonText: String): MyPerson = MyPerson("my serialized person")
    }
}

fun <T> loadFromJson(factory: JSONFactory<T>) {
    println("loadFromJson() is called")
}

/**
 * Declaring extension function
 */
class MyPerson2(val name: String) {
    companion object
}

fun MyPerson2.Companion.fromJSON(json: String) {
    println("fromJSON() is called")
}

interface MouseListener {
    fun mouseClicked(actionId: Int)
    fun mouseEntered(actionId: Int)
}

class Window {
    private var mouseListeners = mutableListOf<MouseListener>()

    fun addMouseListener(listener: MouseListener) {
        if (!mouseListeners.contains(listener)) {
            mouseListeners.add(listener)
        }
    }

}

fun main() {

    // Accessing fields and methods of Singleton object
    Payroll.allEmployees.add(Person("John"))
    Payroll.calculateSalary()

    // Singleton object can be passed in methods which expects 'Comparator' interface
    val files = listOf(File("/a"), File("/b"))
    println(files.sortedWith(CaseInsensitiveFileComparator))

    // See - https://github.com/google/guice (library for adding dependencies in Kotlin)

    // Accessing companion object
    A.foobar()

    // Using Factory methods
    val user1 = User.newSubscribingUser("bob@gmail.com")
    val user2 = User.newFacebookUser(4)

    println(user1)
    println(user2)

    // Accessing Companion object method
    // 1. Method - using class name
    B.MyObject.foobar()
    // 2. Method - without class name
    B.foobar()

    // Passing companion objects in function
    loadFromJson(MyPerson)

    // Calling extension function
    MyPerson2.fromJSON("my json test string")

    /**
     * Declaring anonimus inner class using object class
     */
    val myWindow = Window()
    myWindow.addMouseListener(
        object : MouseListener {
            override fun mouseClicked(actionId: Int) {
                println("mouseClicked() is called")
            }

            override fun mouseEntered(actionId: Int) {
                println("mouseEntered() is called")
            }
        }
    )

    var counter = 0

    // Note that the class has access to local variables.
    val listener = object : MouseListener {
        override fun mouseClicked(actionId: Int) {
            println("mouseClicked() is called")
            counter++ // Access local variable
        }

        override fun mouseEntered(actionId: Int) {
            println("mouseEntered() is called")
        }
    }

    myWindow.addMouseListener(listener)
}

