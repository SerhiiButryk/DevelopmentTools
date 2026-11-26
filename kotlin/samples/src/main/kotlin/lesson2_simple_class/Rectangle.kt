package lesson2_simple_class

/**
 * A class with a primary and secondary constructor
 */
class Rectangle(private val height: Int, private val width: Int) {

    /** Property which doesn't contain data.
     * There is no need to define property for holding
     * information whether rectangle is square or not as
     * we can dynamically calculate this value.
     **/
    val isSquare: Boolean
        get() {
            return height == width
        }

    /**
     * Secondary constructor
     */
    constructor() : this(100,100) {

    }

}