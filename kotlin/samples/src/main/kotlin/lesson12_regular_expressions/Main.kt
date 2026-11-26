package lesson12_regular_expressions

/**
 * Regular expressions
 *
 * Kotlin adds a new operator for defining regular expressions.
 * It uses triple quotes: """...""". Here are examples of working with regular expressions.
 *
 * Concept of groups in regex:
 * https://pynative.com/python-regex-capturing-groups/
 *
 * Test here:
 * https://regexr.com/
 */
fun main() {

    // You do not need to escape special characters with this syntax
    val pattern1 = """(\.|-)"""
    val text = "12.345-6.A"

    val result = text.split(pattern1.toRegex())
    println(result)

    val result2 = text.split(".", "-")
    println(result2)

    val path = "/Users/myname/desktop/file/my.doc"

    // You do not need to escape special characters with this syntax
    val regex = """(.+)/(.+)\.(.+)""".toRegex()

    val matchResult = regex.findAll(path)
    for (result in matchResult) {

        println("Group size " +
                "${result.groups.size}")

        println("${result.groups[0]?.value}")
        println("${result.groups[1]?.value}")
        println("${result.groups[2]?.value}")
        println("${result.groups[3]?.value}")

        // The same result as above
        val (directory, filename, extension) = result.destructured
        println("Directory: $directory, file name: $filename, extension: $extension")
    }
}