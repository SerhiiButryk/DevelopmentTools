/**
 * Regular expressions.
 *
 * Regular-expression constructs:
 *
 * \d+ - matches 1 digit or more
 * \b - matches a boundary before or after a whole word, for example "My dog"; it matches the position between "My"
 * and "dog"
 *
 * ? - matches 0 or 1 repetitions
 * * - matches 0 or more repetitions
 * + - 1 or more repetitions
 * {n} - matches exactly n repetitions
 *
 * Carriage return - '\r'
 * New line - '\n'
 * Form feed - '\f'
 * Tab - '\t'
 *
 * . = any char except newline
 * \. = the actual dot character
 * .? = .{0,1} = match any char except newline zero or one times
 * .* = .{0,} = match any char except newline zero or more times
 * .+ = .{1,} = match any char except newline one or more times
 *
 * Kotlin regex:
 * Regex.matchEntire() - Check if whole input string matches pattern
 * Regex.find() - Find first match if any in input string
 * Regex.findAll() - Find all first matches if any in input string
 * Regex.containsMatch() - Find all first matches if any in input string
 */

fun main() {

    // Match any character equals to '.' dot.
    // Character is escaped as it is a part of regular-expression constructs.
    val pattern1 = """\.""".toRegex()
    // Match any character equals to any digit followed by '.' dot character.
    val pattern2 = """\d+\.""".toRegex()
    // Match a space + word from capital letter
    val pattern3 = """\s+[A-Z]+""".toRegex()
    val pattern4 = """[A-Z]+""".toRegex()

    // Check if whole input string satisfies pattern
    var string = "AA"
    val matchResult = pattern4.matchEntire(string)
    if (matchResult != null) {
        // Iterate through List value
        for (matchedString in matchResult.groupValues) {
            //println("Result: $matchedString")
        }
        // Or get value directly
        println("Found match: \"${matchResult.value}\" of $pattern4 pattern in range: ${matchResult.range} in string \"$string\"")
    }

    string = "AA bbb Fd lF ZZ"
    val matchResult2: Sequence<MatchResult> = pattern4.findAll(string)

    // Using iterator.
    val iterator = matchResult2.iterator()
    while (iterator.hasNext()) {
        // Iterate using iterator
        val matchResult = iterator.next()

        // Range of indexes. Can be used to get first and last indexes.
        val startIndex = matchResult.range.first
        val endIndex = matchResult.range.last

        print("[Group size] ${matchResult.groups.size} ")
        println("[With iterator] Found match: \"${matchResult.value}\" of $pattern4 pattern in range: ${matchResult.range} in string \"$string\"")
    }

}