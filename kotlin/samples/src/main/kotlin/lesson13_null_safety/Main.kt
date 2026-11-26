package lesson13_null_safety

/**
 * Null safety
 *
 * Kotlin has 4 standard approaches for working with null values:
 *
 * 1. Safe operator - 'a.?fun()'
 * 2. Elvis operator - '?:'
 * 3. Safe cast - 'as?' which doesn't throw exception
 * 4. Let function with safe call
 *
 * Also, there are some additional language features:
 *
 * 1. Null cast (!! operator)
 * 2. Extension function with null value support
 * 3. Operator lateinit
 *
 * Cases when a variable can be null
 *
 * 1. Variable that is declared as T
 * 2. Variable that comes from Java code
 *
 * Note that variables that come from Java have a special PLATFORM TYPE.
 */

fun main() {

    // Type which can have null values
    var message: String? = null

    // 1. Null check
    if (message != null)
    println(message.length)

    // 2. Safe null operator
    // equals to if (s != null) s.length else null
    println(message?.length)

    // 3. Elvis
    // equals to statement - if (message != null) message : "null"
    println(message ?: "null")

    // 4. Safe cast
    val str: Any = "hello"
    val obj = str as? String ?: "hello2"

    // 5. Let function
    val text: String? = "apple"
    // Function body is called only if text is not null
    text?.let { str -> println("$str") }

    // 6. Null cast
    val str2: String? = "lemon"
    val str3: String = str2!!

    // 6. Extension function with null value support
    var str4: String? = "hello"
    // Note you don't need safe operator here - '?'
    str4.isNullOrEmpty()

    // 7. Lateinit
    lateinit var nyname: String;
    nyname = "Tom"

    // 4. Type parameters can have null values
    myFunc(null)
    // Cannot have null values
    myFunc2("hello")
}

fun <T> myFunc(value: T) {
    println(value)
}

fun <T: Any> myFunc2(value: T) {
    println(value)
}