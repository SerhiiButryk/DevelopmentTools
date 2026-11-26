package lesson6_classes

/**
 * The order of initialization
 *
 * 1. Child constructor arguments
 * 2. Parent constructor arguments
 * 2. Parent properties and init blocks
 * 3. Parent constructor body
 * 4. Child properties and init blocks
 * 5. Child constructor body
 */

open class Parent {

    private val prop1 = println("Parent.prop1")

    constructor(arg: Unit = println("Parent constructor arg")) {
        println("Parent(int) {...}")
    }

    init {
        println("Parent init block")
    }

    private val prop2 = println("Parent.prop2")
}

class Child : Parent {

    private val prop1 = println("Child.prop1")

    constructor(arg: Unit = println("Child secondary constructor arg")) : super() {
        println("Child() {...}")
    }

    constructor(value: Int, arg: Unit = println("Child primary constructor arg")) : this(arg) {
        println("Child(int) {...}")
    }

    init {
        println("Child init block")
    }

    private val prop2 = println("Child.prop2")
}


fun main() {
    val child = Child(1)
}