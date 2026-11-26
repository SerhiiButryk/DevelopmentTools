package samples

/**
 * Example of contact filtering program
 */

data class Person(val name: String, val lastName: String = "", val phoneNumber: String = "")

class ContactListFilter(private val prefix: String = "", private val shouldHavePhoneNumber: Boolean = false) {

    fun selectPredicate(): (Person) -> Boolean {

        val hasPrefix = {
                person: Person ->
            person.name.startsWith(prefix) || person.lastName.startsWith(prefix)
        }

        // If we don't need to check the phone number, return
        // a function that only checks the first name and last name
        if (!shouldHavePhoneNumber) {
            return hasPrefix
        }

        // else return function which checks also phone number
        val hasPrefixAndPhoneNumber = {
                person: Person ->
            (person.name.startsWith(prefix) || person.lastName.startsWith(prefix)) && person.phoneNumber.isNotEmpty()
        }

        return hasPrefixAndPhoneNumber
    }
}

fun main() {
    val contactsList = listOf(
        Person("Tom", "Semion", "123"),
        Person("Zom", "Femion"),
        Person("Aom", "Gemion", "123"),
        Person("Lom", "Hemion", "123"),
        Person("Pom", "Kemion"),
        Person("Eom", "Zemion", "123")
    )

    val prefix = "Z"
    val shouldHavePhoneNumber = true

    val contactListFilter = ContactListFilter(prefix, shouldHavePhoneNumber)
    val predicate = contactListFilter.selectPredicate()

    val result = contactsList.filter { person: Person -> predicate(person) }
    println(result.joinToString("\n"))
}