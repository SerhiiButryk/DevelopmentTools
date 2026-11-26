package lesson2_simple_class

/**
 * An enum class.
 * Each enum constant field is a single object at runtime.
 */
enum class Color(val red: Int, val green: Int, val blue: Int) {

    RED(255, 0, 0),
    ORANGE(255, 165, 0),
    YELLOW(255, 255, 0),
    BLUE(0, 0, 255),
    INDIGO(75, 0, 130),
    VIOLET(238, 130, 238),
    GREEN(0, 255, 0);

    fun rgb() = (red * 256 + green) * 256 + blue
}