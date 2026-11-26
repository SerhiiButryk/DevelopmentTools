package lesson8_properties

/**
 * Overriding of properties
 */

interface User {

    // Abstract property
    val email: String

    // Non-abstract property
    val nickname: String
        get() = email.substringBefore('@')
}

// Property is explicitly overridden by derived class
class PrivateUser(override val email: String) : User

class SubscribeUser(val userId: String) : User {
    // Suppose 'getUserEmail' method can perform expencive database or network call
    override val email: String = getUserEmail(userId)

    private fun getUserEmail(userId: String): String = "my user email"
}

/**
 * 'filed' keyword
 */
class MyUser(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            println(
                """
            Address was changed for $name:
            "$field" -> "$value"
        """.trimIndent()
            )
            field = value
        }
}

/**
 * 3. Changing visibility of 'set' method
 */
class LengthCounter {
    // This property cannot be modified from outside the class
    var counter: Int = 0
        private set

    fun increaseCounter() {
        counter++
    }
}

fun main() {
}
