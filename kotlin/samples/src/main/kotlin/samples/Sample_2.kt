package samples

/**
 * Double dispatch in Kotlin
 * https://www.youtube.com/watch?v=4cUQR7-YV5U
 *
 * A double dispatch is an approach when we select a function or operation
 * based on the real receiver and argument type during runtime time.
 * It could be a feature of a language, or it could be implemented programmatically
 */

sealed interface People {
    val name: String
    fun meet(dog: Dog)
}

class PeopleA(override val name: String) : People {
    override fun meet(dog: Dog) = dog.reactTo(this)
}

class PeopleB(override val name: String) : People {
    override fun meet(dog: Dog) = dog.reactTo(this)
}

interface Dog {
    fun reactTo(people: People)
}

class DogA : Dog {

    override fun reactTo(people: People) = when (people) {
        is PeopleA -> println("DogA reactTo PeopleA call")
        is PeopleB -> println("DogA reactTo PeopleB call")
    }

}

class DogB : Dog {

    override fun reactTo(people: People) = when (people) {
        is PeopleA -> println("DogB reactTo PeopleA call")
        is PeopleB -> println("DogB reactTo PeopleB call")
    }

}

fun main() {

    val dogs = listOf(DogA(), DogB())
    val peoples = listOf(PeopleA("A"), PeopleB("B"))

    for (dog in dogs) {
        for (people in peoples) {
            // Note here that we call a function on Dog based on
            // 'people' object runtime type
            people.meet(dog)
        }
    }

}