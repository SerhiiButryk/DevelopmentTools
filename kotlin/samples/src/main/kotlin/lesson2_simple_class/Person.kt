package lesson2_simple_class

/**
 *  Data class example.
 *
 *  Compiler generates functions:
 *
 *  1. equals() & hashCode()
 *  2. toString()
 *  3. componentN()
 *  4. copy()
 *  5. Setters & getters if it's applicable for properties
 *
 *  Restrictions:
 *
 *  1. Data classes can inherit abstract classes, other classes and implement interfaces, but
 *  it can't inherit other Data classes
 *  2. varargs are not allowed (due to some JVM limitations)
 *  3. You can't define custom copy & componentN implementations
 */
data class Person(val name: String = "", var isMarried: Boolean = false)