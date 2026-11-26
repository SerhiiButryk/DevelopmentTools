package samples

/**
 * This demonstrates a good use of lambdas.
 *
 * Task:
 * Get some information from logs for some user by some complex criteria
 */
data class SiteVisit(val path: String, val duration: Double, val os: OS)

enum class OS { WINDOWS, LINUX, MAC, ANDROID, IOS }

// Example 1
// What if we need to select 2 OS ?
fun averageDurationForOs(log: List<SiteVisit>, os: OS): Double {
    return log.filter { it.os == os }
        .map(SiteVisit::duration)
        .average()
}

// Example 2
// What if the filtering requirement has changed ?
fun averageDurationForMobileOs(log: List<SiteVisit>): Double {
    return log.filter { it.os in setOf(OS.IOS, OS.ANDROID) }
        .map(SiteVisit::duration)
        .average()
}

// Example 3
// This is much better. It is extension function, and
// we can configure the filtering condition using param
fun List<SiteVisit>.averageDurationFor(predicate: (e: SiteVisit) -> Boolean): Double {
    return filter { predicate(it) }
        .map(SiteVisit::duration)
        .average()
}

fun main() {

    // Let's say we have a long log file...
    val logFile = listOf(SiteVisit("/", 3.2, OS.IOS),
        SiteVisit("/singup", 0.2, OS.ANDROID),
        SiteVisit("/view", 5.2, OS.IOS),
        SiteVisit("/singup", 3.2, OS.IOS),
        SiteVisit("/main", 3.2, OS.MAC),
        SiteVisit("/", 3.2, OS.LINUX),
        SiteVisit("/", 3.2, OS.WINDOWS))

    // Calculate average time for mobile users
    var result = logFile.averageDurationFor { it.os == OS.ANDROID || it.os == OS.IOS }
    println(result)

    // Calculate average time for Android users who visited registration page
    result = logFile.averageDurationFor { it.os == OS.ANDROID || it.path == "/singup" }
    println(result)
}