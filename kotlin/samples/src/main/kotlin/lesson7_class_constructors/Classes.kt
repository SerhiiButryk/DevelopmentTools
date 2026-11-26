package lesson7_class_constructors

/**
    Kotlin has several ways for declaring a class.

    Eventually, every class can have:

    1. Primary constructor
    2. Secondary constructor
    3. Init blocks (for performing non-trivial initialization operations)
    4. Properties
    5. Methods
    6. Inner and Nested classes
 */

/**
    Declaration 1.

    This is a class with 1 immutable property
 */
class User1(val name: String)

/**
    Declaration 2.

    It equals to Declaration 1.
 */
class User2 constructor(_name: String) {

    // Class property declaration
    val name: String

    // In init block we can perform non-trivial initialization operations
    init {
        name = _name
    }
}

/**
    Declaration 3.

    It equals to Declaration 1, Declaration 2.
 */
class User3 constructor(_name: String) {
    val name = _name
}

/**
    Declaration 4.

    We can use default arguments.
 */
class User4(val name: String, val isSubscribed: Boolean = true)

/*
    When we want to extend class, Kotlin requires to explicitly call
    constructor and initialize properties of super class.

    When super class have single default constructor, we must call it explicitly.
 */
open class Fruit(val name: String)

class Apple(name: String) : Fruit(name)

// .... different hierarchy
open class Button

/*
    Note: we must call default constructor explicitly.
 */
class RadioButton : Button()

class Context

/**
 * Declaration 5.
 *
 * Class which declares no primary constructor and 2 secondary constructors
 */
open class View {

    constructor(ctx: Context) {
        println("Secondary constructor with 1 param")
    }

    constructor(ctx: Context, style: Int) {
        println("Secondary constructor with 2 param")
    }
}

/**
 * Declaration 6.
 *
 * Class constructor delegation
 */
class MyView : View {

    constructor(ctx: Context) : this(ctx, MY_STYLE) {
        println("Secondary constructor with 1 param")
    }

    constructor(ctx: Context, style: Int) : super(ctx, style) {
        println("Secondary constructor with 2 param")
    }

    companion object {
        private const val MY_STYLE: Int = 1
    }

}
