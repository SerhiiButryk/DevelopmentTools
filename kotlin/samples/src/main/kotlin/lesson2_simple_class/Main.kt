package lesson2_simple_class

/**
 * Lesson 1. Properties
 *
 * Properties are data fields plus its setter and getter methods.
 *
 * For accessing a field Kotlin provides 'field' keyword:
 *  set(value) {
 *      field = value
 *  }
 *  get() = field
 *
 * Backing field:
 *
 *  A backing field is generated if a property used at least one default accessor (setter or getter)
 *  or custom setter or getter uses 'field' keyword.
 *
 * Lesson 2. Classes
 *
 *  Classes in Kotlin can have:
 *  1. Object declaration
 *  2. Property declaration
 *  3. Constructors and init blocks
 *  4. Functions
 *  5. Nested and inner classes
 *
 *  Nested classes don't have a ref to outer class.
 *  Inner classes have a ref to outer class.
 *
 *  Lesson 3. Packages
 *
 *  Kotlin doesn't have any restrictions on packages and files.
 *  A file doesn't need to have the same name as the class which it contains.
 *  Moreover, it is possible to place several classes in one file with arbitrary names.
 *
 */
fun main() {
    // Create object
    val person = Person("John", false)

    // Access properties
    println("Name: ${person.name}")
    println("Married: ${person.isMarried}")

    // Modify property
    person.isMarried = true
    println("Married: ${person.isMarried}")

    // Class with custom getter
    val rectangle = Rectangle(100, 100)
    println("Is square: ${rectangle.isSquare}")
}